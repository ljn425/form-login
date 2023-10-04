package security.formlogin;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsManager users() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}1111") // {noop}은 암호화를 하지 않는다는 의미
                .roles("USER")
                .build();
        UserDetails sys = User.builder()
                .username("sys")
                .password("{noop}1111")
                .roles("SYS")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}1111")
                .roles("ADMIN", "SYS", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, sys, admin);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                    .requestMatchers("/user").hasRole("USER") // user 요청에 대해 USER 권한만 허용
                    .requestMatchers("/admin/pay").hasRole("SYS") // admin/pay 요청에 대해 SYS 권한만 허용
                    .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SYS") // admin/** 요청에 대해 ADMIN, SYS 권한만 허용
                    .requestMatchers("/token").permitAll() // token 요청에 대해 모든 권한 허용
                    .anyRequest().authenticated() // 나머지 요청들은 인증되었을 시 자원 허용
                )
                .formLogin(withDefaults())
                .build();
    }
}
