package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.TableRequest;
import com.saranaresturantsystem.dto.response.TableResponse;
import com.saranaresturantsystem.entities.Tables;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.TableMapper;
import com.saranaresturantsystem.repositories.TableRepository;
import com.saranaresturantsystem.services.TableService;
import com.saranaresturantsystem.specification.settings.tables.TableFilter;
import com.saranaresturantsystem.specification.settings.tables.TableSpec;
import com.saranaresturantsystem.utils.GloblePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        int pageNumber = params.containsKey(GloblePagination.PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER))
                : GloblePagination.DEFAULT_PAGE_NUMBER;
        int pageSize = params.containsKey(GloblePagination.PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
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
        tables.setStatus(GeneralStatus.INACTIVE);
        tableRepository.save(tables);
    }

    @Override
    public Tables findById(Long id) {
        return  tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExecption("Table", id));
    }
}
