package br.thiago.autenticacao.view.models;

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
public class OrderRequest {

    private List<Book> books;

    private User user;

    private boolean complet;

    private Date date;

}
