//package com.saranaresturantsystem.controllers.People;
//
//import com.saranaresturantsystem.dto.PageDTO;
//import com.saranaresturantsystem.dto.request.sales.CustomerRequest;
//import com.saranaresturantsystem.dto.response.sales.CustomerResponse;
//import com.saranaresturantsystem.services.CustomerService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.saranaresturantsystem.dto.response.ApiResponse;
//
//import java.time.Instant;
//import java.util.Map;
//
//@RestController
//@Data
//@AllArgsConstructor
//@RequestMapping("api/v1/customer")
//@Tag(name = "Customer", description = "Endpoints for managing customer")
//public class CustomerController {
//    private final CustomerService customerService;
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<PageDTO>> getListCustomer(
//            @RequestParam Map<String , String> params
//    ) {
//        Page<CustomerResponse> customerPage = customerService.getListCustomer(params);
//        PageDTO pageDTO = new PageDTO(customerPage);
//
//        ApiResponse<PageDTO> response = ApiResponse.<PageDTO>builder()
//                .succeess(true)
//                .status(HttpStatus.OK)
//                .message("Customer retrieved successfully")
//                .payload(pageDTO)
//                .timestamp(Instant.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
//        CustomerResponse customerResponse = customerService.getCustomerResponseById(id);
//        ApiResponse<CustomerResponse> response = ApiResponse.<CustomerResponse>builder()
//                .succeess(true)
//                .status(HttpStatus.OK)
//                .message("Customer retrieved successfully")
//                .payload(customerResponse)
//                .timestamp(Instant.now())
//                .build();
//        return ResponseEntity.ok(response);
//    }
//    @PostMapping
//    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {
//        CustomerResponse response = customerService.createCustomer(request);
//        ApiResponse<CustomerResponse> apiResponse = ApiResponse.<CustomerResponse>builder()
//                .succeess(true)
//                .status(HttpStatus.CREATED)
//                .message("Customer created successfully")
//                .payload(response)
//                .timestamp(Instant.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
//    }
//    @PutMapping(path = "/{id}")
//    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
//            @PathVariable Long id,
//            @Valid @RequestBody CustomerRequest request) {
//        CustomerResponse response = customerService.updateCustomer(id,request);
//        ApiResponse<CustomerResponse> apiResponse =ApiResponse.<CustomerResponse>builder()
//                .succeess(true)
//                .status(HttpStatus.OK)
//                .message("Customer updated successfully")
//                .payload(response)
//                .timestamp(Instant.now())
//                .build();
//        return ResponseEntity.ok(apiResponse);
//    }
//    @PostMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>>deleteCustomer(@PathVariable Long id){
//        customerService.deleteCustomer(id);
//        ApiResponse<Void> response= ApiResponse.<Void>builder()
//                .succeess(true)
//                .status(HttpStatus.OK)
//                .message("Customer delete successful")
//                .timestamp(Instant.now())
//                .build();
//        return ResponseEntity.ok(response);
//
//    }
//
//}
