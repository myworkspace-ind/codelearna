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
}
