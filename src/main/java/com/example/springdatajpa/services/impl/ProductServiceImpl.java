package com.example.springdatajpa.services.impl;

import com.example.springdatajpa.models.Product;
import com.example.springdatajpa.repositories.ProductRepository;
import com.example.springdatajpa.repositories.SearchRepository;
import com.example.springdatajpa.responses.PageReponses;
import com.example.springdatajpa.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SearchRepository searchRepository;

    @Override
    public PageReponses getAllProductsWithSortBy(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo > 0 ? pageNo - 1 : pageNo;

        List<Sort.Order> sorts = new ArrayList<>();
        //Nếu có giá trị
        if(StringUtils.hasLength(sortBy)){
            //firstName:asc
            Pattern pattern = Pattern.compile("(.*)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
                else{
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));
        Page<Product> productPage = productRepository.findAll(pageable);
        return PageReponses.builder()
                .pageNo(pageNo+1)
                .pageSize(pageSize)
                .totalPage(productPage.getTotalPages())
                .data(productPage.getContent())
                .build();
    }

    @Override
    public PageReponses getAllProductsWithSortByMultipleColumns(int pageNo, int pageSize, String... sortBy) {
        pageNo = pageNo > 0 ? pageNo - 1 : pageNo;

        List<Sort.Order> sorts = new ArrayList<>();
        //Nếu có giá trị

        for(String sort : sortBy){
            //firstName:asc
            Pattern pattern = Pattern.compile("(.*)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
                else{
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));
        Page<Product> productPage = productRepository.findAll(pageable);
        return PageReponses.builder()
                .pageNo(pageNo+1)
                .pageSize(pageSize)
                .totalPage(productPage.getTotalPages())
                .data(productPage.getContent())
                .build();
    }

    @Override
    public PageReponses getAllProductsWithSortByMultipleColumnsAndSearch(int pageNo, int pageSize, String[] sortBy, String... search) {
        return searchRepository.getAllProductsWithSortByMultipleColumnsAndSearch(pageNo,pageSize,sortBy,search);
    }
}
