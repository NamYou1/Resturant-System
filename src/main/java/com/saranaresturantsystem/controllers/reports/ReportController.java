package com.saranaresturantsystem.controllers.reports;

import com.saranaresturantsystem.dto.response.reports.SalesReportResponse;
import com.saranaresturantsystem.services.reports.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<SalesReportResponse> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer storeId) {

        return ResponseEntity.ok(reportService.getSalesReport(start, end, storeId));
    }
}