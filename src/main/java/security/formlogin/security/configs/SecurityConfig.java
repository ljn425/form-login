package security.formlogin.security.configs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import security.formlogin.security.common.FormWebAuthenticationDetails;
import security.formlogin.security.handler.FormAccessDeniedHandler;
import security.formlogin.security.handler.FormAuthenticationFailureHandler;
import security.formlogin.security.handler.FormAuthenticationSuccessHandler;
import security.formlogin.security.metadatasource.MyCustomRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig {

    private final AuthenticationDetailsSource<HttpServletRequest, FormWebAuthenticationDetails> authenticationDetailsSource;
    private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain httpFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> auzm) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 리소스 허용
                        .requestMatchers("/", "/users", "/login*").permitAll()
//                        .requestMatchers(new MyCustomRequestMatcher()).hasRole("USER")
//                        .requestMatchers("/messages").hasRole("MANAGER")
//                        .requestMatchers("/config").hasRole("ADMIN")
//                        .anyRequest().access(authz))
                        .anyRequest().access(auzm))
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login_proc")
                        .defaultSuccessUrl("/")
                        .authenticationDetailsSource(authenticationDetailsSource)
                        .successHandler(formAuthenticationSuccessHandler)
                        .failureHandler(formAuthenticationFailureHandler)
                        .permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler()));

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new FormAccessDeniedHandler("/denied");
    }






}
