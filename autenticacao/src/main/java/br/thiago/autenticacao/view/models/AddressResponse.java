package br.thiago.autenticacao.view.models;

import br.thiago.autenticacao.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private Long id;

    private String zipCode;

    private String country;

    private String state;

    private String city;

    private String neighborhood;

    private String street;

    private Long number;

    private User user;

}
