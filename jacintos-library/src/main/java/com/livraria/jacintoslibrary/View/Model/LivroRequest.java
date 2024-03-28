package com.livraria.jacintoslibrary.View.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Objeto que esta sendo requisitado
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroRequest {
//Não precisa do id pois ele vai no URL
    private long isbn;

    private Long dono;

    private String titulo;

    private String autor;

    private Integer paginas;

    private Integer anoLancamento;

    private double preco;
    
    private String sinopse;

    private String imagem;



}
