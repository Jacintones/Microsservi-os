package br.thiago.autenticacao.shared;

import br.thiago.autenticacao.models.Book;
import br.thiago.autenticacao.models.Sale;
import br.thiago.autenticacao.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private List<Book> books;

    private User user;

    private boolean complet;

    private Date date;

}
