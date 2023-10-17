package security.formlogin.security.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import security.formlogin.domain.entity.Account;
import security.formlogin.domain.entity.Resources;
import security.formlogin.domain.entity.Role;
import security.formlogin.repository.ResourcesRepository;
import security.formlogin.repository.RoleRepository;
import security.formlogin.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final PasswordEncoder passwordEncoder;
    private static AtomicInteger count = new AtomicInteger(0);
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        setupSecurityResources();

        alreadySetup = true;

    }

    @Transactional
    public void setupSecurityResources() {
        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);
        Resources url = createResourceIfNotFound("/admin/**", "", roles, "url");
        Account account = createUserIfNotFound("admin", "pass", "admin@gmial.com", 10, roles);

    }
    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);

        if(role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }
        return roleRepository.save(role);
    }

    public Account createUserIfNotFound(String userName, String password, String email, int age, Set<Role> roleSet) {

        Account account = userRepository.findByUsername(userName).orElse(null);

        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .email(email)
                    .age(age)
                    .password(passwordEncoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }

        return userRepository.save(account);
    }

    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourceName)
                    .roleSet(roleSet)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(count.incrementAndGet())
                    .build();
        }
        return resourcesRepository.save(resources);
    }
}