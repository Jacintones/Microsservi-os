package br.thiago.autenticacao.view.models;

import br.thiago.autenticacao.enums.PaymentMethod;
import br.thiago.autenticacao.models.Order;
import br.thiago.autenticacao.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {

    private BigDecimal amount;

    private User user;

    private List<Order> orders;

    private Date date;

    private PaymentMethod paymentMethod;

}
