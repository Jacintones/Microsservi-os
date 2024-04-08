package br.thiago.autenticacao.view.controllers;

import br.thiago.autenticacao.shared.AuthDTO;
import br.thiago.autenticacao.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService service;



    /**
     * Método de autenticação
     * @param authDTO
     * @return retorna o token
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String auth(@RequestBody AuthDTO authDTO){

        //Cria o usuario autenticado
        var usuarioAuthenticationToken = new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.password());

        //Autentica
        authenticationManager.authenticate(usuarioAuthenticationToken);


        //Obtém o token
        return service.obterToken(authDTO);
    }
}
