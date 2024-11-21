package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.entity.Wallet;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.WalletRepository;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.UserLibraryCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private CourseRepository courseRepository; // Thêm CourseRepository

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;

    @Override
    public Double getBalance(String userEid) {
        Wallet wallet = walletRepository.findByUserEid(userEid);
        
        // Nếu không tìm thấy wallet, tạo mới
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserEid(userEid);
            wallet.setBalance(0.0); // Khởi tạo số dư là 0
            walletRepository.save(wallet); // Lưu wallet mới vào cơ sở dữ liệu
        }
        
        return wallet.getBalance();
    }
    
    @Override
    public boolean payForCourse(String userEid, Long courseId) {
        Wallet wallet = walletRepository.findByUserEid(userEid);
        Course course = courseRepository.findById(courseId).orElse(null);

        // Kiểm tra xem wallet và course có tồn tại
        if (wallet == null || course == null) {
            return false; // Nếu không có wallet hoặc course, trả về false
        }

        // Kiểm tra xem khóa học đã tồn tại trong thư viện của người dùng chưa
        UserLibraryCourse existingCourse = userLibraryCourseService.getUserLibraryCourseById(courseId);
        if (existingCourse != null) {
            // Khóa học đã được mua trước đó
            return false; // Không thực hiện thanh toán
        }

        // Kiểm tra số dư có đủ không
        if (wallet.getBalance() >= course.getDiscountedPrice()) {
            // Trừ số dư
            wallet.setBalance(wallet.getBalance() - course.getDiscountedPrice());
            walletRepository.save(wallet); // Lưu wallet mới vào cơ sở dữ liệu

            // Thêm khóa học vào thư viện bằng phương thức đã có
            userLibraryCourseService.addCourseToLibrary(userEid, courseId, UserLibraryCourse.PaymentStatus.PURCHASED, UserLibraryCourse.ProgressStatus.IN_PROGRESS);

            return true; // Thanh toán thành công
        }

        // Nếu không đủ điều kiện, không làm gì cả và trả về false
        return false; // Thanh toán không thành công
    }
}
