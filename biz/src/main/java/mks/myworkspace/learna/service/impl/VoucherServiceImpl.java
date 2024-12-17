package mks.myworkspace.learna.service.impl;

import java.util.List;

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
    private VoucherRepository repo; // Sử dụng JPA Repository

    @Autowired
    private VoucherJdbcRespository voucherJdbcRepository; // Sử dụng JDBC

    @Override
    public VoucherRepository getRepo() {
        return repo;
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        // Nếu id null -> INSERT, ngược lại -> UPDATE
        return voucherJdbcRepository.save(voucher);
    }

    @Override
    public Voucher getVoucherById(Long id) {
        // Trả về voucher theo id, hoặc null nếu không tìm thấy
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteVoucher(Long id) {
        // Xóa voucher bằng JDBC
        voucherJdbcRepository.deleteById(id);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        // Trả về danh sách tất cả voucher
        return repo.findAll();
    }

    @Override
    public List<Voucher> getVouchersByStatus(String status) {
        // Tìm voucher theo status
        return repo.findByValueType(Voucher.ValueType.valueOf(status.toUpperCase()));
    }

    @Override
    public int getTotalVouchers() {
        // Đếm tổng số lượng voucher
        return repo.getTotalVouchers();
    }
}
