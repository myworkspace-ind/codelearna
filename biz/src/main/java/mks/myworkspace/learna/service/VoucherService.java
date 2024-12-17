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
//    List<Voucher> getVouchersBySubcategory(Long id);
    //Search filter
//    List<Course> searchVouchersByKeywordAndFilters(String keyword, String sortOrder, String sortField, String level, String averageRating);
    //List<Voucher> searchVouchersByKeywordAndFilters(String keyword, String sortOrder, String sortField, String level, Long subcategoryId, String averageRating);

    //List<Voucher> getVouchersNotInLibrary(String userEid);
    
    int getTotalVouchers();
}
