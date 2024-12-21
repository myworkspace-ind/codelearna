package mks.myworkspace.learna.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Voucher;
import mks.myworkspace.learna.repository.VoucherJdbcRespository;
import mks.myworkspace.learna.repository.VoucherRepository;
import mks.myworkspace.learna.service.VoucherService;

@Slf4j
@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository repo;

    @Autowired
    private VoucherJdbcRespository voucherJdbcRepository;

    @Override
    public VoucherRepository getRepo() {
        return repo;
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        return voucherJdbcRepository.save(voucher);
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteVoucher(Long id) {
        voucherJdbcRepository.deleteById(id);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        // Trả về danh sách tất cả voucher
        return repo.findAll();
    }

    @Override
    public int getTotalVouchers() {
        // Đếm tổng số lượng voucher
        return repo.getTotalVouchers();
    }
    @Override
    public List<Voucher> getAvailableVouchers(Long courseId, Double price) {
        Date currentDate = new Date();
        
        return repo.findAll().stream()
            .filter(voucher -> {
                // Kiểm tra thời gian hiệu lực
                boolean isValidDate = currentDate.after(voucher.getStartDate()) 
                    && currentDate.before(voucher.getEndDate());
                
                // Kiểm tra số lượng còn lại
                boolean hasQuantity = voucher.getQuantity() > 0;
                
                return isValidDate && hasQuantity;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean isVoucherValid(Long voucherId, Long courseId, Double price) {
        Voucher voucher = getVoucherById(voucherId);
        if (voucher == null) return false;
        
        Date currentDate = new Date();
        
        return currentDate.after(voucher.getStartDate()) 
            && currentDate.before(voucher.getEndDate())
            && voucher.getQuantity() > 0;
    }
    
    @Override
    public Double calculateDiscountedPrice(Long voucherId, Double originalPrice) {
        Voucher voucher = getVoucherById(voucherId);
        if (voucher == null) return originalPrice;
        
        double discountAmount = 0;
        
        if (voucher.getValueType() == Voucher.ValueType.PERCENTAGE) {
            discountAmount = (originalPrice * voucher.getDiscountValue()) / 100;
            // Kiểm tra giới hạn giảm giá tối đa
            if (voucher.getMaxValue() != null && discountAmount > voucher.getMaxValue()) {
                discountAmount = voucher.getMaxValue();
            }
        } else {
            discountAmount = voucher.getDiscountValue();
        }
        
        return Math.max(0, originalPrice - discountAmount);
    }
    
    @Override
    @Transactional
    public boolean updateVoucherUsage(Long voucherId) {
        Voucher voucher = getVoucherById(voucherId);
        if (voucher == null || voucher.getQuantity() <= 0) return false;
        
        voucher.setQuantity(voucher.getQuantity() - 1);
        saveVoucher(voucher);
        return true;
    }
}
