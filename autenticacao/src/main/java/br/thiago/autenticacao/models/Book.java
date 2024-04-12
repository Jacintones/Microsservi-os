package br.thiago.autenticacao.models;

import br.thiago.autenticacao.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn10;

    @Column(nullable = false, unique = true)
    private String isbn13;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Integer pages;

    @Column(nullable = false)
    private Date releaseYear;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Column(columnDefinition = "TEXT" , nullable = false)
    private String synopsis;

    @Column(nullable = false)
    private CategoryType categoryType;

    @Column(nullable = false)
    private String formatBook; //Capa dura ou mole

    private String image;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<Available> availables;


}
