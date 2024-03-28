package br.thiago.autenticacao.shared;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroDTO {
    private Long id;

    private Long dono;

    private long isbn;

    private String titulo;

    private String autor;

    private Integer paginas;

    private Integer anoLancamento;

    private Integer estoque;

    private double preco;

    private String sinopse;

    private String imagem;


}
