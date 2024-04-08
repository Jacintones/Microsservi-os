package br.thiago.autenticacao.view.controllers;

import br.thiago.autenticacao.services.impl.OrderService;
import br.thiago.autenticacao.shared.OrderDTO;
import br.thiago.autenticacao.view.models.OrderRequest;
import br.thiago.autenticacao.view.models.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService service;

    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll(){
        return ResponseEntity.ok(service
                .getAll()
                .stream()
                .map(orderDTO -> mapper.map(orderDTO, OrderResponse.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<OrderResponse>> getById(@PathVariable Long id){

        Optional<OrderDTO> dto = service.getById(id);

        OrderResponse response = mapper.map(dto, OrderResponse.class);

        return ResponseEntity.ok(Optional.of(response));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> register(@RequestBody OrderRequest orderRequest){
        //Convert in dto
        OrderDTO dto = mapper.map(orderRequest, OrderDTO.class);

        //Save in data
        dto = service.register(dto);

        //Convert in response
        OrderResponse response = mapper.map(dto, OrderResponse.class);

        //Return Ok
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id, @RequestBody OrderRequest orderRequest){

        OrderDTO dto = mapper.map(orderRequest, OrderDTO.class);

        dto = service.update(id, dto);

        return ResponseEntity.ok(mapper.map(dto, OrderResponse.class ));

    }

    @PutMapping("/status/{id}")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id){
        OrderDTO dto = service.updateStatus(id);

        return ResponseEntity.ok(mapper.map(dto, OrderResponse.class ));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        service.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
