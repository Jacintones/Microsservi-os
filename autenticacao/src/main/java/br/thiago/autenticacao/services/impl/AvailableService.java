package br.thiago.autenticacao.services.impl;



import br.thiago.autenticacao.models.Available;
import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.models.User;
import br.thiago.autenticacao.repository.AvailableRepository;
import br.thiago.autenticacao.shared.AvailableDTO;
import br.thiago.autenticacao.shared.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailableService {

    @Autowired
    private AvailableRepository repository;

    private final ModelMapper mapper = new ModelMapper();

    public List<AvailableDTO> getAll(){
        //Converter para DTO
        return repository
                .findAll()
                .stream()
                .map(available -> mapper.map(available, AvailableDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<AvailableDTO> getById(Long id){
        //Catch the available by id
        Optional<Available> available = repository.findById(id);

        //Check if the available is null, if yes, return a exception
        if (available.isEmpty()){
            throw new ResourceNotFoundException("Endereço com id: "+id+ " não encontrado");
        }

        //Return the Optional
        return Optional.of(mapper.map(available.get(), AvailableDTO.class));
    }

    public AvailableDTO register(AvailableDTO availableDTO){
        //It's a post
        availableDTO.setId(null);

        //convert dto in data
        Available add = mapper.map(availableDTO, Available.class);

        //save in the data
        add = repository.save(add);

        //set the id of dto
        availableDTO.setId(add.getId());

        //return dto
        return availableDTO;
    }

    public void delete(Long id){
        Optional<Available> available = repository.findById(id);

        available.ifPresent(value -> repository.delete(value));

        throw new ResourceNotFoundException("Endereço não existe!");
    }

    public AvailableDTO update(Long id, AvailableDTO dto){
        if (id == null) {
            throw new ResourceNotFoundException("id inválido ");
        }

        //Find the available
        Optional<Available> available = repository.findById(id);

        //if is empty, throw new exception
        if (available.isEmpty()) {
            throw new ResourceNotFoundException("Usuário com id " + id + " não encontrado!");
        }

        //the dto's id is the same for this request
        dto.setId(id);

        //convert in data original
        Available add = mapper.map(dto, Available.class);

        //save in the dateBase
        repository.save(add);

        //Return dto
        return dto;

    }
}
