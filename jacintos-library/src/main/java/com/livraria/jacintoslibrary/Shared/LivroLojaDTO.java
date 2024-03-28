package com.livraria.jacintoslibrary.Shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroLojaDTO {
    private Long id;

    private Long isbn;

    private String titulo;

    private String autor;

    private Integer paginas;

    private Integer anoLancamento;

    private Integer estoque;

    private double preco;

    private String sinopse;

    private String imagem;
}
