package security.formlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.formlogin.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
