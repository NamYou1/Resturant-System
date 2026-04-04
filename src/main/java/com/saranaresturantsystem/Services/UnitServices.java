package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.UnitRequest;
import com.saranaresturantsystem.DTO.Response.UnitResponse;
import com.saranaresturantsystem.Enities.Unit;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UnitServices {
    Page<UnitResponse> getAllUnits(Map<String, String> params);
    Unit getUnitById(Long id);
    UnitResponse findById(Long id);
    UnitResponse createUnit(UnitRequest request);
    UnitResponse updateUnit(Long id, UnitRequest request);
    void deleteUnit(Long id);
}