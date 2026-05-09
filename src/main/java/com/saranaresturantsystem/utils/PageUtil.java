package com.saranaresturantsystem.utils;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public interface PageUtil {
    int DEFAULT_PAGE_SIZE = 10;
    int DEFAULT_PAGE = 1;

    String PAGE_LIMIT = "size";
    String PAGE_NUMBER = "page";

    static Pageable getPageable(int pageNumber, int pageSize) {

//        if (pageNumber < 1) {
//            pageNumber = DEFAULT_PAGE;
//        }

//        if (pageSize < 1) {
//            pageSize = DEFAULT_PAGE_SIZE;
//        }
        if (pageNumber < DEFAULT_PAGE_SIZE){ pageNumber = DEFAULT_PAGE; }
        if (pageSize<1){ pageSize = DEFAULT_PAGE; }

        return PageRequest.of(pageNumber - 1, pageSize);
    }
}
