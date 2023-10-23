package security.formlogin.security.metadatasource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import security.formlogin.security.factory.UrlResourcesMapFactoryBean;
import security.formlogin.security.factory.UrlResourcesMapFactoryBeanV2;

import java.util.*;
import java.util.function.Supplier;

@Component
public class CustomAuthorizationManagerV2 implements AuthorizationManager<RequestAuthorizationContext> {
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    public CustomAuthorizationManagerV2(UrlResourcesMapFactoryBeanV2 urlResourcesMapFactoryBean) throws Exception {
        requestMap = urlResourcesMapFactoryBean.getObject();
    }
    @SneakyThrows
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();

        if (requestMap != null) {
            Set<ConfigAttribute> roles = new HashSet<>();

            for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                RequestMatcher matcher = entry.getKey();
                if(matcher.matches(request)) {
                    roles.addAll(entry.getValue());
                }
            }

            if (roles.isEmpty()) {
                return null;
            }

            return new AuthorizationDecision(checkRoles(roles, authentication));
        }
        return null;
    }

    private boolean checkRoles(Set<ConfigAttribute> roles, Supplier<Authentication> authentication) {
        for (ConfigAttribute role : roles) {
            boolean result = authentication.get().getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().matches(role.getAttribute()));
            if (result) return true;
        }
        return false;
    }
}
