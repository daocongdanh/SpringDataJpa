package com.example.springdatajpa;

import com.example.springdatajpa.models.Category;
import com.example.springdatajpa.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);

    }
    @Bean
    public CommandLineRunner commandLineRunner(EntityManager entityManager){
        return runner -> {
//            testCriteria1(entityManager);
//            testCriteriaGroupByAndJoin(entityManager);
        };
    }

    public void testCriteria1(EntityManager entityManager){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder(); // Lấy ra đối tượng CriteriaBuilder
        CriteriaQuery<Product> query = builder.createQuery(Product.class); // Truy vấn trên đối tượng Product
        Root<Product> root = query.from(Product.class);

        // Lấy ra tất cả bảng ghi
        query.select(root);

        // Lấy ra với điều kiện
//            Predicate p1 = builder.like(root.get("name"),"%ASOS%");
//            Predicate p2 = builder.greaterThan(root.get("price"),100);
//            query.where(p1,p2);

        Predicate p1 = builder.like(root.get("name"),"%ASOS%");
        Predicate p2 = builder.like(root.get("name"),"%The North%");
        query.where(builder.or(p1,p2));
        List<Order> orders = new ArrayList<>();
        orders.add(builder.asc(root.get("price")));
        orders.add(builder.desc(root.get("id")));
        query.orderBy(orders);
        List<Product> products = entityManager.createQuery(query).getResultList();
        for(Product p : products){
            System.out.println(p);
        }
    }

    public void testCriteriaGroupByAndJoin(EntityManager entityManager){
        // Thống kê tổng tiền, tổng hàng tồn kho theo từng loại danh mục
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Product> pRoot = query.from(Product.class);
        Join<Product, Category> cJoin = pRoot.join("category");
        query.multiselect(
                cJoin.get("name"),
                builder.sum(pRoot.get("price")),
                builder.sum(pRoot.get("unitStock")));
        query.groupBy(cJoin.get("name"));

        List<Object[]> result = entityManager.createQuery(query).getResultList();
        for(Object[] ob : result){
            System.out.println(ob[0] + " " + ob[1] + " " + ob[2]);
        }
    }
}
