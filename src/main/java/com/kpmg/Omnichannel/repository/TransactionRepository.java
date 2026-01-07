package com.kpmg.omnichannel.repository;

import com.kpmg.omnichannel.model.Transaction;
import com.kpmg.omnichannel.model.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    
    // Find by user
    List<Transaction> findByUserUserId(UUID userId);
    Page<Transaction> findByUserUserId(UUID userId, Pageable pageable);
    
    // Find by merchant
    List<Transaction> findByMerchantUserId(UUID merchantId);
    Page<Transaction> findByMerchantUserId(UUID merchantId, Pageable pageable);
    
    // Find by status
    List<Transaction> findByStatus(TransactionStatus status);
    Long countByStatus(TransactionStatus status);
    
    // Find by payment type
    List<Transaction> findByPaymentTypePaymentTypeId(UUID paymentTypeId);
    Long countByPaymentTypePaymentTypeId(UUID paymentTypeId);
    
    // Find by date range
    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Count queries for analytics
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.userId = :userId")
    Long countByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.userId = :userId AND t.status = :status")
    Long countByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") TransactionStatus status);
    
    // Sum queries for amount analytics
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t")
    BigDecimal sumTotalAmount();
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") TransactionStatus status);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.userId = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.userId = :userId AND t.status = :status")
    BigDecimal sumAmountByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") TransactionStatus status);
    
    // Average, Max, Min amount queries
    @Query("SELECT COALESCE(AVG(t.amount), 0) FROM Transaction t")
    BigDecimal averageAmount();
    
    @Query("SELECT COALESCE(MAX(t.amount), 0) FROM Transaction t")
    BigDecimal maxAmount();
    
    @Query("SELECT COALESCE(MIN(t.amount), 0) FROM Transaction t WHERE t.amount > 0")
    BigDecimal minAmount();
    
    // Date range queries for reports
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate AND t.status = :status")
    Long countByDateRangeAndStatus(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate, 
                                    @Param("status") TransactionStatus status);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate AND t.status = :status")
    BigDecimal sumAmountByDateRangeAndStatus(@Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate, 
                                              @Param("status") TransactionStatus status);
    
    // Payment type analytics
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.paymentType.paymentTypeId = :paymentTypeId AND t.status = :status")
    Long countByPaymentTypeAndStatus(@Param("paymentTypeId") UUID paymentTypeId, @Param("status") TransactionStatus status);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.paymentType.paymentTypeId = :paymentTypeId")
    BigDecimal sumAmountByPaymentType(@Param("paymentTypeId") UUID paymentTypeId);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.paymentType.paymentTypeId = :paymentTypeId AND t.status = :status")
    BigDecimal sumAmountByPaymentTypeAndStatus(@Param("paymentTypeId") UUID paymentTypeId, @Param("status") TransactionStatus status);
    
    // User specific date range queries
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.userId = :userId AND t.createdAt BETWEEN :startDate AND :endDate")
    Long countByUserIdAndDateRange(@Param("userId") UUID userId, 
                                    @Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.userId = :userId AND t.createdAt BETWEEN :startDate AND :endDate AND t.status = :status")
    Long countByUserIdAndDateRangeAndStatus(@Param("userId") UUID userId, 
                                             @Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate,
                                             @Param("status") TransactionStatus status);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.userId = :userId AND t.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByUserIdAndDateRange(@Param("userId") UUID userId, 
                                              @Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
    
    // Get recent transactions for user
    @Query("SELECT t FROM Transaction t WHERE t.user.userId = :userId ORDER BY t.createdAt DESC")
    List<Transaction> findRecentByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    // Get transactions by multiple filters
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:userId IS NULL OR t.user.userId = :userId) AND " +
           "(:merchantId IS NULL OR t.merchant.userId = :merchantId) AND " +
           "(:paymentTypeId IS NULL OR t.paymentType.paymentTypeId = :paymentTypeId) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:startDate IS NULL OR t.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR t.createdAt <= :endDate) AND " +
           "(:minAmount IS NULL OR t.amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR t.amount <= :maxAmount)")
    Page<Transaction> findByFilters(@Param("userId") UUID userId,
                                    @Param("merchantId") UUID merchantId,
                                    @Param("paymentTypeId") UUID paymentTypeId,
                                    @Param("status") TransactionStatus status,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    @Param("minAmount") BigDecimal minAmount,
                                    @Param("maxAmount") BigDecimal maxAmount,
                                    Pageable pageable);
}
