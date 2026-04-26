package com.saranaresturantsystem.dto.response.reports;

import com.saranaresturantsystem.dto.response.SaleResponse;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SalesReportResponse {
    private List<SaleResponse> sales;
    private BigDecimal totalSalesAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal totalDueAmount;
    private Integer totalInvoices;
}