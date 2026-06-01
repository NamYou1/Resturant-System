//package com.saranaresturantsystem.services.impl;
//
//import com.saranaresturantsystem.common.UniqueChecker;
//import com.saranaresturantsystem.dto.request.CustomerRequest;
//import com.saranaresturantsystem.dto.response.CustomerResponse;
//import com.saranaresturantsystem.dto.response.sales.CustomerResponse;
//import com.saranaresturantsystem.entities.sales.Customer;
//import com.saranaresturantsystem.entities.sales.Group;
//import com.saranaresturantsystem.entities.inventory.Store;
//import com.saranaresturantsystem.enums.StatusType;
//import com.saranaresturantsystem.execption.ResourceNotFoundException;
//import com.saranaresturantsystem.mappers.CustomerMapper;
//import com.saranaresturantsystem.repositories.sales.CustomerRepository;
//import com.saranaresturantsystem.services.CustomerService;
//import com.saranaresturantsystem.services.GroupService;
//import com.saranaresturantsystem.services.StoreService;
//import com.saranaresturantsystem.specification.customers.CustomerFilter;
//import com.saranaresturantsystem.specification.customers.CustomerSpec;
//import com.saranaresturantsystem.utils.PageUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import tools.jackson.databind.ObjectMapper;
//
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class CustomerServiceImp implements CustomerService {
//    private  final CustomerRepository customerRepository;
//    private final CustomerMapper customerMapper;
//    private final ObjectMapper objectMapper;
//    private final StoreService storeService;
//    private final GroupService groupService;
//    private final UniqueChecker uniqueChecker;
//
//    @Override
//    public Page<CustomerResponse> getListCustomer(Map<String, String> params) {
//        CustomerFilter filter = objectMapper.convertValue(params, CustomerFilter.class);
//        Pageable pageable = PageUtil.fromParams(params);
//        Specification<Customer> spec = CustomerSpec.filterBy(filter);
//        return customerRepository.findAll(spec, pageable)
//                .map(customerMapper::toResponse);
//    }
//
//    @Override
//    public Customer getCustomerById(Long id) {
//        Customer customer = customerRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
//        if (customer.getStatus() == StatusType.INACTIVE || customer.getStatus() == null) {
//            throw new ResourceNotFoundException("Customer", id);
//        }
//        return customer;
//    }
//
//    @Override
//    public CustomerResponse getCustomerResponseById(Long id) {
//        return customerMapper.toResponse(getCustomerById(id));
//    }
//
//    @Override
//    public CustomerResponse createCustomer(CustomerRequest request) {
//        Store store = storeService.findById(request.getStoreId());
//        Group tableGroup = groupService.findById(request.getTableGroupId());
//
//        Customer customer = customerMapper.toEntity(request);
//        customer.setStore(store);
//        customer.setTableGroup(tableGroup);
//        customer.setPriceGroupName("Cash");
//        customer.setTableGroupName("VIP");
//        customer.setStatus(StatusType.ACTIVE);
//        validateUniqueness(customer);
//        return customerMapper.toResponse(customerRepository.save(customer));
//    }
//
//    @Override
//    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
//        Customer customer = getCustomerById(id);
//        Store store = storeService.findById(request.getStoreId());
//        Group tableGroup = groupService.findById(request.getTableGroupId());
//
//        customerMapper.updateEntityFromRequest(request, customer);
//        customer.setStore(store);
//        customer.setTableGroup(tableGroup);
//        validateUniqueness(customer);
//        return customerMapper.toResponse(customerRepository.save(customer));
//    }
//
//    @Override
//    public void deleteCustomer(Long id) {
//        Customer customer = getCustomerById(id);
//        customer.setStatus(StatusType.INACTIVE);
//        customerRepository.save(customer);
//    }
//
//    private void validateUniqueness(Customer customer) {
//        uniqueChecker.verify(customerRepository, customer, "phone", customer.getPhone());
//        uniqueChecker.verify(customerRepository, customer, "email", customer.getEmail());
//    }
//}
