package com.example.springdatajpa.repositories.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProduct {
    private String key;
    private String operator; // : -> =, > -> >, < -> <
    private Object value;
}
