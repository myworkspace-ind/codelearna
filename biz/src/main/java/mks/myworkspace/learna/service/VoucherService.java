package mks.myworkspace.learna.service;

import java.util.Date;
import java.util.List;
import mks.myworkspace.learna.entity.Voucher;
import mks.myworkspace.learna.repository.VoucherRepository;

public interface VoucherService {
    VoucherRepository getRepo();
    
    Voucher saveVoucher(Voucher voucher);
    
    Voucher getVoucherById(Long id);
    
    void deleteVoucher(Long id);
    
    List<Voucher> getAllVouchers();
    
    int getTotalVouchers();
    
    // Thêm các phương thức mới
    
    /**
     * Lấy danh sách voucher có thể áp dụng cho một khóa học cụ thể
     * @param courseId ID của khóa học
     * @param price Giá của khóa học
     * @return Danh sách voucher có thể áp dụng
     */
    List<Voucher> getAvailableVouchers(Long courseId, Double price, String status);
    
    /**
     * Kiểm tra voucher có thể áp dụng không
     * @param voucherId ID của voucher
     * @param courseId ID của khóa học
     * @param price Giá của khóa học
     * @return true nếu voucher có thể áp dụng
     */
    boolean isVoucherValid(Long voucherId, Long courseId, Double price, String status);
    
    /**
     * Tính toán giá sau khi áp dụng voucher
     * @param voucherId ID của voucher
     * @param originalPrice Giá gốc
     * @return Giá sau khi áp dụng voucher
     */
    Double calculateDiscountedPrice(Long voucherId, Double originalPrice);
    
    /**
     * Cập nhật số lượng voucher sau khi sử dụng
     * @param voucherId ID của voucher
     * @return true nếu cập nhật thành công
     */
    boolean updateVoucherUsage(Long voucherId);
}