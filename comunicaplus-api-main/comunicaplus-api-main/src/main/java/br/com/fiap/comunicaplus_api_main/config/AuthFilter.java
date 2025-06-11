package br.com.fiap.comunicaplus_api_main.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.comunicaplus_api_main.model.User;
import br.com.fiap.comunicaplus_api_main.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    // Lista de rotas públicas que devem ser ignoradas pelo filtro
    private static final List<String> PUBLIC_PATHS = List.of(
        "/auth/login",
        "/auth/register", 
        "/users", // Para criação de usuários
        "/v3/api-docs",
        "/swagger-ui",
        "/swagger-ui.html",
        "/swagger-resources",
        "/webjars",
        "/h2-console",
        "/error",
        "/api/devices", // Endpoint público específico
        "/favicon.ico"  // Favicon requests
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== FILTRO DE AUTENTICAÇÃO ===");
        System.out.println("→ Processando: " + request.getServletPath());

        String path = request.getServletPath();

        // Ignorar autenticação em rotas públicas
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            System.out.println("→ Rota pública liberada: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        var header = request.getHeader("Authorization");

        if (header == null) {
            System.out.println("→ Sem header Authorization - rejeitando requisição");
            sendUnauthorizedResponse(response, "Header Authorization é obrigatório");
            return; 
        }

        if (!header.startsWith("Bearer ")) {
            System.out.println("→ Token não começa com 'Bearer '");
            sendUnauthorizedResponse(response, "Token deve começar com Bearer");
            return; 
        }

        var token = header.replace("Bearer ", "").trim();

        try {
            User user = tokenService.getUserFromToken(token);

            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("→ Usuário autenticado: " + user.getUsername());
                
                filterChain.doFilter(request, response);
            } else {
                System.out.println("→ Usuário não encontrado no token");
                sendUnauthorizedResponse(response, "Token inválido - usuário não encontrado");
                return; 
            }

        } catch (Exception e) {
            System.err.println("→ Erro ao validar token: " + e.getMessage());
            e.printStackTrace(); // Para debug
            sendUnauthorizedResponse(response, "Token inválido ou expirado");
            return; 
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        
        if (response.isCommitted()) {
            System.err.println("→ Response já foi enviada, não é possível modificar");
            return;
        }
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); 
        response.getWriter().write(String.format("""
            { "message": "%s", "status": 401 }
        """, message));
        response.getWriter().flush();
    }
}