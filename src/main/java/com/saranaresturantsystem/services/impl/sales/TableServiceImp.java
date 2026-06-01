package com.saranaresturantsystem.services.impl.sales;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.sales.TableRequest;
import com.saranaresturantsystem.dto.response.sales.TableResponse;
import com.saranaresturantsystem.entities.sales.Tables;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.sales.TableMapper;
import com.saranaresturantsystem.repositories.sales.TableRepository;
import com.saranaresturantsystem.services.TableService;
import com.saranaresturantsystem.specification.sales.tables.TableFilter;
import com.saranaresturantsystem.specification.sales.tables.TableSpec;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class TableServiceImp implements TableService {
    private final TableRepository  tableRepository;
    private final TableMapper tableMapper;
    private  final ObjectMapper objectMapper ;
    private  final UniqueChecker uniqueChecker ;

    @Override
    public Page<TableResponse> getAllTables(Map<String, String> params) {
        TableFilter filter =  objectMapper.convertValue(params, TableFilter.class);
        Pageable pageable = PageUtil.fromParams(params);

        Specification<Tables> spec = TableSpec.filterBy(filter);
        return  tableRepository.findAll(spec, pageable).map(tableMapper::toResponse);
    }

    @Override
    public TableResponse getTableById(Long id) {
        Tables tables = findById(id);
        return tableMapper.toResponse(tables);
    }
    @Override
    @Transactional
    public TableResponse createTable(TableRequest request) {
        Tables tables = tableMapper.toEntity(request);
        uniqueChecker.verify(tableRepository , tables , "name"  , tables.getName());
        uniqueChecker.verify(tableRepository , tables , "orderNumber"  , tables.getOrderNumber());
        Tables saved = tableRepository.save(tables);
        return tableMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TableResponse updateTable(Long id, TableRequest request) {
        Tables tables = findById(id);
        uniqueChecker.verify(tableRepository , tables , "name"  , tables.getName());
        uniqueChecker.verify(tableRepository , tables , "orderNumber"  , tables.getOrderNumber());
        tableMapper.updateEntityFromRequest(request, tables);
        tableRepository.save(tables);
        return tableMapper.toResponse(tableRepository.save(tables));
    }

    @Override
    @Transactional
    public void deleteTable(Long id) {
        Tables tables = findById(id);
        tables.setStatus(StatusType.INACTIVE);
        tableRepository.save(tables);
    }

    @Override
    public Tables findById(Long id) {
        return  tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table", id));
    }
}
