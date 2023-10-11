package security.formlogin.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import security.formlogin.domain.Account;

import java.io.IOException;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Account account = (Account) authentication.getPrincipal();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), account);
    }
}
