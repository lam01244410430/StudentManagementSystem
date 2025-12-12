package com.stu.app.config;

import com.stu.app.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(); // Service chúng ta đã viết
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Trả về bộ mã hóa "không làm gì cả", nó sẽ so sánh thẳng chuỗi nhập vào với DB
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF để đơn giản hóa việc gọi Fetch API
                .authorizeHttpRequests(auth -> auth
                        // Tài nguyên tĩnh (CSS, JS, Images) ai cũng xem được
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        // Trang login công khai
                        .requestMatchers("/", "/login", "/api/auth/**").permitAll()

                        // Phân quyền theo Role (Prefix ROLE_ được thêm tự động)
                        .requestMatchers("/admin/**", "/api/users/**", "/api/classes/**").hasRole("ADMIN")
                        .requestMatchers("/teacher/**").hasRole("TEACHER")
                        .requestMatchers("/student/**").hasRole("STUDENT")

                        // Các API chung cần đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Đường dẫn tới trang login custom
                        .loginProcessingUrl("/perform_login") // URL form post tới (Spring tự xử lý)
                        .successHandler(myAuthenticationSuccessHandler()) // Xử lý chuyển hướng sau khi login thành công
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    // Logic điều hướng sau khi đăng nhập thành công
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Lấy danh sách quyền (Authorities)
            var authorities = authentication.getAuthorities();
            String role = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse("");

            // --- DEBUG LOG: Xem console để biết chính xác Role là gì ---
            System.out.println(">>> LOGIN SUCCESS! User: " + authentication.getName());
            System.out.println(">>> DETECTED ROLE: " + role);
            // ----------------------------------------------------------

            // Sửa logic so sánh: Dùng contains để linh hoạt (chấp nhận cả ADMIN lẫn ROLE_ADMIN)
            if (role.contains("ADMIN")) {
                response.sendRedirect("/admin/dashboard");
            } else if (role.contains("TEACHER")) {
                response.sendRedirect("/teacher/dashboard");
            } else if (role.contains("STUDENT")) {
                response.sendRedirect("/student/dashboard");
            } else {
                response.sendRedirect("/");
            }
        };
    }
}