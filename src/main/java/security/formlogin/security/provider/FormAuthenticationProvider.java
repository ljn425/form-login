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
@Component
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 인증처리
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 로그인 폼에서 전달 받은 유저 정보
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        // 로그인 폼에서 전달 받은 유저 이름(ID)로 DB 에서 유저 정보 조회
        AccountContext accountContext = (AccountContext) customUserDetailsService.loadUserByUsername(username);

        // 비밀번호 매칭 실패시 예외 처리
        if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }

        // 추가 인증 정보 처리
        FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = formWebAuthenticationDetails.getSecretKey();
        if (!"secret".equals(secretKey)) {
            throw new InsufficientAuthenticationException("secretKey 인증 예외");
        }  

        // 비밀번호 매칭 성공시 인증 토큰 생성(비밀번호(credentials)는 보안상 이유로 null 설정
        return new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
    }


    /**
     * 토큰 타입과 일치할 때 인증 처리
     * - UsernamePasswordAuthenticationToken 타입과 일치할 때 인증 처리
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
