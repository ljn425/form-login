package security.formlogin.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import security.formlogin.domain.entity.Resources;
import security.formlogin.repository.ResourcesRepository;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityResourceService {

    private final ResourcesRepository resourcesRepository;
    public LinkedHashMap<RequestMatcher, ConfigAttribute> getResourceList() {
        LinkedHashMap<RequestMatcher, ConfigAttribute> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllByOrderByOrderNumDesc();
        resourcesList.forEach(re -> {
            re.getRoleSet().forEach(role -> {
                result.put(
                        new AntPathRequestMatcher(re.getResourceName()),
                        new SecurityConfig(role.getRoleName())
                );
            });
        });
        return result;
    }
}
