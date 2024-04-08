package br.thiago.autenticacao.models;

import br.thiago.autenticacao.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = " idSale")
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "idUser")
    @JsonBackReference(value = "user-sales")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "sale_order",
            joinColumns = @JoinColumn(name = "id_sale"),
            inverseJoinColumns = @JoinColumn(name = "id_order")
    )
    private List<Order> orders;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;
}
