package mks.myworkspace.learna.service;

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
    
    List<Voucher> getAllVoucherByCourseId(Long courseId);
}
