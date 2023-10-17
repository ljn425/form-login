package security.formlogin.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import security.formlogin.domain.entity.Account;

@Controller
public class MessageController {

    @GetMapping("/messages")
    public String message() {
        return "user/messages";
    }

    @ResponseBody
    @GetMapping("/api/messages")
    public String apiMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
//        if (session != null) {
//            Account loginMember = (Account) session.getAttribute("LOGIN_MEMBER");
//            if (loginMember.getUserRoles().equals("ROLE_MANAGER")) {
//                return "message ok";
//            } else {
//                return "access denied";
//            }
//        }

        return "session null";
    }
}
