package security.formlogin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }
    @GetMapping("/admin/pay")
    public String adminPay() {
        return "adminPay";
    }
    @GetMapping("/admin/**")
    public String adminAll() {
        return "adminAll";
    }

    @GetMapping("/token")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();
        String headerName = csrfToken.getHeaderName();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("headerName", headerName);
        System.out.println(map.toString());
        return map;
    }


}
