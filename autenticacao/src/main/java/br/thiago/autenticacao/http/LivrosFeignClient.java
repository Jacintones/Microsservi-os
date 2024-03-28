package br.thiago.autenticacao.http;

import br.thiago.autenticacao.shared.LivroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "book-service")
public interface LivrosFeignClient {

    @GetMapping(path = "/api/livros/{dono}/lista")
    List<LivroDTO> obterLivros(@PathVariable Long dono);
}
