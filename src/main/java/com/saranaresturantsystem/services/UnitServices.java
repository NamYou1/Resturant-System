package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.UnitRequest;
import com.saranaresturantsystem.dto.response.UnitResponse;
import com.saranaresturantsystem.entities.Unit;
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