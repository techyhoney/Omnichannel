package com.kpmg.omnichannel.controller;

import com.kpmg.omnichannel.dto.ApiResponse;
import com.kpmg.omnichannel.dto.report.*;
import com.kpmg.omnichannel.model.TransactionStatus;
import com.kpmg.omnichannel.service.TransactionReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Transaction Reports & Analytics", description = "APIs for transaction reporting and analytics")
public class TransactionReportController {

    private final TransactionReportService reportService;

    // ==================== TRANSACTION SUMMARY ====================

    @GetMapping("/summary")
    @Operation(summary = "Get overall transaction summary", 
               description = "Returns summary of all transactions including counts, amounts, and success/failure rates")
    public ResponseEntity<ApiResponse<TransactionSummaryDTO>> getTransactionSummary() {
        TransactionSummaryDTO summary = reportService.getTransactionSummary();
        return ResponseEntity.ok(ApiResponse.success("Transaction summary retrieved successfully", summary));
    }

    // ==================== TRANSACTION HISTORY ====================

    @GetMapping("/history")
    @Operation(summary = "Get transaction history with filters", 
               description = "Returns paginated transaction history with optional filters")
    public ResponseEntity<ApiResponse<PaginatedTransactionHistoryDTO>> getTransactionHistory(
            @Parameter(description = "User ID to filter by") @RequestParam(required = false) UUID userId,
            @Parameter(description = "Merchant ID to filter by") @RequestParam(required = false) UUID merchantId,
            @Parameter(description = "Payment Type ID to filter by") @RequestParam(required = false) UUID paymentTypeId,
            @Parameter(description = "Transaction status to filter by") @RequestParam(required = false) TransactionStatus status,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Minimum amount") @RequestParam(required = false) BigDecimal minAmount,
            @Parameter(description = "Maximum amount") @RequestParam(required = false) BigDecimal maxAmount,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") String sortDirection) {

        TransactionHistoryRequest request = TransactionHistoryRequest.builder()
                .userId(userId)
                .merchantId(merchantId)
                .paymentTypeId(paymentTypeId)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PaginatedTransactionHistoryDTO history = reportService.getTransactionHistory(request);
        return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved successfully", history));
    }

    @GetMapping("/history/all")
    @Operation(summary = "Get all transaction history", 
               description = "Returns all transactions without pagination (use with caution for large datasets)")
    public ResponseEntity<ApiResponse<List<TransactionHistoryDTO>>> getAllTransactionHistory() {
        List<TransactionHistoryDTO> history = reportService.getAllTransactionHistory();
        return ResponseEntity.ok(ApiResponse.success("All transaction history retrieved successfully", history));
    }

    // ==================== DAILY REPORTS ====================

    @GetMapping("/daily")
    @Operation(summary = "Get daily report for a specific date", 
               description = "Returns transaction report for a specific date")
    public ResponseEntity<ApiResponse<DailyReportDTO>> getDailyReport(
            @Parameter(description = "Date for the report (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyReportDTO report = reportService.getDailyReport(date);
        return ResponseEntity.ok(ApiResponse.success("Daily report retrieved successfully", report));
    }

    @GetMapping("/daily/today")
    @Operation(summary = "Get today's report", 
               description = "Returns transaction report for today")
    public ResponseEntity<ApiResponse<DailyReportDTO>> getTodayReport() {
        DailyReportDTO report = reportService.getDailyReport(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.success("Today's report retrieved successfully", report));
    }

    @GetMapping("/daily/range")
    @Operation(summary = "Get daily reports for a date range", 
               description = "Returns daily transaction reports for a specified date range")
    public ResponseEntity<ApiResponse<List<DailyReportDTO>>> getDailyReportsRange(
            @Parameter(description = "Start date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyReportDTO> reports = reportService.getDailyReports(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Daily reports retrieved successfully", reports));
    }

    @GetMapping("/daily/last7days")
    @Operation(summary = "Get last 7 days report", 
               description = "Returns daily transaction reports for the last 7 days")
    public ResponseEntity<ApiResponse<List<DailyReportDTO>>> getLast7DaysReport() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        List<DailyReportDTO> reports = reportService.getDailyReports(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Last 7 days report retrieved successfully", reports));
    }

    @GetMapping("/daily/last30days")
    @Operation(summary = "Get last 30 days report", 
               description = "Returns daily transaction reports for the last 30 days")
    public ResponseEntity<ApiResponse<List<DailyReportDTO>>> getLast30DaysReport() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        List<DailyReportDTO> reports = reportService.getDailyReports(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Last 30 days report retrieved successfully", reports));
    }

    // ==================== MONTHLY REPORTS ====================

    @GetMapping("/monthly")
    @Operation(summary = "Get monthly report", 
               description = "Returns transaction report for a specific month and year")
    public ResponseEntity<ApiResponse<MonthlyReportDTO>> getMonthlyReport(
            @Parameter(description = "Year") @RequestParam Integer year,
            @Parameter(description = "Month (1-12)") @RequestParam Integer month) {
        MonthlyReportDTO report = reportService.getMonthlyReport(year, month);
        return ResponseEntity.ok(ApiResponse.success("Monthly report retrieved successfully", report));
    }

