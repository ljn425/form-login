package security.formlogin.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.formlogin.domain.Account;
import security.formlogin.domain.AccountDto;
import security.formlogin.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * 회원가입
     */
    @Transactional
    public void createUser(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        account.updatePassword(passwordEncoder.encode(account.getPassword()));
        userRepository.save(account);
    }
}
