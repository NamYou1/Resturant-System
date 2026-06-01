package com.saranaresturantsystem.controllers.inventory;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.request.inventory.TransferRequest;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.dto.response.inventory.TransferResponse;
import com.saranaresturantsystem.services.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfer Management", description = "APIs for managing inventory transfers between stores")
public class TransferController {

    private final TransferService transferService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        return ResponseFactory.ok(transferService.getAll(params), "Transfer");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransferResponse>> create(@Valid @RequestBody TransferRequest request) {
        return ResponseFactory.created(transferService.create(request), "Transfer");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransferResponse>> getById(@PathVariable Long id) {
        return ResponseFactory.ok(transferService.getById(id), Message.getById("Transfer", id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransferResponse>> update(
            @PathVariable Long id,
            @RequestBody TransferRequest request,
            Principal principal) {
        String updatedBy = principal != null ? principal.getName() : "system";
        return ResponseFactory.ok(transferService.update(id, request, updatedBy), Message.updated("Transfer", id));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<TransferResponse>> approve(@PathVariable Long id, Principal principal) {
        String approvedBy = principal != null ? principal.getName() : "system";
        return ResponseFactory.ok(transferService.approve(id, approvedBy), "Transfer " + id + " approved successfully");
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TransferResponse>> complete(@PathVariable Long id, Principal principal) {
        String completedBy = principal != null ? principal.getName() : "system";
        return ResponseFactory.ok(transferService.complete(id, completedBy), "Transfer " + id + " completed successfully");
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<TransferResponse>> cancel(@PathVariable Long id, Principal principal) {
        String cancelledBy = principal != null ? principal.getName() : "system";
        return ResponseFactory.ok(transferService.cancel(id, cancelledBy), "Transfer " + id + " cancelled successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Principal principal) {
        String deletedBy = principal != null ? principal.getName() : "system";
        transferService.delete(id, deletedBy);
        return ResponseFactory.deleted("Transfer", id);
    }
}
