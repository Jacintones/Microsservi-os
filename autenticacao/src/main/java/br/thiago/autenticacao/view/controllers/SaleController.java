package br.thiago.autenticacao.view.controllers;

import br.thiago.autenticacao.models.User;
import br.thiago.autenticacao.services.impl.SaleService;
import br.thiago.autenticacao.shared.SaleDTO;
import br.thiago.autenticacao.shared.UserDTO;
import br.thiago.autenticacao.view.models.SaleRequest;
import br.thiago.autenticacao.view.models.SaleResponse;
import br.thiago.autenticacao.view.models.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(service
                .getAll()
                .stream()
                .map(saleDTO -> mapper.map(saleDTO, SaleResponse.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<SaleResponse>> getById(@PathVariable Long id){

        Optional<SaleDTO> dto = service.getById(id);

        SaleResponse response = mapper.map(dto, SaleResponse.class);

        return ResponseEntity.ok(Optional.of(response));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<UserResponse>> getUserById(@PathVariable Long id){
        Optional<UserDTO> dto = service.getOrdersByUser(id);

        UserResponse response = mapper.map(dto, UserResponse.class);

        return ResponseEntity.ok(Optional.of(response));
    }

    @PostMapping
    public ResponseEntity<SaleResponse> register(@RequestBody SaleRequest saleRequest){
        //Convert in dto
        SaleDTO dto = mapper.map(saleRequest, SaleDTO.class);

        //Save in data
        dto = service.register(dto);

        //Convert in response
        SaleResponse response = mapper.map(dto, SaleResponse.class);

        //Return Ok
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> update(@PathVariable Long id, @RequestBody SaleRequest saleRequest){

        SaleDTO dto = mapper.map(saleRequest, SaleDTO.class);

        dto = service.update(id, dto);

        return ResponseEntity.ok(mapper.map(dto, SaleResponse.class ));

    }
}
