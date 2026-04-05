package com.saranaresturantsystem.Services;


import com.saranaresturantsystem.DTO.Request.TableRequest;
import com.saranaresturantsystem.DTO.Response.TableResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface TableService {

    List<TableResponse> getAllTables();
    TableResponse getTableById(@Positive Long id);
    TableResponse createTable(@Valid TableRequest request);
    TableResponse updateTable(@Positive Long id, @Valid TableRequest request);
    void deleteTable(@Positive Long id);
}
