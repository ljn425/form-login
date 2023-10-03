package security.formlogin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
//                        .loginPage("/loginPage") // 로그인 페이지 경로(로그인 페이지 custom 가능)
                        .defaultSuccessUrl("/") // 로그인 성공 후 이동 페이지
//                        .failureUrl("/login") // 로그인 실패 후 이동 페이지
//                        .usernameParameter("userId") // 아이디 파라미터명 설정(html name명 설정)
//                        .passwordParameter("passwd") // 패스워드 파라미터명 설정
                        .loginProcessingUrl("/login_proc") // 로그인 html Form Action Url
                        .successHandler((request, response, authentication) -> { // 로그인 성공 후 핸들러
                            System.out.println("authentication : " + authentication.getName());
                            response.sendRedirect("/");
                        })
                        .failureHandler((request, response, exception) -> { // 로그인 실패 후 핸들러
                            System.out.println("exception : " + exception.getMessage());
                            response.sendRedirect("/login");
                        })
                        .permitAll() // 로그인 페이지는 누구나 접근 가능
                );
        return http.build();
    }
}
