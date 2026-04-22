package com.saranaresturantsystem.services;


import com.saranaresturantsystem.dto.request.TableRequest;
import com.saranaresturantsystem.dto.response.TableResponse;
import com.saranaresturantsystem.entities.Tables;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TableService {

    Page<TableResponse> getAllTables(Map<String , String >params);
    TableResponse getTableById(@Positive Long id);
    TableResponse createTable(@Valid TableRequest request);
    TableResponse updateTable(@Positive Long id, @Valid TableRequest request);
    void deleteTable(@Positive Long id);
    Tables findById(@Positive Long id );
}
