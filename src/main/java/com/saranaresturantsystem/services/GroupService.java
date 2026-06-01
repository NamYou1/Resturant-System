package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.request.sales.GroupRequest;
import com.saranaresturantsystem.dto.response.sales.GroupResponse;
import com.saranaresturantsystem.entities.sales.Group;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface GroupService {
    Page<GroupResponse> getAllGroups(Map<String, String> params);
    GroupResponse getGroupById(@Positive Long id);
    GroupResponse createGroup(@Valid GroupRequest request);
    GroupResponse updateGroup(@Positive Long id,@Valid GroupRequest request);
    void deleteGroup(@Positive Long id);
    Group findById(@Positive Long id );
}
