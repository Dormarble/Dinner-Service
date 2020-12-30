package com.dudungtak.seproject.service;

import com.dudungtak.seproject.network.Pagination;
import org.springframework.data.domain.Page;

public abstract class BaseCrudApiService {
    public static Pagination pagination(Page page) {
        return Pagination.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .currentPage(page.getNumber())
                .currentElements(page.getNumberOfElements())
                .build();
    }
}
