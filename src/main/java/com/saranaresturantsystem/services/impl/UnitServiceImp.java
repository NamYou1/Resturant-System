package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.UnitRequest;
import com.saranaresturantsystem.dto.response.UnitResponse;
import com.saranaresturantsystem.entities.Unit;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.UnitMapper;
import com.saranaresturantsystem.repositories.UnitRepository;
import com.saranaresturantsystem.services.UnitServices;
import com.saranaresturantsystem.specification.settings.units.UnitFilter; // You'll need to create this
import com.saranaresturantsystem.specification.settings.units.UnitSpec;     // You'll need to create this
import com.saranaresturantsystem.utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UnitServiceImp implements UnitServices {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;
    private final ObjectMapper objectMapper ;
    private final UniqueChecker uniqueChecker;
    @Override
    public Page<UnitResponse> getAllUnits(Map<String, String> params) {
        UnitFilter filter = objectMapper.convertValue(params, UnitFilter.class);
        int pageLimit = params.containsKey(GloblePagination.DEFAULT_PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;
        int pageNumber = params.containsKey(GloblePagination.DEFAULT_PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.DEFAULT_PAGE_NUMBER))
                : 0;
        Pageable pageable = PageRequest.of(pageNumber, pageLimit);
        Specification<Unit> spec = UnitSpec.filterBy(filter);
        return unitRepository.findAll(spec, pageable)
                .map(unitMapper::toUnitResponse);
    }
    @Override
    public Unit getUnitById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("Unit", id));
    }
    @Override
    public UnitResponse findById(Long id) {
        return unitMapper.toUnitResponse(getUnitById(id));
    }
    @Override
    public UnitResponse createUnit(UnitRequest request) {
        Unit unit = unitMapper.toUnit(request);
        uniqueChecker.verify(unitRepository, unit, "code", unit.getCode());
        Unit savedUnit = unitRepository.save(unit);
        return unitMapper.toUnitResponse(savedUnit);
    }
    @Override
    public UnitResponse updateUnit(Long id, UnitRequest request) {
        Unit unit = getUnitById(id);
        unitMapper.updateUnitFromRequest(request, unit);
        uniqueChecker.verify(unitRepository, unit, "code", unit.getCode());
        Unit updatedUnit = unitRepository.save(unit);
        return unitMapper.toUnitResponse(updatedUnit);
    }
    @Override
    public void deleteUnit(Long id) {
        Unit unit = getUnitById(id);
//        unit.setDeleteFlag(1);
        unit.setStatus(GeneralStatus.INACTIVE);
        unitRepository.save(unit);
    }
}