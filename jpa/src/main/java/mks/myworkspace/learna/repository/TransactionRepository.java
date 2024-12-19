package mks.myworkspace.learna.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
	Optional<Transaction> findByReferenceNumber(String referenceNumber);
}
