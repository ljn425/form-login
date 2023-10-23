package security.formlogin.security.metadatasource;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.formlogin.domain.entity.Resources;
import security.formlogin.repository.ResourcesRepository;

import java.util.*;
@SpringBootTest
class CustomAuthorizationManagerTest {

    @Autowired
    private ResourcesRepository resourcesRepository;

    @DisplayName("CustomeAuthorizationManagerTest")
    public void requestMaps() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
//        requestMap.put(new AntPathRequestMatcher("/mypage"), List.of(new SecurityConfig("ROLE_USER"), new SecurityConfig("ROLE_MANAGER")));
        requestMap.put(new AntPathRequestMatcher("/mypage"), List.of(new SecurityConfig("ROLE_USER")));
        requestMap.put(new AntPathRequestMatcher("/mypage"), List.of(new SecurityConfig("ROLE_MANAGER")));
        requestMap.put(new AntPathRequestMatcher("/messages"), List.of(new SecurityConfig("ROLE_MANAGER")));
        requestMap.put(new AntPathRequestMatcher("/config"), List.of(new SecurityConfig("ROLE_ADMIN")));

        Set<ConfigAttribute> roles = new HashSet<>();
        for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
            String pattern = ((AntPathRequestMatcher) entry.getKey()).getPattern();
            if(pattern.equals("/mypage")) {
                roles.addAll(entry.getValue());
            }
        }
        for (ConfigAttribute role : roles) {
            boolean b = authorities.stream()
                    .anyMatch(r -> r.getAuthority().matches(role.getAttribute()));
            if(b) {
                System.out.println("포함");
                return;
            } else {
                System.out.println("비포함");
            }
        }
    }

    @Test
    @DisplayName("db")
    public void requestMapDB() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllByOrderByOrderNumDesc();
        resourcesList.forEach(re -> {
            re.getRoleSet().forEach(role -> {
                AntPathRequestMatcher resourceName = new AntPathRequestMatcher(re.getResourceName());
                SecurityConfig roleName = new SecurityConfig(role.getRoleName());
                if(requestMap.containsKey(resourceName)) {
                    requestMap.get(resourceName).add(roleName);
                } else {
                    requestMap.put(resourceName, new ArrayList<>(List.of(roleName)));
                }
            });
        });

        System.out.println(requestMap.toString());

    }

}