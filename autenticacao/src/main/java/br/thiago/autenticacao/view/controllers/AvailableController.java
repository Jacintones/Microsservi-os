package br.thiago.autenticacao.view.controllers;



import br.thiago.autenticacao.services.impl.AvailableService;
import br.thiago.autenticacao.shared.AvailableDTO;
import br.thiago.autenticacao.view.models.AvailableRequest;
import br.thiago.autenticacao.view.models.AvailableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/available") // Adicionando um mapeamento base para todas as URLs neste controlador
public class AvailableController {

    @Autowired
    private AvailableService service;

    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<List<AvailableResponse>> getAll() {
        List<AvailableResponse> responses = service.getAll()
                .stream()
                .map(availableDTO -> mapper.map(availableDTO, AvailableResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<AvailableResponse>> getById(@PathVariable Long id) {
        Optional<AvailableResponse> response = service.getById(id)
                .map(availableDTO -> mapper.map(availableDTO, AvailableResponse.class));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AvailableResponse> register(@RequestBody AvailableRequest request) {
        // Convertendo a requisição para DTO
        AvailableDTO dto = mapper.map(request, AvailableDTO.class);

        // Registrando o disponível
        dto = service.register(dto);

        // Convertendo o DTO de resposta
        AvailableResponse response = mapper.map(dto, AvailableResponse.class);

        // Retornando uma resposta com sucesso
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailableResponse> update(@PathVariable Long id, @RequestBody AvailableRequest request) {
        // Convertendo a requisição para DTO
        AvailableDTO dto = mapper.map(request, AvailableDTO.class);

        // Atualizando o disponível
        dto = service.update(id, dto);

        // Convertendo o DTO de resposta
        AvailableResponse response = mapper.map(dto, AvailableResponse.class);

        // Retornando uma resposta com sucesso
        return ResponseEntity.ok(response);
    }
}
