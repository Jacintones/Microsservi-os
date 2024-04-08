package br.thiago.autenticacao.shared;

import br.thiago.autenticacao.models.Book;
import br.thiago.autenticacao.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDTO {
    private Long id;

    private String description;

    private Integer value;

    private Book book;

    private User user;

}
