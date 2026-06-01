package com.saranaresturantsystem.dto.request.inventory;

import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.enums.TransferStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private LocalDateTime date;
    @NotNull(message = "From store is required")
    private Long fromStoreId;
    @NotNull(message = "To store is required")
    private Long toStoreId;
    @Size(max = 250)
    private String note;
    private TransferStatus status;
    @Size(max = 255)
    private String attachment;
    private StatusType isActive = StatusType.ACTIVE;
    @NotEmpty(message = "Transfer must have at least one item")
    @Valid
    private List<TransferItemRequest> items;
}