package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "SessionFilter", urlPatterns = {"/faces/pages/*"})
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        boolean isAuthenticated = false;
        
        if (session != null) {
            try {
                Object userId = session.getAttribute("userId");
                if (userId != null) {
                    isAuthenticated = true;
                    System.out.println("SessionFilter: Usuario autenticado (userId: " + userId + ")");
                } else {
                    System.out.println("SessionFilter: Sesión existe pero no hay userId");
                }
            } catch (IllegalStateException e) {
                System.out.println("⚠️ SessionFilter: Sesión invalidada");
                isAuthenticated = false;
            }
        } else {
            System.out.println("SessionFilter: No hay sesión activa");
        }
        
        if (!isAuthenticated) {
            String contextPath = httpRequest.getContextPath();
            String requestURI = httpRequest.getRequestURI();
            System.out.println("🔒 SessionFilter: Redirigiendo a aviso-logout desde: " + requestURI);
            httpResponse.sendRedirect(contextPath + "/faces/aviso-logout.xhtml");
            return;
        }
        
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}

