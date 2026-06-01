package com.saranaresturantsystem.dto.response.reports;

import com.saranaresturantsystem.dto.response.sales.SaleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesReportResponse {
    private List<SaleResponse> sales;
    private BigDecimal totalSalesAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal totalDueAmount;
    private Integer totalInvoices;
}
