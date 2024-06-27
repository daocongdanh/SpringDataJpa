package com.example.springdatajpa.services;

import com.example.springdatajpa.responses.PageReponses;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    PageReponses getAllProductsWithSortBy(int pageNo, int pageSize, String sortBy);

    PageReponses getAllProductsWithSortByMultipleColumns(int pageNo, int pageSize, String ...sortBy);

    PageReponses getAllProductsWithSortByMultipleColumnsAndSearch(
            int pageNo, int pageSize, String[] sortBy,String ...search);
}
