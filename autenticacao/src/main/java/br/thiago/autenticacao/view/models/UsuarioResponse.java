package br.thiago.autenticacao.view.models;

import br.thiago.autenticacao.enums.RoleEnum;
import br.thiago.autenticacao.shared.LivroDTO;
import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioResponse {

    private Long id;

    private String nome;

    private String email;

    private String senha;

    private String imagem;

    private RoleEnum role = RoleEnum.USER;

    private List<LivroDTO> livros;
}
