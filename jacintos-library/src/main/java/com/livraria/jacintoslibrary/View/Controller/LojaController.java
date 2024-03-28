package com.livraria.jacintoslibrary.View.Controller;

import com.livraria.jacintoslibrary.Services.LojaService;
import com.livraria.jacintoslibrary.Shared.LivroLojaDTO;
import com.livraria.jacintoslibrary.View.Model.LivroLojaRequest;
import com.livraria.jacintoslibrary.View.Model.LivroLojaResponse;

import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class LojaController {

    @Autowired
    private LojaService livroService;

    private final ModelMapper mapper = new ModelMapper(); //objeto de mapeamento

    @GetMapping("/comercio")
    public ResponseEntity<List<LivroLojaResponse>> obterLivrosComerciais(){
        List<LivroLojaDTO> livros = livroService.obterTodosLivrosComerciais();

        List<LivroLojaResponse> resp = livros.stream()
                .map(livroLojaDTO -> mapper.map(livroLojaDTO, LivroLojaResponse.class))
                .toList();



        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/cadastrar/comercio")
    @Retry(name = "default")
    public ResponseEntity<LivroLojaResponse> cadastrarComercio(@RequestBody LivroLojaRequest livroRequest){
        //Converter o request em dto para salvar no banco
        LivroLojaDTO dto = mapper.map(livroRequest, LivroLojaDTO.class);

        //Salvo no banco
        dto = livroService.cadastroComercio(dto);

        //Retorno o response entity, converto o dto em request
        return new ResponseEntity<>(mapper.map(dto, LivroLojaResponse.class), HttpStatus.OK);
    }

    @DeleteMapping("/comercio/{id}")
    @Retry(name = "default")
    public ResponseEntity<?> deletarComercio(@PathVariable Long id){
        livroService.deletarComercio(id);

        //Deu tudo certo e não tem nada pra devolver
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/atualizar/{id}")
    @Retry(name = "default")
    public ResponseEntity<LivroLojaResponse> atualizar(@PathVariable Long id, @RequestBody LivroLojaRequest livroRequest){
        //Converter para DTO o request
        LivroLojaDTO dto = mapper.map(livroRequest, LivroLojaDTO.class);

        //Atualizo no banco
        dto = livroService.atualizar(id, dto);

        //Retorno a instancia do response entity convertendo de dto para response
        return new ResponseEntity<>(mapper.map(dto, LivroLojaResponse.class), HttpStatus.OK);
    }
}
