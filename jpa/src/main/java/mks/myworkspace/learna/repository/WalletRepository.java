package mks.myworkspace.learna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

	Wallet findByUserEid(String userEid);
}	