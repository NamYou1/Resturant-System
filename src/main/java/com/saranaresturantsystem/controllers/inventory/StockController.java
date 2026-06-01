package com.saranaresturantsystem.controllers.inventory;

import com.saranaresturantsystem.common.Message;
import com.saranaresturantsystem.common.ResponseFactory;
import com.saranaresturantsystem.dto.PageDTO;
import com.saranaresturantsystem.dto.response.inventory.StockResponse;
import com.saranaresturantsystem.dto.response.ApiResponse;
import com.saranaresturantsystem.entities.users.User;
import com.saranaresturantsystem.repositories.users.UserRepository;
import com.saranaresturantsystem.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stocks")
@Tag(name = "Stock Management", description = "Endpoints for viewing and tracking current stock levels")
public class StockController {

    private final StockService stockService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('supAdmin') or hasAuthority('stock:read')")
    @Operation(summary = "Get stock records", description = "Returns a paginated list of stocks. Super admin sees all, store admin sees own store.")
    public ResponseEntity<ApiResponse<PageDTO>> getAll(@RequestParam Map<String, String> params) {
        User currentUser = getCurrentUser();
        boolean isSuperAdmin = currentUser.getRoles().stream().anyMatch(r -> "ROLE_SUPER_ADMIN".equals(r.getCode()));
        Page<StockResponse> responses;
        if (isSuperAdmin) {
            responses = stockService.getAll(params);
        } else {
            if (currentUser.getStore() == null) {
                throw new AccessDeniedException("User is not associated with any store.");
            }
            responses = stockService.getByStore(currentUser.getStore().getId(), params);
        }
        return ResponseFactory.ok(responses, "Stock");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('supAdmin') or hasAuthority('stock:read')")
    @Operation(summary = "Get stock by ID", description = "Retrieves details of a specific stock record by its unique ID.")
    public ResponseEntity<ApiResponse<StockResponse>> getById(@PathVariable Long id) {
        StockResponse stockResponse = stockService.getById(id);
        return ResponseFactory.ok(stockResponse, Message.getById("Stock", id));
    }

    private User getCurrentUser() {
        String usernameOrEmail = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("Authenticated user not found"));
    }
}
