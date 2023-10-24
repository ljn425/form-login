package security.formlogin.aopsecurity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import security.formlogin.domain.dto.AccountDto;

import java.security.Principal;

@Controller
public class AopSecurityController {

    /**
     *  principal은 인터페이스이고 폼로그인에서 User 인터페이스를 구현한 클래스를 받는다.
     *  이 애플리케이션에서는 AccountContext로 User 인터페이스를 구현했고 AccountContext는 Account 객체를 get 한다.
     *  따라서 Account 객체의 속성을 얻을 수 있다.
     * @return
     */
    @GetMapping("/preAuthorize")
    @PreAuthorize("hasRole('ROLE_USER') and #accountDto.username == authentication.name")
    public String preAuthorize(AccountDto accountDto, Model model, Authentication authentication) {
        model.addAttribute("method", "Success @PreAuthorize");

        return "aop/method";
    }
}
