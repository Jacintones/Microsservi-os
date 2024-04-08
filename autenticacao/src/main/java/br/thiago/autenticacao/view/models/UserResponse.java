package br.thiago.autenticacao.view.models;

import br.thiago.autenticacao.enums.RoleEnum;
import br.thiago.autenticacao.models.Address;
import br.thiago.autenticacao.models.Available;
import br.thiago.autenticacao.models.Order;
import br.thiago.autenticacao.models.Sale;
import br.thiago.autenticacao.shared.AvailableDTO;
import br.thiago.autenticacao.shared.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String image;

    private RoleEnum role;

    private List<Available> availables;

    private List<Address> addresses;

    private List<Order> orders;

    private List<Sale> sales;

}
