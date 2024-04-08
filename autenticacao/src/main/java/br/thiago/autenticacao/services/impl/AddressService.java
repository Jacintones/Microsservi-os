package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.models.Address;
import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.repository.AddressRepository;
import br.thiago.autenticacao.shared.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    private final ModelMapper mapper = new ModelMapper();

    public List<AddressDTO> getAll(){
        //Converter para DTO
        return repository
                .findAll()
                .stream()
                .map(address -> mapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<AddressDTO> getById(Long id){
        //Catch the address by id
        Optional<Address> address = repository.findById(id);

        //Check if the address is null, if yes, return a exception
        if (address.isEmpty()){
            throw new ResourceNotFoundException("Endereço com id: "+id+ " não encontrado");
        }

        //Return the Optional
        return Optional.of(mapper.map(address.get(), AddressDTO.class));
    }

    public AddressDTO register(AddressDTO addressDTO){
        //It's a post
        addressDTO.setId(null);

        //convert dto in data
        Address add = mapper.map(addressDTO, Address.class);

        //save in the data
        add = repository.save(add);

        //set the id of dto
        addressDTO.setId(add.getId());

        //return dto
        return addressDTO;
    }

    public void delete(Long id){
        Optional<Address> address = repository.findById(id);

        address.ifPresent(value -> repository.delete(value));

        throw new ResourceNotFoundException("Endereço não existe!");
    }

    public AddressDTO update(Long id, AddressDTO dto){
        if (id == null) {
            throw new ResourceNotFoundException("id inválido ");
        }

        //Find the address
        Optional<Address> address = repository.findById(id);

        //if is empty, throw new exception
        if (address.isEmpty()) {
            throw new ResourceNotFoundException("Usuário com id " + id + " não encontrado!");
        }

        //the dto's id is the same for this request
        dto.setId(id);

        //convert in data original
        Address add = mapper.map(dto, Address.class);

        //save in the dateBase
        repository.save(add);

        //Return dto
        return dto;

    }
}
