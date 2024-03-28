package br.thiago.autenticacao.shared;

public record AuthDTO(
        Long id,
        String email,
        String senha
) {
}
