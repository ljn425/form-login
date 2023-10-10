package security.formlogin.security.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * 폼 로그인에서 보내 오는 추가 파라미터를 저장하기 위한 클래스
 */
@Getter
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {
    private String secretKey;
    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");
    }
}
