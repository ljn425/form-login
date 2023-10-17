package security.formlogin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.formlogin.domain.entity.Role;
import security.formlogin.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(long id) {
        return roleRepository.findById(id).orElse(new Role());
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
    @Transactional
    public void createRole(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }






}
