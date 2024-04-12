package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.models.User;
import br.thiago.autenticacao.shared.AuthDTO;
import br.thiago.autenticacao.repository.UserRepository;
import br.thiago.autenticacao.services.AuthenticationService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class AutenticacaoServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository repository;

    /**
     * Método para verificar como vai ser carregado, no caso, via email do usuário
     * @param email the username identifying the user whose data is required.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).get();
    }

    /**
     * Método para obter o token do usuário
     */
    @Override
    public String obterToken(AuthDTO authDTO) {

        Optional<User> usuario = repository.findByEmail(authDTO.email());

        return geraToken(usuario.get());
    }

    /**
     * Método para gerar o token do usuário,
     * coloca o subject, a data de expiraçao e o algoritmo
     * @param user user para poder epgar o token
     * @return
     */
    private String geraToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT
                    .create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);

        }catch (JWTCreationException e){
            throw new ResourceNotFoundException("Erro ao gerar o token");
        }

    }

    /**
     * Método para gerar uma data de expediçao do token,
     * Pega a data atual + 8 horas e define pro horario de brasília
     * @return
     */
    private Instant gerarDataExpiracao() {
        return LocalDateTime
                .now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    /**
     * Método para validar o token do usuário
     * @param token token a ser validado
     * @return
     */
    @Override
   public String validaTokenJWT(String token){

        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT
                    .require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        }catch (JWTVerificationException e){
            return "";
        }

    }

}
