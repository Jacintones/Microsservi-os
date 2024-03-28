package com.livraria.jacintoslibrary.Repository;

import com.livraria.jacintoslibrary.Model.LivroLoja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroLojaRepository extends JpaRepository<LivroLoja, Long> {

}
