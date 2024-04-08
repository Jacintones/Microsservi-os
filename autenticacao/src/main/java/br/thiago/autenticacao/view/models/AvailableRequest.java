package br.thiago.autenticacao.view.models;

import br.thiago.autenticacao.models.Book;
import br.thiago.autenticacao.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRequest {

    private String description;

    private Integer value;

    private Book book;

    private User user;

}
