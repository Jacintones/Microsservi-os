package br.thiago.autenticacao.repository;

import br.thiago.autenticacao.models.Available;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableRepository extends JpaRepository<Available, Long> {
}
