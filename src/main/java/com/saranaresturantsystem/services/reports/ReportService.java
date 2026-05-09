package com.saranaresturantsystem.services.reports;

import com.saranaresturantsystem.dto.response.reports.SalesReportResponse;
import java.time.LocalDateTime;

public interface ReportService {
    SalesReportResponse getSalesReport(LocalDateTime start, LocalDateTime end, Integer storeId);
}