package security.formlogin;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 정책
                        .sessionFixation().changeSessionId() // 세션 고정 보호(default), none: 세션 고정 공격에 당함, migrateSession, newSession
                        .maximumSessions(1) // 최대 허용 가능 세션 수, -1은 무제한 로그인 세션 허용
                        .maxSessionsPreventsLogin(false) // true 동시 로그인 차단(기존 세션 유지), false는 기존 세션 만료(default)
                        .expiredUrl("/expired") // 세션이 만료된 경우 이동할 페이지
//                                .sessionRegistry(sessionRegistry -> {}) // 세션 저장소 설정
//                                .expiredSessionStrategy((expiredSessionStrategy) -> {}) // 세션 만료 후 처리
                );

        return http.build();
    }
}
