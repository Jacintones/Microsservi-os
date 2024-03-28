package com.livraria.jacintoslibrary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.livraria.jacintoslibrary.Model.Livro;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    //repository de livros e chave integer
    List<Livro> findByDono(Long dono);

}
