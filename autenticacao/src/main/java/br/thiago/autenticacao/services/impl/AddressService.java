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

    /**
     * Método para obter todos os endereços no repositório
     * @return Retorna uma lista de AddressDTO
     */
    public List<AddressDTO> getAll(){
        // Converter para DTO
        return repository
                .findAll()
                .stream()
                .map(address -> mapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Método para obter um endereço pelo seu id
     * @param id Id do endereço
     * @return Retorna um Optional com AddressDTO
     */
    public Optional<AddressDTO> getById(Long id){
        // Buscar o endereço pelo id
        Optional<Address> address = repository.findById(id);

        // Verificar se o endereço é nulo, se sim, lançar uma exceção
        if (address.isEmpty()){
            throw new ResourceNotFoundException("Endereço com id: "+id+ " não encontrado");
        }

        // Retornar o Optional
        return Optional.of(mapper.map(address.get(), AddressDTO.class));
    }

    /**
     * Método para registrar um endereço
     * @param addressDTO Endereço que desejo inserir
     * @return Retorna o mesmo endereço
     */
    public AddressDTO register(AddressDTO addressDTO){
        // É um post
        addressDTO.setId(null);

        // Converter DTO em dados
        Address add = mapper.map(addressDTO, Address.class);

        // Salvar os dados
        add = repository.save(add);

        // Definir o ID do DTO
        addressDTO.setId(add.getId());

        // Retornar o DTO
        return addressDTO;
    }

    /**
     * Método para excluir um endereço pelo seu id
     * @param id Id do endereço
     */
    public void delete(Long id){
        Optional<Address> address = repository.findById(id);

        // Se o endereço estiver presente, excluí-lo
        address.ifPresentOrElse(value -> repository.delete(value),
                () -> {
                    throw new ResourceNotFoundException("Endereço não existe!");
                });
    }

    /**
     * Método para atualizar um endereço
     * @param id Id do endereço
     * @param dto Corpo da alteração
     * @return Retorna o DTO do endereço atualizado
     */
    public AddressDTO update(Long id, AddressDTO dto){
        if (id == null) {
            throw new ResourceNotFoundException("id inválido ");
        }

        // Encontrar o endereço
        Optional<Address> address = repository.findById(id);

        // Se estiver vazio, lançar uma exceção
        if (address.isEmpty()) {
            throw new ResourceNotFoundException("Endereço com id " + id + " não encontrado!");
        }

        // O id do DTO é o mesmo para esta solicitação
        dto.setId(id);

        // Converter em dados originais
        Address add = mapper.map(dto, Address.class);

        // Salvar no banco de dados
        repository.save(add);

        // Retornar o DTO
        return dto;
    }
}
