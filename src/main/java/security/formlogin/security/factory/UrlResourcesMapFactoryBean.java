package security.formlogin.security.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.formlogin.security.service.SecurityResourceService;

import java.util.LinkedHashMap;
@RequiredArgsConstructor
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, ConfigAttribute>> {

    private final SecurityResourceService securityResourceService;
    private LinkedHashMap<RequestMatcher, ConfigAttribute> resourceMap;
    @Override
    public LinkedHashMap<RequestMatcher, ConfigAttribute> getObject() throws Exception {
        if ( resourceMap == null) {
            init();
        }
        return resourceMap;
    }

    private void init() {
        resourceMap = securityResourceService.getResourceList();
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }
    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
