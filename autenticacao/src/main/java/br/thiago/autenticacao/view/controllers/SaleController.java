package br.thiago.autenticacao.view.controllers;

import br.thiago.autenticacao.services.impl.SaleService;
import br.thiago.autenticacao.shared.SaleDTO;
import br.thiago.autenticacao.shared.UserDTO;
import br.thiago.autenticacao.view.models.SaleRequest;
import br.thiago.autenticacao.view.models.SaleResponse;
import br.thiago.autenticacao.view.models.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService service;

    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAll(){
        //Converter a lista dto para Response
        return ResponseEntity.ok(service
                .getAll()
                .stream()
                .map(saleDTO -> mapper.map(saleDTO, SaleResponse.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<SaleResponse>> getById(@PathVariable Long id){
        //Obter o dto pelo id
        Optional<SaleDTO> dto = service.getById(id);

        //Converter de dto para response
        SaleResponse response = mapper.map(dto.get(), SaleResponse.class);

        //Retornar o response
        return ResponseEntity.ok(Optional.of(response));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<UserResponse>> getUserById(@PathVariable Long id){
        //Obter o dto pelo service
        Optional<UserDTO> dto = service.getOrdersByUser(id);

        //Converter de dto para response
        UserResponse response = mapper.map(dto.get(), UserResponse.class);

        //Retornar o optional do response
        return ResponseEntity.ok(Optional.of(response));
    }

    @PostMapping
    public ResponseEntity<SaleResponse> register(@RequestBody SaleRequest saleRequest){
        //Converter para dto
        SaleDTO dto = mapper.map(saleRequest, SaleDTO.class);

        //Salvar no banco
        dto = service.register(dto);

        //Converter o response
        SaleResponse response = mapper.map(dto, SaleResponse.class);

        //Retornar Ok com o corpo criado
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> update(@PathVariable Long id, @RequestBody SaleRequest saleRequest){
        //Converter o corpo para dto
        SaleDTO dto = mapper.map(saleRequest, SaleDTO.class);

        //Atualizar com o service
        dto = service.update(id, dto);

        //Retornar o Ok com o corpo convertido
        return ResponseEntity.ok(mapper.map(dto, SaleResponse.class ));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        //Chamar o service para deletar
        service.delete(id);

        //Retorna a resposta Http
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
