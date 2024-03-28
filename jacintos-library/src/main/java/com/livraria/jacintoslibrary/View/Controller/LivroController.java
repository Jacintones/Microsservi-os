package com.livraria.jacintoslibrary.View.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.livraria.jacintoslibrary.Model.Exceptions.ResourceNotFoundException;
import com.livraria.jacintoslibrary.Services.LivroService;
import com.livraria.jacintoslibrary.Shared.LivroDTO;
import com.livraria.jacintoslibrary.View.Model.LivroRequest;
import com.livraria.jacintoslibrary.View.Model.LivroResponse;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService; //atributo do service

    private final ModelMapper mapper = new ModelMapper(); //objeto de mapeamento

    @GetMapping
    @Retry(name = "default")
    public ResponseEntity<List<LivroResponse>> obterTodos(){
        //Obter todos retorna uma list
        List<LivroDTO> livrosDtos = livroService.obterTodos();
        
        //Convertendo de dto para Response
        List<LivroResponse> resposta = livrosDtos.stream()
                                                 .map(livro -> mapper.map(livro, LivroResponse.class))
                                                 .collect(Collectors.toList());


        //Retornar o responseEntity
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    @Retry(name = "default")
    public ResponseEntity<Optional<LivroResponse>> obterPorId(@PathVariable Long id){
        //Obtenho o livroDTO por id 
        Optional<LivroDTO> livro = livroService.obterPorId(id);

        //Regra pra verificar se é nulo
        if (livro.isEmpty()) {
            throw new ResourceNotFoundException("ID: "+id+" não existe!");
        }

        //Converter de DTO para Response
        LivroResponse response = mapper.map(livro.get(), LivroResponse.class);

        //Retornar a response Entity
        return new ResponseEntity<>(Optional.of(response), HttpStatus.OK);
    }

    @GetMapping("/{dono}/lista")
    public ResponseEntity<List<LivroResponse>> obterLivrosPorDono(@PathVariable Long dono){
        List<LivroDTO> livros = livroService.obterPorDono(dono);

        List<LivroResponse> responses = livros.stream()
                .map(l -> mapper.map(l, LivroResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/isbn/{isbn}")
    @Retry(name = "default")
    public ResponseEntity<Optional<LivroResponse>> obterPorIsbn(@PathVariable long isbn){
    	
    	//Obtenho a lista de livros responses
    	ResponseEntity<List<LivroResponse>>  livros = obterTodos();

    	//Faço o lambda procurando o primeiro
    	@SuppressWarnings("null")
        Optional<LivroResponse> livroProcurado = Objects.requireNonNull(livros.getBody())
											    	   .stream()
											    	   .filter(livro -> livro.getIsbn() == isbn)
											    	   .findFirst();
    	//Verifico se é vazio, se for, lança exceção
    	if (livroProcurado.isEmpty()) {
			throw new ResourceNotFoundException("Livro com isbn: "+ isbn+ " não encontrado");
		}
    	
        //Retorno a response entity 
    	return new ResponseEntity<>(livroProcurado, HttpStatus.OK);
    }
    
    @PostMapping("/cadastrar")
    @Retry(name = "default")
    public ResponseEntity<LivroResponse> cadastrar(@RequestBody LivroRequest livroRequest){
        //Converter o request em dto para salvar no banco
        LivroDTO dto = mapper.map(livroRequest, LivroDTO.class);

        //Salvo no banco
        dto = livroService.cadastrar(dto);

        //Retorno o response entity, converto o dto em request
        return new ResponseEntity<>(mapper.map(dto, LivroResponse.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Retry(name = "default")
    public ResponseEntity<?> deletar(@PathVariable Long id){
        livroService.deletar(id);

        //Deu tudo certo e não tem nada pra devolver 
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("/atualizar/{id}")
    @Retry(name = "default")
    public ResponseEntity<LivroResponse> atualizar(@PathVariable Long id, @RequestBody LivroRequest livroRequest){
        //Converter para DTO o request
        LivroDTO dto = mapper.map(livroRequest, LivroDTO.class);

        //Atualizo no banco
        dto = livroService.atualizar(id, dto);

        //Retorno a instancia do response entity convertendo de dto para response
        return new ResponseEntity<>(mapper.map(dto, LivroResponse.class), HttpStatus.OK);
    }
}
