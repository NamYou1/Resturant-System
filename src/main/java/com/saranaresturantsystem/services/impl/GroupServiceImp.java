package com.saranaresturantsystem.services.impl;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.GroupRequest;
import com.saranaresturantsystem.dto.response.GroupResponse;
import com.saranaresturantsystem.entities.Group;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import com.saranaresturantsystem.execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.mappers.GroupMapper;
import com.saranaresturantsystem.repositories.GroupRepository;
import com.saranaresturantsystem.services.GroupService;
import com.saranaresturantsystem.specification.settings.groups.GroupFilter;
import com.saranaresturantsystem.specification.settings.groups.GroupSpec;
import com.saranaresturantsystem.utils.GloblePagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
public class GroupServiceImp implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private  final UniqueChecker uniqueChecker;
    private  final ObjectMapper objectMapper ;


    @Override
    public Page<GroupResponse> getAllGroups(Map<String, String> params) {
        GroupFilter filter = objectMapper.convertValue(params, GroupFilter.class);
        int pageNumber = params.containsKey(GloblePagination.PAGE_NUMBER)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_NUMBER))
                : GloblePagination.DEFAULT_PAGE_NUMBER;
        int pageSize = params.containsKey(GloblePagination.PAGE_LIMIT)
                ? Integer.parseInt(params.get(GloblePagination.PAGE_LIMIT))
                : GloblePagination.DEFAULT_PAGE_LIMIT;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Group> spec = GroupSpec.filterBy(filter);
        return  groupRepository.findAll(spec, pageable).map(groupMapper::toResponse);
    }

    @Override
    public GroupResponse getGroupById(@Positive Long id) {
        Group group = findById(id);
        return groupMapper.toResponse(group);
    }


    @Override
    @Transactional
    public GroupResponse createGroup(@Valid GroupRequest request) {
        Group group = groupMapper.toEntity(request);
        // check duplicate
        uniqueChecker.verify(groupRepository,group , "name", group.getName());
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toResponse(savedGroup);
    }


    @Override
    @Transactional
    public GroupResponse updateGroup(@Positive Long id, @Valid GroupRequest request) {
        Group existingGroup = findById(id);
        uniqueChecker.verify(groupRepository,existingGroup , "name", existingGroup.getName());
        groupMapper.updateEntityFromRequest(request, existingGroup);
        Group updatedGroup = groupRepository.save(existingGroup);
        return groupMapper.toResponse(updatedGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(@Positive Long id) {
        Group exitstingGroup = findById(id);
        exitstingGroup.setStatus(GeneralStatus.INACTIVE);
        groupRepository.save(exitstingGroup);
    }

    @Override
    public Group findById(@Positive Long id) {
        return  groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("Group", id));
    }
}