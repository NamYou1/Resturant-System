package com.saranaresturantsystem.services.impl.product;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.product.UnitRequest;
import com.saranaresturantsystem.dto.response.product.UnitResponse;
import com.saranaresturantsystem.entities.product.Unit;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.product.UnitMapper;
import com.saranaresturantsystem.repositories.product.UnitRepository;
import com.saranaresturantsystem.services.UnitServices;
import com.saranaresturantsystem.specification.products.units.UnitFilter; // You'll need to create this
import com.saranaresturantsystem.specification.products.units.UnitSpec;     // You'll need to create this
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        Pageable pageable = PageUtil.fromParams(params);

        Specification<Unit> spec = UnitSpec.filterBy(filter);
        return unitRepository.findAll(spec, pageable)
                .map(unitMapper::toUnitResponse);
    }
    @Override
    public Unit getUnitById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", id));
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
        unit.setStatus(StatusType.INACTIVE);
        unitRepository.save(unit);
    }
}