package br.thiago.autenticacao.shared;

import br.thiago.autenticacao.enums.RoleEnum;
import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    private String nome;

    private String email;

    private String senha;

    private String imagem;

    private RoleEnum role = RoleEnum.USER;

    private List<LivroDTO> livros;

}
