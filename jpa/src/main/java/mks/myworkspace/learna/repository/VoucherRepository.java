package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    //Tìm voucher theo khóa học
    @Query(value = "SELECT * FROM learna_voucher v join learna_voucher_course vc on v.id = vc.voucher_id where course_id = :courseId", nativeQuery = true)
    List<Voucher> findByCourseId(@Param("courseId") Long courseId);
    
    //Tìm voucher theo người dùng
    @Query(value = "SELECT * FROM learna_voucher v join learna_voucher_user vu on v.id = vu.voucher_id where user_id = :userId", nativeQuery = true)
    List<Voucher> findByUserId(@Param("userId") Long userId);
    
}


