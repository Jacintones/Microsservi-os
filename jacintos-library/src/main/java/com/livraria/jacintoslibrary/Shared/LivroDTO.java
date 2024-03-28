package com.livraria.jacintoslibrary.Shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroDTO {

    private Long id;

    private Long dono;

    private String isbn;

    private String titulo;

    private String autor;

    private Integer paginas;

    private Integer anoLancamento;

    private double preco;
    
    private String sinopse;

    private String imagem;


}
