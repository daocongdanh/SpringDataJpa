package com.example.springdatajpa.controllers;

import com.example.springdatajpa.repositories.ProductRepository;
import com.example.springdatajpa.responses.PageReponses;
import com.example.springdatajpa.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    /**
     * Lấy tất cả product kết hợp phân trang, sort
     * */
    @GetMapping("")
    public PageReponses getAllProductsWithSortBy(@RequestParam(defaultValue = "1") int pageNo,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(required = false) String sortBy){
        return productService.getAllProductsWithSortBy(pageNo, pageSize, sortBy);
    }


    /**
     * Lấy tất cả product kết hợp phân trang, sort trên nhiều columns
     * */
    @GetMapping("/sort-multiple-columns")
    public PageReponses getAllProductsWithSortByMultipleColumns(@RequestParam(defaultValue = "1") int pageNo,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(required = false) String ...sortBy){
        return productService.getAllProductsWithSortByMultipleColumns(pageNo, pageSize,sortBy);
    }

    /**
     * Sử dụng @Query
     * */
    @GetMapping("/join-with-annotation-query")
    public List<Object> joinWithAnnotationQuery(String search){
        return productRepository.getAllProductWithAnnotation(search);
    }


    /**
     * Custom query using EntityManager
     * Lấy tất cả product kết hợp phân trang, sort trên nhiều columns
     * Tìm kiếm trên nhiều columns
     * */
    @GetMapping("/sort-multiple-columns-search")
    public PageReponses getAllProductsWithSortByMultipleColumnsAndSearch
            (@RequestParam(defaultValue = "1") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(required = false) String[ ] sortBy,
             @RequestParam(required = false) String ...search){
        return productService.getAllProductsWithSortByMultipleColumnsAndSearch(pageNo, pageSize,sortBy,search);
    }
}
