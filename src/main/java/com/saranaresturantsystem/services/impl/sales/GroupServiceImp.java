package com.saranaresturantsystem.services.impl.sales;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.sales.GroupRequest;
import com.saranaresturantsystem.dto.response.sales.GroupResponse;
import com.saranaresturantsystem.entities.sales.Group;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.sales.GroupMapper;
import com.saranaresturantsystem.repositories.sales.GroupRepository;
import com.saranaresturantsystem.services.GroupService;
import com.saranaresturantsystem.specification.sales.groups.GroupFilter;
import com.saranaresturantsystem.specification.sales.groups.GroupSpec;
import com.saranaresturantsystem.utils.PageUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
public class GroupServiceImp implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private  final UniqueChecker uniqueChecker;
    private  final ObjectMapper objectMapper ;


    @Override
    public Page<GroupResponse> getAllGroups(Map<String, String> params) {
        GroupFilter filter = objectMapper.convertValue(params, GroupFilter.class);
        Pageable pageable = PageUtil.fromParams(params);
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
        groupMapper.updateEntityFromRequest(request, existingGroup);
        Group updatedGroup = groupRepository.save(existingGroup);
        return groupMapper.toResponse(updatedGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(@Positive Long id) {
        Group exitstingGroup = findById(id);
        exitstingGroup.setStatus(StatusType.INACTIVE);
        groupRepository.save(exitstingGroup);
    }

    @Override
    public Group findById(@Positive Long id) {
        Group group= groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));
        if (group.getStatus().equals(StatusType.INACTIVE)) {
            throw new ResourceNotFoundException("Group", id);
        }
        return  group;
    }
}