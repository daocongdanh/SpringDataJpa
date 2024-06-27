package com.example.springdatajpa.repositories;

import com.example.springdatajpa.repositories.custom.SearchProduct;
import com.example.springdatajpa.responses.PageReponses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public PageReponses getAllProductsWithSortByMultipleColumnsAndSearch(
            int pageNo, int pageSize, String[] sortBy, String... search) {
        StringBuilder sql = new StringBuilder("select p from Product p where 1 = 1");
        pageNo = pageNo > 0 ? pageNo - 1 : pageNo;

        // Search
        if(search != null){
            List<SearchProduct> searchProducts = new ArrayList<>();
            for(String s : search){
                Pattern pattern = Pattern.compile("(.*)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    SearchProduct searchProduct
                            = new SearchProduct(matcher.group(1),matcher.group(2),matcher.group(3));
                    searchProducts.add(searchProduct);
                }
            }
            for(SearchProduct searchProduct : searchProducts){
                if(searchProduct.getOperator().equals(">")){
                    sql.append(String.format(" and p.%s > %f",
                            searchProduct.getKey(), Float.parseFloat(searchProduct.getValue().toString())));
                }
                else if(searchProduct.getOperator().equals("<")){
                    sql.append(String.format(" and p.%s < %f",
                            searchProduct.getKey(), Float.parseFloat(searchProduct.getValue().toString())));
                }
                else{
                    sql.append(String.format(" and p.%s like '%%%s%%'",
                            searchProduct.getKey(), searchProduct.getValue().toString()));

                }
            }
        }

        //Sort
        for(int i=0 ; i<sortBy.length ; i++){
            //firstName:asc
            Pattern pattern = Pattern.compile("(.*)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy[i]);
            if(matcher.find()){
                if(i != sortBy.length - 1) {
                    sql.append(String.format(" order by p.%s %s ,", matcher.group(1), matcher.group(3)));
                }
                else{
                    sql.append(String.format(" p.%s %s", matcher.group(1), matcher.group(3)));
                }
            }
        }

        Query query = entityManager.createQuery(sql.toString());
        // Pagination
        query.setFirstResult(pageNo);
        query.setMaxResults(pageSize);
        List products = query.getResultList();

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<?> page = new PageImpl<Object>(products, pageable, totalProduct(search));
        return PageReponses.builder()
                .pageNo(pageNo+1)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .data(page.getContent())
                .build();
    }

    private long totalProduct(String... search){
        StringBuilder sqlCount = new StringBuilder("select count(*) from Product p where 1 = 1");

        if(search != null){
            List<SearchProduct> searchProducts = new ArrayList<>();
            for(String s : search){
                Pattern pattern = Pattern.compile("(.*)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    SearchProduct searchProduct
                            = new SearchProduct(matcher.group(1),matcher.group(2),matcher.group(3));
                    searchProducts.add(searchProduct);
                }
            }
            for(SearchProduct searchProduct : searchProducts){
                if(searchProduct.getOperator().equals(">")){
                    sqlCount.append(String.format(" and p.%s > %f",
                            searchProduct.getKey(), Float.parseFloat(searchProduct.getValue().toString())));
                }
                else if(searchProduct.getOperator().equals("<")){
                    sqlCount.append(String.format(" and p.%s < %f",
                            searchProduct.getKey(), Float.parseFloat(searchProduct.getValue().toString())));
                }
                else{
                    sqlCount.append(String.format(" and p.%s like '%%%s%%'",
                            searchProduct.getKey(), searchProduct.getValue().toString()));

                }
            }
        }
        Query query = entityManager.createQuery(sqlCount.toString());
        return (long)query.getSingleResult();
    }
}
