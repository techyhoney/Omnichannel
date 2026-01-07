package com.kpmg.omnichannel.service;

import com.kpmg.omnichannel.dto.report.*;
import com.kpmg.omnichannel.exception.ResourceNotFoundException;
import com.kpmg.omnichannel.model.PaymentType;
import com.kpmg.omnichannel.model.Transaction;
import com.kpmg.omnichannel.model.TransactionStatus;
import com.kpmg.omnichannel.model.User;
import com.kpmg.omnichannel.repository.PaymentTypeRepository;
import com.kpmg.omnichannel.repository.TransactionRepository;
import com.kpmg.omnichannel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionReportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    // ==================== TRANSACTION HISTORY ====================

    /**
     * Get transaction history with filters and pagination
     */
    public PaginatedTransactionHistoryDTO getTransactionHistory(TransactionHistoryRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "createdAt";
        String sortDirection = request.getSortDirection() != null ? request.getSortDirection() : "DESC";

        Sort sort = sortDirection.equalsIgnoreCase("ASC") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDateTime startDateTime = request.getStartDate() != null 
                ? request.getStartDate().atStartOfDay() 
                : null;
        LocalDateTime endDateTime = request.getEndDate() != null 
                ? request.getEndDate().atTime(LocalTime.MAX) 
                : null;

        Page<Transaction> transactionPage = transactionRepository.findByFilters(
                request.getUserId(),
                request.getMerchantId(),
                request.getPaymentTypeId(),
                request.getStatus(),
                startDateTime,
                endDateTime,
                request.getMinAmount(),
                request.getMaxAmount(),
                pageable
        );

        List<TransactionHistoryDTO> transactions = transactionPage.getContent().stream()
                .map(this::mapToTransactionHistoryDTO)
                .collect(Collectors.toList());

        return PaginatedTransactionHistoryDTO.builder()
                .transactions(transactions)
                .currentPage(transactionPage.getNumber())
                .totalPages(transactionPage.getTotalPages())
                .totalElements(transactionPage.getTotalElements())
                .pageSize(transactionPage.getSize())
                .hasNext(transactionPage.hasNext())
                .hasPrevious(transactionPage.hasPrevious())
                .build();
    }

    /**
     * Get all transaction history (no pagination)
     */
    public List<TransactionHistoryDTO> getAllTransactionHistory() {
        return transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(this::mapToTransactionHistoryDTO)
                .collect(Collectors.toList());
    }

    // ==================== DAILY REPORTS ====================

    /**
     * Get daily report for a specific date
     */
    public DailyReportDTO getDailyReport(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Long totalTransactions = transactionRepository.countByDateRange(startOfDay, endOfDay);
        Long successfulTransactions = transactionRepository.countByDateRangeAndStatus(startOfDay, endOfDay, TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByDateRangeAndStatus(startOfDay, endOfDay, TransactionStatus.FAILED);
        BigDecimal totalAmount = transactionRepository.sumAmountByDateRange(startOfDay, endOfDay);
        BigDecimal successfulAmount = transactionRepository.sumAmountByDateRangeAndStatus(startOfDay, endOfDay, TransactionStatus.SUCCESS);

        Double successRate = totalTransactions > 0 
                ? (successfulTransactions * 100.0) / totalTransactions 
                : 0.0;

        return DailyReportDTO.builder()
                .date(date)
                .totalTransactions(totalTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .build();
    }

    /**
     * Get daily reports for a date range
     */
    public List<DailyReportDTO> getDailyReports(LocalDate startDate, LocalDate endDate) {
        List<DailyReportDTO> reports = new ArrayList<>();
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            reports.add(getDailyReport(currentDate));
            currentDate = currentDate.plusDays(1);
        }
        
        return reports;
    }

    // ==================== MONTHLY REPORTS ====================

    /**
     * Get monthly report for a specific month and year
     */
    public MonthlyReportDTO getMonthlyReport(int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(LocalTime.MAX);

        Long totalTransactions = transactionRepository.countByDateRange(startDateTime, endDateTime);
        Long successfulTransactions = transactionRepository.countByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.FAILED);
        BigDecimal totalAmount = transactionRepository.sumAmountByDateRange(startDateTime, endDateTime);
        BigDecimal successfulAmount = transactionRepository.sumAmountByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.SUCCESS);

        Double successRate = totalTransactions > 0 
                ? (successfulTransactions * 100.0) / totalTransactions 
                : 0.0;
        
        BigDecimal avgAmount = totalTransactions > 0 
                ? totalAmount.divide(BigDecimal.valueOf(totalTransactions), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        String monthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return MonthlyReportDTO.builder()
                .year(year)
                .month(month)
                .monthName(monthName)
                .totalTransactions(totalTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .averageTransactionAmount(avgAmount)
                .build();
    }

    /**
     * Get monthly reports for a year
     */
    public List<MonthlyReportDTO> getMonthlyReportsForYear(int year) {
        List<MonthlyReportDTO> reports = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            reports.add(getMonthlyReport(year, month));
        }
        return reports;
    }

    // ==================== YEARLY REPORTS ====================

    /**
     * Get yearly report with monthly breakdown
     */
    public YearlyReportDTO getYearlyReport(int year) {
        LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);

        Long totalTransactions = transactionRepository.countByDateRange(startOfYear, endOfYear);
        Long successfulTransactions = transactionRepository.countByDateRangeAndStatus(startOfYear, endOfYear, TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByDateRangeAndStatus(startOfYear, endOfYear, TransactionStatus.FAILED);
        BigDecimal totalAmount = transactionRepository.sumAmountByDateRange(startOfYear, endOfYear);
        BigDecimal successfulAmount = transactionRepository.sumAmountByDateRangeAndStatus(startOfYear, endOfYear, TransactionStatus.SUCCESS);

        Double successRate = totalTransactions > 0 
                ? (successfulTransactions * 100.0) / totalTransactions 
                : 0.0;
        
        BigDecimal avgAmount = totalTransactions > 0 
                ? totalAmount.divide(BigDecimal.valueOf(totalTransactions), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        List<MonthlyReportDTO> monthlyBreakdown = getMonthlyReportsForYear(year);

        return YearlyReportDTO.builder()
                .year(year)
                .totalTransactions(totalTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .averageTransactionAmount(avgAmount)
                .monthlyBreakdown(monthlyBreakdown)
                .build();
    }

    // ==================== USER TRANSACTION REPORTS ====================

    /**
     * Get comprehensive transaction report for a user
     */
    public UserTransactionReportDTO getUserTransactionReport(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Long totalTransactions = transactionRepository.countByUserId(userId);
        Long successfulTransactions = transactionRepository.countByUserIdAndStatus(userId, TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByUserIdAndStatus(userId, TransactionStatus.FAILED);
        BigDecimal totalAmount = transactionRepository.sumAmountByUserId(userId);
        BigDecimal successfulAmount = transactionRepository.sumAmountByUserIdAndStatus(userId, TransactionStatus.SUCCESS);

        Double successRate = totalTransactions > 0 
                ? (successfulTransactions * 100.0) / totalTransactions 
                : 0.0;

        // Get payment type usage
        List<PaymentTypeUsageDTO> paymentTypeUsage = getPaymentTypeUsageForUser(userId, totalTransactions);

        // Get recent transactions
        List<Transaction> recentTransactions = transactionRepository.findRecentByUserId(userId, PageRequest.of(0, 10));
        List<TransactionHistoryDTO> recentTransactionDTOs = recentTransactions.stream()
                .map(this::mapToTransactionHistoryDTO)
                .collect(Collectors.toList());

        return UserTransactionReportDTO.builder()
                .userId(userId)
                .userName(user.getFirstName() + " " + user.getLastName())
                .userEmail(user.getEmail())
                .userType(user.getUserType().name())
                .totalTransactions(totalTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .paymentTypeUsage(paymentTypeUsage)
                .recentTransactions(recentTransactionDTOs)
                .build();
    }

    /**
     * Get user transaction report for a specific date range
     */
    public UserTransactionReportDTO getUserTransactionReportByDateRange(UUID userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Long totalTransactions = transactionRepository.countByUserIdAndDateRange(userId, startDateTime, endDateTime);
        Long successfulTransactions = transactionRepository.countByUserIdAndDateRangeAndStatus(userId, startDateTime, endDateTime, TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByUserIdAndDateRangeAndStatus(userId, startDateTime, endDateTime, TransactionStatus.FAILED);
        BigDecimal totalAmount = transactionRepository.sumAmountByUserIdAndDateRange(userId, startDateTime, endDateTime);

        Double successRate = totalTransactions > 0 
                ? (successfulTransactions * 100.0) / totalTransactions 
                : 0.0;

        return UserTransactionReportDTO.builder()
                .userId(userId)
                .userName(user.getFirstName() + " " + user.getLastName())
                .userEmail(user.getEmail())
                .userType(user.getUserType().name())
                .totalTransactions(totalTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .totalAmount(totalAmount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .build();
    }

    // ==================== PAYMENT TYPE ANALYTICS ====================

    /**
     * Get analytics for all payment types
     */
    public List<PaymentTypeAnalyticsDTO> getPaymentTypeAnalytics() {
        List<PaymentType> paymentTypes = paymentTypeRepository.findAll();
        Long totalTransactionsOverall = transactionRepository.count();

        return paymentTypes.stream()
                .map(pt -> buildPaymentTypeAnalytics(pt, totalTransactionsOverall))
                .sorted((a, b) -> Long.compare(b.getTotalTransactions(), a.getTotalTransactions()))
                .collect(Collectors.toList());
    }

    /**
     * Get analytics for a specific payment type
     */
    public PaymentTypeAnalyticsDTO getPaymentTypeAnalyticsById(UUID paymentTypeId) {
        PaymentType paymentType = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + paymentTypeId));

        Long totalTransactionsOverall = transactionRepository.count();
        return buildPaymentTypeAnalytics(paymentType, totalTransactionsOverall);
    }

    // ==================== TRANSACTION METRICS ====================

    /**
     * Get overall transaction metrics
     */
    public TransactionMetricsDTO getTransactionMetrics() {
        Long totalTransactions = transactionRepository.count();
        Long initiatedTransactions = transactionRepository.countByStatus(TransactionStatus.INITIATED);
        Long processingTransactions = transactionRepository.countByStatus(TransactionStatus.PROCESSING);
        Long successfulTransactions = transactionRepository.countByStatus(TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByStatus(TransactionStatus.FAILED);

        BigDecimal totalAmount = transactionRepository.sumTotalAmount();
        BigDecimal successfulAmount = transactionRepository.sumAmountByStatus(TransactionStatus.SUCCESS);
        BigDecimal failedAmount = transactionRepository.sumAmountByStatus(TransactionStatus.FAILED);
        BigDecimal avgAmount = transactionRepository.averageAmount();
        BigDecimal maxAmount = transactionRepository.maxAmount();
        BigDecimal minAmount = transactionRepository.minAmount();

        Double successRate = totalTransactions > 0 ? (successfulTransactions * 100.0) / totalTransactions : 0.0;
        Double failureRate = totalTransactions > 0 ? (failedTransactions * 100.0) / totalTransactions : 0.0;
        Double processingRate = totalTransactions > 0 ? (processingTransactions * 100.0) / totalTransactions : 0.0;

        // Status breakdown
        Map<String, Long> statusBreakdown = new LinkedHashMap<>();
        statusBreakdown.put("INITIATED", initiatedTransactions);
        statusBreakdown.put("PROCESSING", processingTransactions);
        statusBreakdown.put("SUCCESS", successfulTransactions);
        statusBreakdown.put("FAILED", failedTransactions);

        Map<String, BigDecimal> statusAmountBreakdown = new LinkedHashMap<>();
        statusAmountBreakdown.put("INITIATED", transactionRepository.sumAmountByStatus(TransactionStatus.INITIATED));
        statusAmountBreakdown.put("PROCESSING", transactionRepository.sumAmountByStatus(TransactionStatus.PROCESSING));
        statusAmountBreakdown.put("SUCCESS", successfulAmount);
        statusAmountBreakdown.put("FAILED", failedAmount);

        return TransactionMetricsDTO.builder()
                .totalTransactions(totalTransactions)
                .initiatedTransactions(initiatedTransactions)
                .processingTransactions(processingTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .failureRate(Math.round(failureRate * 100.0) / 100.0)
                .processingRate(Math.round(processingRate * 100.0) / 100.0)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .failedAmount(failedAmount)
                .averageTransactionAmount(avgAmount)
                .maxTransactionAmount(maxAmount)
                .minTransactionAmount(minAmount)
                .statusBreakdown(statusBreakdown)
                .statusAmountBreakdown(statusAmountBreakdown)
                .build();
    }

    /**
     * Get transaction metrics for a date range
     */
    public TransactionMetricsDTO getTransactionMetricsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Long totalTransactions = transactionRepository.countByDateRange(startDateTime, endDateTime);
        Long initiatedTransactions = transactionRepository.countByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.INITIATED);
        Long processingTransactions = transactionRepository.countByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.PROCESSING);
        Long successfulTransactions = transactionRepository.countByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.FAILED);

        BigDecimal totalAmount = transactionRepository.sumAmountByDateRange(startDateTime, endDateTime);
        BigDecimal successfulAmount = transactionRepository.sumAmountByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.SUCCESS);
        BigDecimal failedAmount = transactionRepository.sumAmountByDateRangeAndStatus(startDateTime, endDateTime, TransactionStatus.FAILED);

        Double successRate = totalTransactions > 0 ? (successfulTransactions * 100.0) / totalTransactions : 0.0;
        Double failureRate = totalTransactions > 0 ? (failedTransactions * 100.0) / totalTransactions : 0.0;
        Double processingRate = totalTransactions > 0 ? (processingTransactions * 100.0) / totalTransactions : 0.0;

        Map<String, Long> statusBreakdown = new LinkedHashMap<>();
        statusBreakdown.put("INITIATED", initiatedTransactions);
        statusBreakdown.put("PROCESSING", processingTransactions);
        statusBreakdown.put("SUCCESS", successfulTransactions);
        statusBreakdown.put("FAILED", failedTransactions);

        return TransactionMetricsDTO.builder()
                .totalTransactions(totalTransactions)
                .initiatedTransactions(initiatedTransactions)
                .processingTransactions(processingTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .failureRate(Math.round(failureRate * 100.0) / 100.0)
                .processingRate(Math.round(processingRate * 100.0) / 100.0)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .failedAmount(failedAmount)
                .statusBreakdown(statusBreakdown)
                .build();
    }

    // ==================== SUMMARY ====================

    /**
     * Get overall transaction summary
     */
    public TransactionSummaryDTO getTransactionSummary() {
        Long totalTransactions = transactionRepository.count();
        Long successfulTransactions = transactionRepository.countByStatus(TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByStatus(TransactionStatus.FAILED);
        Long pendingTransactions = transactionRepository.countByStatus(TransactionStatus.INITIATED) 
                + transactionRepository.countByStatus(TransactionStatus.PROCESSING);

        BigDecimal totalAmount = transactionRepository.sumTotalAmount();
        BigDecimal successfulAmount = transactionRepository.sumAmountByStatus(TransactionStatus.SUCCESS);
        BigDecimal failedAmount = transactionRepository.sumAmountByStatus(TransactionStatus.FAILED);
        BigDecimal avgAmount = transactionRepository.averageAmount();

        Double successRate = totalTransactions > 0 ? (successfulTransactions * 100.0) / totalTransactions : 0.0;
        Double failureRate = totalTransactions > 0 ? (failedTransactions * 100.0) / totalTransactions : 0.0;

        return TransactionSummaryDTO.builder()
                .totalTransactions(totalTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .pendingTransactions(pendingTransactions)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .failedAmount(failedAmount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .failureRate(Math.round(failureRate * 100.0) / 100.0)
                .averageTransactionAmount(avgAmount)
                .build();
    }

    // ==================== HELPER METHODS ====================

    private TransactionHistoryDTO mapToTransactionHistoryDTO(Transaction transaction) {
        return TransactionHistoryDTO.builder()
                .transactionId(transaction.getTransactionId())
                .userName(transaction.getUser().getFirstName() + " " + transaction.getUser().getLastName())
                .merchantName(transaction.getMerchant().getFirstName() + " " + transaction.getMerchant().getLastName())
                .paymentTypeName(transaction.getPaymentType().getName())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .transactionDate(transaction.getCreatedAt())
                .build();
    }

    private PaymentTypeAnalyticsDTO buildPaymentTypeAnalytics(PaymentType paymentType, Long totalTransactionsOverall) {
        UUID paymentTypeId = paymentType.getPaymentTypeId();
        
        Long totalTransactions = transactionRepository.countByPaymentTypePaymentTypeId(paymentTypeId);
        Long successfulTransactions = transactionRepository.countByPaymentTypeAndStatus(paymentTypeId, TransactionStatus.SUCCESS);
        Long failedTransactions = transactionRepository.countByPaymentTypeAndStatus(paymentTypeId, TransactionStatus.FAILED);
        BigDecimal totalAmount = transactionRepository.sumAmountByPaymentType(paymentTypeId);
        BigDecimal successfulAmount = transactionRepository.sumAmountByPaymentTypeAndStatus(paymentTypeId, TransactionStatus.SUCCESS);

        Double successRate = totalTransactions > 0 ? (successfulTransactions * 100.0) / totalTransactions : 0.0;
        Double usagePercentage = totalTransactionsOverall > 0 ? (totalTransactions * 100.0) / totalTransactionsOverall : 0.0;
        BigDecimal avgAmount = totalTransactions > 0 
                ? totalAmount.divide(BigDecimal.valueOf(totalTransactions), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        return PaymentTypeAnalyticsDTO.builder()
                .paymentTypeId(paymentTypeId)
                .paymentTypeName(paymentType.getName())
                .totalTransactions(totalTransactions)
                .successfulTransactions(successfulTransactions)
                .failedTransactions(failedTransactions)
                .totalAmount(totalAmount)
                .successfulAmount(successfulAmount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .usagePercentage(Math.round(usagePercentage * 100.0) / 100.0)
                .averageTransactionAmount(avgAmount)
                .build();
    }

    private List<PaymentTypeUsageDTO> getPaymentTypeUsageForUser(UUID userId, Long totalUserTransactions) {
        List<PaymentType> paymentTypes = paymentTypeRepository.findAll();
        List<Transaction> userTransactions = transactionRepository.findByUserUserId(userId);
        
        Map<UUID, Long> paymentTypeCount = userTransactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getPaymentType().getPaymentTypeId(),
                        Collectors.counting()
                ));
        
        Map<UUID, BigDecimal> paymentTypeAmount = userTransactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getPaymentType().getPaymentTypeId(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        return paymentTypes.stream()
                .filter(pt -> paymentTypeCount.containsKey(pt.getPaymentTypeId()))
                .map(pt -> {
                    Long count = paymentTypeCount.getOrDefault(pt.getPaymentTypeId(), 0L);
                    BigDecimal amount = paymentTypeAmount.getOrDefault(pt.getPaymentTypeId(), BigDecimal.ZERO);
                    Double usagePercentage = totalUserTransactions > 0 
                            ? (count * 100.0) / totalUserTransactions 
                            : 0.0;
                    
                    return PaymentTypeUsageDTO.builder()
                            .paymentTypeName(pt.getName())
                            .transactionCount(count)
                            .totalAmount(amount)
                            .usagePercentage(Math.round(usagePercentage * 100.0) / 100.0)
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getTransactionCount(), a.getTransactionCount()))
                .collect(Collectors.toList());
    }
}

