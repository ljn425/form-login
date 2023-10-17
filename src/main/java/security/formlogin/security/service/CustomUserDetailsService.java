package security.formlogin.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.formlogin.domain.entity.Account;
import security.formlogin.domain.entity.Role;
import security.formlogin.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 로그인 사용자의 이름을 전달받아 사용자 정보를 가져오는 역할(인증 토큰 생성 X)
 * - 유저 이름 조회
 * - 유저 정보 및 권한 정보 전달
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found Username"));

        List<GrantedAuthority> collect = account.getUserRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet())
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new AccountContext(account, collect);
    }
}
