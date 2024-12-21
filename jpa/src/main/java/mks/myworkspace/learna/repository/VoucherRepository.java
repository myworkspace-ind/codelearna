package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    List<Voucher> findByCampaignId(Long campaignId);

    // Đếm tổng số lượng voucher
    @Query("SELECT COUNT(v.id) FROM Voucher v")
    int getTotalVouchers();

    // Tìm voucher theo loại giảm giá (percentage hoặc fixed)
    List<Voucher> findByValueType(Voucher.ValueType valueType);
}