    @GetMapping("/monthly/current")
    @Operation(summary = "Get current month report", 
               description = "Returns transaction report for the current month")
    public ResponseEntity<ApiResponse<MonthlyReportDTO>> getCurrentMonthReport() {
        LocalDate now = LocalDate.now();
        MonthlyReportDTO report = reportService.getMonthlyReport(now.getYear(), now.getMonthValue());
        return ResponseEntity.ok(ApiResponse.success("Current month report retrieved successfully", report));
    }

    @GetMapping("/monthly/year/{year}")
    @Operation(summary = "Get all monthly reports for a year", 
               description = "Returns monthly transaction reports for all months of a specified year")
    public ResponseEntity<ApiResponse<List<MonthlyReportDTO>>> getMonthlyReportsForYear(
            @Parameter(description = "Year") @PathVariable Integer year) {
        List<MonthlyReportDTO> reports = reportService.getMonthlyReportsForYear(year);
        return ResponseEntity.ok(ApiResponse.success("Monthly reports for year " + year + " retrieved successfully", reports));
    }

    // ==================== YEARLY REPORTS ====================

    @GetMapping("/yearly/{year}")
    @Operation(summary = "Get yearly report with monthly breakdown", 
               description = "Returns comprehensive transaction report for a year with monthly breakdown")
    public ResponseEntity<ApiResponse<YearlyReportDTO>> getYearlyReport(
            @Parameter(description = "Year") @PathVariable Integer year) {
        YearlyReportDTO report = reportService.getYearlyReport(year);
        return ResponseEntity.ok(ApiResponse.success("Yearly report retrieved successfully", report));
    }

    @GetMapping("/yearly/current")
    @Operation(summary = "Get current year report", 
               description = "Returns transaction report for the current year with monthly breakdown")
    public ResponseEntity<ApiResponse<YearlyReportDTO>> getCurrentYearReport() {
        YearlyReportDTO report = reportService.getYearlyReport(LocalDate.now().getYear());
        return ResponseEntity.ok(ApiResponse.success("Current year report retrieved successfully", report));
    }

    // ==================== USER TRANSACTION REPORTS ====================

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user transaction report", 
               description = "Returns comprehensive transaction report for a specific user")
    public ResponseEntity<ApiResponse<UserTransactionReportDTO>> getUserTransactionReport(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        UserTransactionReportDTO report = reportService.getUserTransactionReport(userId);
        return ResponseEntity.ok(ApiResponse.success("User transaction report retrieved successfully", report));
    }

    @GetMapping("/user/{userId}/range")
    @Operation(summary = "Get user transaction report for date range", 
               description = "Returns transaction report for a user within a specific date range")
    public ResponseEntity<ApiResponse<UserTransactionReportDTO>> getUserTransactionReportByDateRange(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Start date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        UserTransactionReportDTO report = reportService.getUserTransactionReportByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("User transaction report retrieved successfully", report));
    }

    // ==================== PAYMENT TYPE ANALYTICS ====================

    @GetMapping("/analytics/payment-types")
    @Operation(summary = "Get payment type analytics", 
               description = "Returns analytics for all payment types including usage stats and success rates")
    public ResponseEntity<ApiResponse<List<PaymentTypeAnalyticsDTO>>> getPaymentTypeAnalytics() {
        List<PaymentTypeAnalyticsDTO> analytics = reportService.getPaymentTypeAnalytics();
        return ResponseEntity.ok(ApiResponse.success("Payment type analytics retrieved successfully", analytics));
    }

    @GetMapping("/analytics/payment-types/{paymentTypeId}")
    @Operation(summary = "Get analytics for a specific payment type", 
               description = "Returns detailed analytics for a specific payment type")
    public ResponseEntity<ApiResponse<PaymentTypeAnalyticsDTO>> getPaymentTypeAnalyticsById(
            @Parameter(description = "Payment Type ID") @PathVariable UUID paymentTypeId) {
        PaymentTypeAnalyticsDTO analytics = reportService.getPaymentTypeAnalyticsById(paymentTypeId);
        return ResponseEntity.ok(ApiResponse.success("Payment type analytics retrieved successfully", analytics));
    }

    // ==================== TRANSACTION METRICS ====================

    @GetMapping("/metrics")
    @Operation(summary = "Get overall transaction metrics", 
               description = "Returns comprehensive transaction metrics including success/failure rates and amount statistics")
    public ResponseEntity<ApiResponse<TransactionMetricsDTO>> getTransactionMetrics() {
        TransactionMetricsDTO metrics = reportService.getTransactionMetrics();
        return ResponseEntity.ok(ApiResponse.success("Transaction metrics retrieved successfully", metrics));
    }

    @GetMapping("/metrics/range")
    @Operation(summary = "Get transaction metrics for date range", 
               description = "Returns transaction metrics for a specific date range")
    public ResponseEntity<ApiResponse<TransactionMetricsDTO>> getTransactionMetricsByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        TransactionMetricsDTO metrics = reportService.getTransactionMetricsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Transaction metrics retrieved successfully", metrics));
    }
}

