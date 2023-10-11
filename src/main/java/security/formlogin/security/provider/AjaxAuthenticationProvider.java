package security.formlogin.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import security.formlogin.security.common.FormWebAuthenticationDetails;
import security.formlogin.security.service.AccountContext;
import security.formlogin.security.service.CustomUserDetailsService;
import security.formlogin.security.token.AjaxAuthenticationToken;

@Component
@RequiredArgsConstructor
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 인증처리
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 전달 받은 유저 정보
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        // 전달 받은 유저 이름(ID)로 DB 에서 유저 정보 조회
        AccountContext accountContext = (AccountContext) customUserDetailsService.loadUserByUsername(username);

        // 비밀번호 매칭 실패시 예외 처리
        if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        return new AjaxAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
