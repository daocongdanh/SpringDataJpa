package com.example.springdatajpa.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageReponses {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private Object data;
}
