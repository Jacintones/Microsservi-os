package br.thiago.autenticacao.view.controllers;

import br.thiago.autenticacao.services.impl.AddressService;
import br.thiago.autenticacao.shared.AddressDTO;
import br.thiago.autenticacao.view.models.AddressRequest;
import br.thiago.autenticacao.view.models.AddressResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService service;

    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAll(){
        return ResponseEntity.ok(service
                .getAll()
                .stream()
                .map(addressDTO -> mapper.map(addressDTO, AddressResponse.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<AddressResponse>> getById(@PathVariable Long id){

        Optional<AddressDTO> dto = service.getById(id);

        AddressResponse response = mapper.map(dto, AddressResponse.class);

        return ResponseEntity.ok(Optional.of(response));
    }

    @PostMapping
    public ResponseEntity<AddressResponse> register(@RequestBody AddressRequest addressRequest){
        //Convert in dto
        AddressDTO dto = mapper.map(addressRequest, AddressDTO.class);

        //Save in data
        dto = service.register(dto);

        //Convert in response
        AddressResponse response = mapper.map(dto, AddressResponse.class);

        //Return Ok
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id, @RequestBody AddressRequest request){

        AddressDTO dto = mapper.map(request, AddressDTO.class);

        dto = service.update(id, dto);

        return ResponseEntity.ok(mapper.map(dto, AddressResponse.class ));

    }
}
