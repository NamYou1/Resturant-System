package com.saranaresturantsystem.Services.Implements;

import com.saranaresturantsystem.DTO.Request.GroupRequest;
import com.saranaresturantsystem.DTO.Response.GroupResponse;
import com.saranaresturantsystem.Enities.Group;
import com.saranaresturantsystem.Execption.DuplicateResourceException;
import com.saranaresturantsystem.Execption.ResourceNotFoundExecption;
import com.saranaresturantsystem.Mappers.GroupMapper;
import com.saranaresturantsystem.Repositories.GroupRepository;
import com.saranaresturantsystem.Services.GroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImp implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Override
    public List<GroupResponse> getAllGroups() {
        return groupMapper.toResponseList(groupRepository.findAll());
    }

    @Override
    public GroupResponse getGroupById(@Positive Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("Group", id));
        return groupMapper.toResponse(group);
    }


    @Override
    @Transactional
    public GroupResponse createGroup(@Valid GroupRequest request) {
        // Check if group name already exists
        if (groupRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Group with name '" + request.getName() + "' already exists.");
        }

        Group group = groupMapper.toEntity(request);
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toResponse(savedGroup);
    }

    @Override
    @Transactional
    public GroupResponse updateGroup(@Positive Long id, @Valid GroupRequest request) {
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("Group", id));

        // Check if the new name is taken by another group
        groupRepository.findByName(request.getName()).ifPresent(group -> {
            if (!group.getId().equals(id)) {
                throw new DuplicateResourceException("Group name '" + request.getName() + "' is already existed.");
            }
        });

        groupMapper.updateEntityFromRequest(request, existingGroup);
        Group updatedGroup = groupRepository.save(existingGroup);
        return groupMapper.toResponse(updatedGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(@Positive Long id) {
        if (!groupRepository.existsById(id)) {
            throw new ResourceNotFoundExecption("Group", id);
        }
        groupRepository.deleteById(id);
    }
}