package security.formlogin.security.metadatasource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import security.formlogin.security.factory.UrlResourcesMapFactoryBean;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

//@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private LinkedHashMap<RequestMatcher, ConfigAttribute> requestMap = new LinkedHashMap<>();

    public CustomAuthorizationManager(UrlResourcesMapFactoryBean urlResourcesMapFactoryBean) throws Exception {
        requestMap = urlResourcesMapFactoryBean.getObject();
    }

    @SneakyThrows
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String requestURI = request.getRequestURI();

//        requestMap.put(new AntPathRequestMatcher("/mypage"), new SecurityConfig("ROLE_USER"));
//        requestMap.put(new AntPathRequestMatcher("/messages"), new SecurityConfig("ROLE_MANAGER"));
//        requestMap.put(new AntPathRequestMatcher("/config"), new SecurityConfig("ROLE_ADMIN"));

        if (requestMap != null) {
            for (Map.Entry<RequestMatcher, ConfigAttribute> entry : requestMap.entrySet()) {
                RequestMatcher matcher = entry.getKey();
                if(matcher.matches(request)) {
                    Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
                    for (GrantedAuthority authority : authorities) {
                        if ( authority.getAuthority().equals(entry.getValue().getAttribute())) {
                            return new AuthorizationDecision(true);
                        }
                    }
                    return new AuthorizationDecision(false);
                }
            }
        }
        return null;
    }
}
