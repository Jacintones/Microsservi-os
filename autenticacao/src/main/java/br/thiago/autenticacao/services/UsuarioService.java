package br.thiago.autenticacao.services;

import br.thiago.autenticacao.models.Email;
import br.thiago.autenticacao.shared.UserDTO;

import java.util.List;

public interface UsuarioService {

    UserDTO salvar(UserDTO userDTO, Email email);

    List<UserDTO> obterTodos();
}
