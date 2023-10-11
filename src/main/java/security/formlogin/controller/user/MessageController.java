package security.formlogin.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    @GetMapping("/messages")
    public String message() {
        return "user/messages";
    }

    @ResponseBody
    @GetMapping("/api/messages")
    public String apiMessage(Authentication authentication) {
        System.out.println(authentication);
        return "messages ok";
    }
}
