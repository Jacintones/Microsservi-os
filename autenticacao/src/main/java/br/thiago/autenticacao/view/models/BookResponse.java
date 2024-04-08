package br.thiago.autenticacao.view.models;

import br.thiago.autenticacao.models.Available;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;

    private String isbn;

    private String title;

    private String author;

    private Integer pages;

    private Date releaseYear;

    private Long stock;

    private BigDecimal price;

    private BigDecimal costPrice;

    private String synopsis;

    private String image;

    private List<Available> availables;

}
