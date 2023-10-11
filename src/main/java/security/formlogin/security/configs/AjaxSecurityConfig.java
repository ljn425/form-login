package security.formlogin.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.formlogin.security.filter.AjaxLoginProcessingFilter;
import security.formlogin.security.handler.AjaxAuthenticationFailureHandler;
import security.formlogin.security.handler.AjaxAuthenticationSuccessHandler;
import security.formlogin.security.provider.AjaxAuthenticationProvider;
import security.formlogin.security.provider.FormAuthenticationProvider;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Order(0)
public class AjaxSecurityConfig {

    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;
    private final FormAuthenticationProvider formAuthenticationProvider;
    @Bean
    public SecurityFilterChain ajaxFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(ajaxLoginProcessingFilter(http), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(ajaxAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(formAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter(HttpSecurity http) throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter("/api/login");
        ajaxLoginProcessingFilter.setAuthenticationManager(authManager(http));
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
        return ajaxLoginProcessingFilter;
    }


}
