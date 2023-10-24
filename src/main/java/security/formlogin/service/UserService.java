package security.formlogin.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.formlogin.domain.entity.Account;
import security.formlogin.domain.dto.AccountDto;
import security.formlogin.domain.entity.Role;
import security.formlogin.repository.RoleRepository;
import security.formlogin.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * 회원가입
     */
    @Transactional
    public void createUser(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        account.updatePassword(passwordEncoder.encode(account.getPassword()));

        Role role = roleRepository.findByRoleName("ROLE_USER");
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        account.updateUserRoles(roles);

        userRepository.save(account);
    }

    @Transactional
    public void modifyUser(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);

        if (accountDto.getRoles() != null) {
            HashSet<Role> roles = new HashSet<>();
            accountDto.getRoles().forEach(role -> {
                Role r = roleRepository.findByRoleName(role);
                roles.add(r);
            });
            account.updateUserRoles(roles);
        }
        account.updatePassword(passwordEncoder.encode(account.getPassword()));
        userRepository.save(account);
    }

    public AccountDto getUser(Long id) {
        Account account = userRepository.findById(id).orElse(new Account());
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        List<String> roles = account.getUserRoles()
                .stream()
                .map(Role::getRoleName)
                .toList();

        accountDto.setRoles(roles);
        return accountDto;
    }

    public List<Account> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Secured("ROLE_MANAGER")
    public void order() {
        System.out.println("order");
    }


}
