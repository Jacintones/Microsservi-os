package br.thiago.autenticacao.shared;

import br.thiago.autenticacao.models.Available;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
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
