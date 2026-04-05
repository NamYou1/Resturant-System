package com.saranaresturantsystem.Services;

import com.saranaresturantsystem.DTO.Request.GroupRequest;
import com.saranaresturantsystem.DTO.Response.GroupResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getAllGroups();
    GroupResponse getGroupById(@Positive Long id);
    GroupResponse createGroup(@Valid GroupRequest request);
    GroupResponse updateGroup(@Positive Long id,@Valid GroupRequest request);
    void deleteGroup(@Positive Long id);
}
