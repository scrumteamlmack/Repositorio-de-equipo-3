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
        // No necesita inicialización
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Obtener la sesión HTTP (false = no crear una nueva si no existe)
        HttpSession session = httpRequest.getSession(false);
        
        // Verificar si hay sesión y si el usuario está autenticado
        // El LoginBean guarda "userId" en la sesión cuando el usuario se autentica
        boolean isAuthenticated = false;
        
        if (session != null) {
            try {
                // Verificar si existe userId en la sesión (esto indica que el usuario está autenticado)
                Object userId = session.getAttribute("userId");
                if (userId != null) {
                    isAuthenticated = true;
                    System.out.println("✅ SessionFilter: Usuario autenticado (userId: " + userId + ")");
                } else {
                    System.out.println("⚠️ SessionFilter: Sesión existe pero no hay userId");
                }
            } catch (IllegalStateException e) {
                // La sesión fue invalidada, no está autenticado
                System.out.println("⚠️ SessionFilter: Sesión invalidada");
                isAuthenticated = false;
            }
        } else {
            System.out.println("⚠️ SessionFilter: No hay sesión activa");
        }
        
        // Si no está autenticado, redirigir a aviso-logout
        if (!isAuthenticated) {
            String contextPath = httpRequest.getContextPath();
            String requestURI = httpRequest.getRequestURI();
            System.out.println("🔒 SessionFilter: Redirigiendo a aviso-logout desde: " + requestURI);
            httpResponse.sendRedirect(contextPath + "/faces/aviso-logout.xhtml");
            return;
        }
        
        // Agregar headers anti-cache para prevenir que el navegador guarde estas páginas
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        
        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No necesita limpieza
    }
}

