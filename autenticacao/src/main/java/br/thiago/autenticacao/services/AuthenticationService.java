package br.thiago.autenticacao.services;

import br.thiago.autenticacao.shared.AuthDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {

    String obterToken(AuthDTO authDTO);

    String validaTokenJWT(String token);

}
