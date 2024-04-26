package com.jovisco.spring6restmvc.services;

import org.springframework.data.domain.PageRequest;

public class PageRequestBuilder {

    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static PageRequest build(Integer pageSize, Integer pageNumber) {

        // handle default values for pagination
        int queryPageSize = DEFAULT_PAGE_SIZE;
        int queryPageNumber = DEFAULT_PAGE_NUMBER;

        if (pageSize != null && pageSize > 0) {
            queryPageSize = pageSize;
        }
        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        }

        return PageRequest.of(queryPageNumber, queryPageSize);
    }
}
