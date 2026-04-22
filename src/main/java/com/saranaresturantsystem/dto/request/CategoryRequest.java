package com.saranaresturantsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Category code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
//    @Uns
    private String code;

    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    @Size(max = 50, message = "Display must not exceed 50 characters")
    private String display;
    @Schema(type = "string", format = "binary", nullable = true)
    private MultipartFile imagePath;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromTime;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate toTime;



}
