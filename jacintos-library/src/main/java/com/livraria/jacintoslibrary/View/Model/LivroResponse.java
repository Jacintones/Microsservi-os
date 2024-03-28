package com.livraria.jacintoslibrary.View.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Response é o que eu vou devolver depois da requisiçao
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroResponse {

    private Long id;

    private Long dono;

    private long isbn;

    private String titulo;

    private String autor;

    private Integer paginas;

    private Integer anoLancamento;

    private double preco;
    
    private String sinopse;
    
    private String imagem;


}
