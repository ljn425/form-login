package security.formlogin.security.metadatasource;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;

public class MyCustomRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/mypage")) {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            Collection<? extends GrantedAuthority> authorities =
//                    authentication.getAuthorities();
//            for (GrantedAuthority authority : authorities) {
//                if ("ROLE_USER".equals(authority.getAuthority())) {
//                    return true;
//                }
//            }
            return true;
        }
       return false;
    }
}
