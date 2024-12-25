package mks.myworkspace.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomCasUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Tạo một UserDetails (nếu cần, có thể kết nối với cơ sở dữ liệu để lấy thông tin người dùng)
        return User.withUsername(username)
                   .password("")  // CAS không sử dụng mật khẩu thông thường, nên để trống
                   .roles("USER")  // Ví dụ, chỉ định quyền cho người dùng
                   .build();
    }
}
