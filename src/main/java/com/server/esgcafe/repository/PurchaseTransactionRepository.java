package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.PurchaseTransaction;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.enum_class.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long> {

    Optional<PurchaseTransaction> findByPurchaseToken(String purchaseToken);
    boolean existsByPurchaseToken(String purchaseToken);

    List<PurchaseTransaction> findByUserOrderByRequestedAtDesc(User user);

    List<PurchaseTransaction> findByUserAndPurchaseStatusOrderByRequestedAtDesc(
            User user,
            PurchaseStatus purchaseStatus
    );

}
