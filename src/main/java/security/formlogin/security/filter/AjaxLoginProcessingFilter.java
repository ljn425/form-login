package security.formlogin.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.thymeleaf.util.StringUtils;
import security.formlogin.domain.AccountDto;
import security.formlogin.security.token.AjaxAuthenticationToken;

import java.io.IOException;

/**
 * Ajax 로그인 요청을 처리하는 필터
 * - AJax 요청인지 확인
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public AjaxLoginProcessingFilter(String customFilterProcessUrl) {
        super(customFilterProcessUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        // Ajax 요청이 아닐 경우 예외 처리
        if (!isAjax(request)) {

            throw new IllegalStateException("Ajax is not supported");
        }

        // Ajax 요청일 경우 ObjectMapper 를 통해 JSON 데이터(유저 정보)를 읽어옴
        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if(StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        // Ajax 로그인 토큰 생성(인증전)
        AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

        // `AuthenticationManager`에 인증 위임
        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    /**
     * Ajax 요청인지 `Header`를 통해 확인
     */
    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
