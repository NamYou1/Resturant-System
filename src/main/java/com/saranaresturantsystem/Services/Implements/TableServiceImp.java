package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.DTO.Request.TableRequest;
import com.saranaresturantsystem.DTO.Response.TableResponse;
import com.saranaresturantsystem.Enities.Group;
import com.saranaresturantsystem.Enities.TableEnities;
import com.saranaresturantsystem.Execption.DuplicateResourceException;
import com.saranaresturantsystem.Execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.Mappers.TableMapper;
import com.saranaresturantsystem.Repositories.GroupRepository;
import com.saranaresturantsystem.Repositories.TableRepository;
import com.saranaresturantsystem.Services.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TableServiceImp implements TableService {
    private final TableRepository  tableRepository;
    private final TableMapper tableMapper;
    private final GroupRepository groupRepository;

    @Override
    public List<TableResponse> getAllTables() {
        List<TableEnities> entities = tableRepository.findAll();
        return tableMapper.toResponseList(entities);
    }

    @Override
    public TableResponse getTableById(Long id) {
        TableEnities entity = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("Table", id));
        return tableMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public TableResponse createTable(TableRequest request) {
        if (tableRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Table name '" + request.getName() + "' already exists.");
        }
//        if (tableRepository.existsByOrderNumber(request.getOrderNumber())) {
//            throw new DuplicateResourceException("Order number '" + request.getOrderNumber() + "' already exists.");
//        }
        TableEnities entity = tableMapper.toEntity(request);
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundExecption("Group", request.getGroupId()));

        entity.setTableGroup(group);

        TableEnities saved = tableRepository.save(entity);
        return tableMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TableResponse updateTable(Long id, TableRequest request) {
        TableEnities entity = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("Table", id));

        tableRepository.findByName(request.getName()).ifPresent(existing -> {
            if (!existing.getTableId().equals(id)) {
                throw new DuplicateResourceException("Name already existed");
            }
        });
        tableMapper.updateEntityFromRequest(request, entity);

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundExecption("Group", request.getGroupId()));

        entity.setTableGroup(group);

        return tableMapper.toResponse(tableRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundExecption("Table", id);
        }
        tableRepository.deleteById(id);
    }
}
