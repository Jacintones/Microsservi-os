package br.thiago.autenticacao.view.models;

import br.thiago.autenticacao.enums.RoleEnum;
import br.thiago.autenticacao.models.Address;
import br.thiago.autenticacao.models.Available;
import br.thiago.autenticacao.models.Order;
import br.thiago.autenticacao.models.Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
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
