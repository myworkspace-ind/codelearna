package mks.myworkspace.learna.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Voucher;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.VoucherJdbcRespository;
import mks.myworkspace.learna.repository.VoucherRepository;
import mks.myworkspace.learna.service.UserLibraryCourseService;
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
    
    @Autowired
    private CourseRepository courseRepository;

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
    
    public boolean applyVoucherToCourse(Long voucherId, Long courseId) {
        // Tìm voucher
        Optional<Voucher> optionalVoucher = repo.findById(voucherId);
        if (optionalVoucher.isEmpty()) {
            return false; // Voucher không tồn tại
        }

        Voucher voucher = optionalVoucher.get();

        // Tìm khóa học
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return false; // Khóa học không tồn tại
        }

        Course course = optionalCourse.get();

        // Tính toán giá sau giảm
        double discountedPrice;
        if ("PERCENTAGE".equalsIgnoreCase(voucher.getValueType().name())) {
            discountedPrice = course.getOriginalPrice() - (course.getOriginalPrice() * voucher.getDiscountValue() / 100);
        } else if ("FIXED".equalsIgnoreCase(voucher.getValueType().name())) {
            discountedPrice = course.getOriginalPrice() - voucher.getDiscountValue();
        } else {
            return false; // Loại giảm giá không hợp lệ
        }

        // Cập nhật giá giảm vào khóa học
        course.setDiscountedPrice(Math.max(0, discountedPrice)); // Giá không âm
        courseRepository.save(course);

        return true;
    }
}
