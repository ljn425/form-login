package security.formlogin;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동 페이지
                        .deleteCookies("remember-me") // 로그아웃 후 쿠키 삭제
                        .addLogoutHandler((request, response, authentication) -> { // 로그아웃 핸들러
                            HttpSession session = request.getSession();
                            session.invalidate();   // 세션 무효화
                        })
                        .logoutSuccessHandler((request, response, authentication) -> { // 로그아웃 성공 후 핸들러
                            response.sendRedirect("/login");
                        })
                )
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeParameter("remember") // 기본 파라미터명은 remember-me
                        .tokenValiditySeconds(3600) // Default 14일
                        .userDetailsService(userDetailsService)
                );
        return http.build();
    }
}
