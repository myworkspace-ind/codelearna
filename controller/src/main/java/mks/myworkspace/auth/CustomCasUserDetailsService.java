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

        return "admin".equalsIgnoreCase(username) ?
                User.withUsername(username).password("").roles("ADMIN", "USER").build():
                User.withUsername(username).password("").roles("USER").build();
    }
}
