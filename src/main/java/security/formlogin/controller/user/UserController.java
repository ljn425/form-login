package security.formlogin.controller.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import security.formlogin.domain.Account;
import security.formlogin.domain.AccountDto;
import security.formlogin.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 마이페이지
     */
    @GetMapping("/mypage")
    public String myPage() {
        return "user/mypage";
    }

    /**
     * 회원가입 페이지
     */
    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {
        userService.createUser(accountDto);
        return "redirect:/";
    }
}
