package com.saranaresturantsystem.services;

import com.saranaresturantsystem.dto.Request.CustomerRequest;
import com.saranaresturantsystem.dto.Response.CustomerResponse;
import com.saranaresturantsystem.entities.Customer;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CustomerService {
    Page<CustomerResponse> getListCustomer(Map<String, String> params);
    Customer getCustomerById(Long id);
    CustomerResponse getCustomerResponseById(Long id);
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);
}
