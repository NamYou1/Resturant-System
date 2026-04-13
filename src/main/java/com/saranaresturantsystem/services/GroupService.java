package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.GroupRequest;
import com.saranaresturantsystem.dto.response.GroupResponse;
import com.saranaresturantsystem.entities.Group;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface GroupService {
    Page<GroupResponse> getAllGroups(Map<String, String> params);
    GroupResponse getGroupById(@Positive Long id);
    GroupResponse createGroup(@Valid GroupRequest request);
    GroupResponse updateGroup(@Positive Long id,@Valid GroupRequest request);
    void deleteGroup(@Positive Long id);
    Group findById(@Positive Long id );
}
