package com.saranaresturantsystem.services.impl.reports;

import com.saranaresturantsystem.dto.response.reports.SalesReportResponse;
import com.saranaresturantsystem.entities.Sale;
import com.saranaresturantsystem.mappers.SaleMapper;
import com.saranaresturantsystem.repositories.SaleRepository;
import com.saranaresturantsystem.services.reports.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    @Override
    public SalesReportResponse getSalesReport(LocalDateTime start, LocalDateTime end, Integer storeId) {
        // ប្រើ Method ដែលយើងបានបង្កើតក្នុង Repository មុននេះ
        List<Sale> sales = saleRepository.getSalesReport(start, end, storeId);

        BigDecimal totalAmount = sales.stream()
                .map(s -> s.getGrandTotal() != null ? s.getGrandTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaid = sales.stream()
                .map(s -> s.getPaid() != null ? s.getPaid() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return SalesReportResponse.builder()
                .sales(sales.stream().map(saleMapper::toResponse).toList())
                .totalSalesAmount(totalAmount)
                .totalPaidAmount(totalPaid)
                .totalDueAmount(totalAmount.subtract(totalPaid))
                .totalInvoices(sales.size())
                .build();
    }
}