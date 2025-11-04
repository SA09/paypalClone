package com.paypal.wallet_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypal.wallet_service.entity.WalletHold;
import java.util.List;
import java.time.Instant;


@Repository
public interface WalletHoldRepository extends JpaRepository<WalletHold, Long> {
	 List<WalletHold> findByStatusAndExpiresAt(String status, Instant expiresAt);

}
