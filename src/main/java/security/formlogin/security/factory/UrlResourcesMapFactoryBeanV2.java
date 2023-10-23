package security.formlogin.security.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.formlogin.security.service.SecurityResourceService;

import java.util.LinkedHashMap;
import java.util.List;

@RequiredArgsConstructor
public class UrlResourcesMapFactoryBeanV2 implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private final SecurityResourceService securityResourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;
    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() {
        if ( resourceMap == null) {
            init();
        }
        return resourceMap;
    }

    private void init() {
        resourceMap = securityResourceService.getResourceListV2();
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
