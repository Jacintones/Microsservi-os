package br.thiago.autenticacao.view.controllers;


import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.services.impl.BookService;
import br.thiago.autenticacao.shared.BookDTO;
import br.thiago.autenticacao.view.models.BookRequest;
import br.thiago.autenticacao.view.models.BookResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/livros")
public class BookController {

    @Autowired
    private BookService bookService; //atributo do service

    private final ModelMapper mapper = new ModelMapper(); //objeto de mapeamento

    @GetMapping
    @Retry(name = "default")
    public ResponseEntity<List<BookResponse>> obterTodos(){
        //Obter todos retorna uma list
        List<BookDTO> livrosDtos = bookService.obterTodos();
        
        //Convertendo de dto para Response
        List<BookResponse> resposta = livrosDtos.stream()
                                                 .map(livro -> mapper.map(livro, BookResponse.class))
                                                 .collect(Collectors.toList());


        //Retornar o responseEntity
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    @Retry(name = "default")
    public ResponseEntity<Optional<BookResponse>> obterPorId(@PathVariable Long id){
        //Obtenho o livroDTO por id 
        Optional<BookDTO> livro = bookService.obterPorId(id);

        //Regra pra verificar se é nulo
        if (livro.isEmpty()) {
            throw new ResourceNotFoundException("ID: "+id+" não existe!");
        }

        //Converter de DTO para Response
        BookResponse response = mapper.map(livro.get(), BookResponse.class);

        //Retornar a response Entity
        return new ResponseEntity<>(Optional.of(response), HttpStatus.OK);
    }


    @PostMapping("/cadastrar")
    @Retry(name = "default")
    public ResponseEntity<BookResponse> cadastrar(@RequestBody BookRequest bookRequest){
        //Converter o request em dto para salvar no banco
        BookDTO dto = mapper.map(bookRequest, BookDTO.class);

        //Salvo no banco
        dto = bookService.cadastrar(dto);

        //Retorno o response entity, converto o dto em request
        return new ResponseEntity<>(mapper.map(dto, BookResponse.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Retry(name = "default")
    public ResponseEntity<?> deletar(@PathVariable Long id){
        bookService.deletar(id);

        //Deu tudo certo e não tem nada pra devolver 
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("/atualizar/{id}")
    @Retry(name = "default")
    public ResponseEntity<BookResponse> atualizar(@PathVariable Long id, @RequestBody BookRequest bookRequest){
        //Converter para DTO o request
        BookDTO dto = mapper.map(bookRequest, BookDTO.class);

        //Atualizo no banco
        dto = bookService.atualizar(id, dto);

        //Retorno a instancia do response entity convertendo de dto para response
        return new ResponseEntity<>(mapper.map(dto, BookResponse.class), HttpStatus.OK);
    }

    @GetMapping("/profit/{id}")
    public ResponseEntity<BigDecimal> calculateProfit(@PathVariable Long id){
        BigDecimal value = bookService.calculateProfit(id);

        return ResponseEntity.ok(value);
    }

    @PutMapping("/update/stock/{id}")
    public ResponseEntity<BookResponse> updateStock(@PathVariable Long id){
       BookDTO dto = bookService.updateStock(id);

       BookResponse response = mapper.map(dto, BookResponse.class);

       return ResponseEntity.ok(response);
    }
}
