package br.thiago.autenticacao.security;

import br.thiago.autenticacao.models.Usuario;
import br.thiago.autenticacao.repository.UsuarioRepository;
import br.thiago.autenticacao.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extraiTokeHeader(request);

        if (token != null) {
            String email = service.validaTokenJWT(token);
            Optional<Usuario> usuarioOptional = repository.findByEmail(email);

            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                var autentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(autentication);
            }
        }


        filterChain.doFilter(request, response);
    }
    public String extraiTokeHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7); // Ignorar o prefixo "Bearer "
    }
}
