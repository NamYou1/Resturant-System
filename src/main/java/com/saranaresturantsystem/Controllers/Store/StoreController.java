package com.saranaresturantsystem.Controllers.Store;

import com.saranaresturantsystem.DTO.Request.StoreRequest;
import com.saranaresturantsystem.DTO.Response.StoreResponse;
import com.saranaresturantsystem.Services.Interfaces.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse create(@Valid @RequestBody StoreRequest request) {
        return storeService.create(request);
    }

    @GetMapping
    public List<StoreResponse> getAll() {
        return storeService.findAll();
    }

    @GetMapping("/{id}")
    public StoreResponse getOne(@PathVariable Long id) {
        return storeService.findById(id);
    }

    @PutMapping("/{id}")
    public StoreResponse update(@PathVariable Long id, @Valid @RequestBody StoreRequest request) {
        return storeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storeService.delete(id);
    }
}