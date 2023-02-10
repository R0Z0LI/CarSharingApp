package banger;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        String authHeader = String.format("Auth realm=\"%s\"", getRealmName());
        response.addHeader("WWW-Authenticate", authHeader);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = "{\"error\":\"unauthorized\"}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("hu.bme.aut");
        super.afterPropertiesSet();
    }
}
