package br.thiago.autenticacao.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "availables")
public class Available {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_available")
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "idUser")
    @JsonBackReference(value = "user-avaliables")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_book")
    @JsonBackReference
    private Book book;
}
