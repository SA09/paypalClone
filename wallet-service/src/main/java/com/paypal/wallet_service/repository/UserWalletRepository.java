package com.paypal.wallet_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypal.wallet_service.entity.UserWallet;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

}
