package com.livraria.jacintoslibrary.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LivroLoja {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
