package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.inventory.TransferRequest;
import com.saranaresturantsystem.dto.response.inventory.TransferResponse;
import org.springframework.data.domain.Page;


import java.util.Map;

public interface TransferService {

    TransferResponse create(TransferRequest request);

    TransferResponse getById(Long id);

    Page<TransferResponse> getAll(Map<String , String> params);

    TransferResponse update(Long id, TransferRequest request, String updatedBy);

    TransferResponse approve(Long id, String updatedBy);

    TransferResponse complete(Long id, String updatedBy);

    TransferResponse cancel(Long id, String updatedBy);

    void delete(Long id, String deletedBy);
}