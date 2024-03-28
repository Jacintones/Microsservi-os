package br.thiago.autenticacao.services;

import br.thiago.autenticacao.models.Email;
import br.thiago.autenticacao.shared.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioDTO salvar(UsuarioDTO usuarioDTO, Email email);

    List<UsuarioDTO> obterTodos();
}
