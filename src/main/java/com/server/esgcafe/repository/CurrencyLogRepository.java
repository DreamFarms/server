package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.CurrencyLog;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.enum_class.CurrencyChangeReason;
import com.server.esgcafe.domain.enum_class.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyLogRepository extends JpaRepository<CurrencyLog, Long> {

    List<CurrencyLog> findByUserOrderByCreatedAtDesc(User user);

    List<CurrencyLog> findByUserAndCurrencyTypeOrderByCreatedAtDesc(
            User user,
            CurrencyType currencyType
    );

    List<CurrencyLog> findByUserAndReasonOrderByCreatedAtDesc(
            User user,
            CurrencyChangeReason reason
    );

}
