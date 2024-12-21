package mks.myworkspace.learna.controller;

import lombok.Data;
import mks.myworkspace.learna.entity.Voucher;
import mks.myworkspace.learna.service.VoucherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping("/available")
    public ResponseEntity<List<Voucher>> getAvailableVouchers(@RequestBody VoucherRequest request) {
        List<Voucher> vouchers = voucherService.getAvailableVouchers(
            request.getCourseId(), 
            request.getPrice()
        );
        return ResponseEntity.ok(vouchers);
    }

    @PostMapping("/validate")
    public ResponseEntity<VoucherValidationResponse> validateVoucher(
            @RequestBody VoucherValidationRequest request) {
        boolean isValid = voucherService.isVoucherValid(
            request.getVoucherId(),
            request.getCourseId(),
            request.getPrice()
        );
        
        Double discountedPrice = null;
        if (isValid) {
            discountedPrice = voucherService.calculateDiscountedPrice(
                request.getVoucherId(),
                request.getPrice()
            );
        }
        
        return ResponseEntity.ok(new VoucherValidationResponse(isValid, discountedPrice));
    }
}

@Data
class VoucherRequest {
    private Long courseId;
    private Double price;
}

@Data
class VoucherValidationRequest {
    private Long voucherId;
    private Long courseId;
    private Double price;
}

@Data
class VoucherValidationResponse {
    private boolean valid;
    private Double discountedPrice;
    
    public VoucherValidationResponse(boolean valid, Double discountedPrice) {
        this.valid = valid;
        this.discountedPrice = discountedPrice;
    }
}