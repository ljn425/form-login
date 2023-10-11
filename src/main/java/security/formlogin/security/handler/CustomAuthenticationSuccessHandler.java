package security.formlogin.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로그인 성공후 추가 작업을 위한 핸들러
 * - ex)
 * 로그인 성공후 접속하고자 했던 페이지 이동
 * 로그인 성공후 사용자 정보를 DB에 저장
 * 로그인 성공후 사용자의 권한을 체크하여 권한에 따른 페이지 이동

 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        setDefaultTargetUrl("/"); // 로그인 성공후 이동할 페이지(Default 설정)

        SavedRequest savedRequest = requestCache.getRequest(request, response); // 로그인 성공후 접속하고자 했던 페이지 정보를 가져옴
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();// 로그인 성공후 접속하고자 했던 페이지 URL
            redirectStrategy.sendRedirect(request, response, targetUrl); // 로그인 성공후 접속하고자 했던 페이지로 이동
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
