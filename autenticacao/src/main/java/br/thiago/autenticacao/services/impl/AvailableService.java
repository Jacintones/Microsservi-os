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

    /**
     * Método para obter todas as avaliações disponíveis
     * @return retorna uma lista de availableDTO
     */
    public List<AvailableDTO> getAll(){
        //Converter para DTO
        return repository
                .findAll()
                .stream()
                .map(available -> mapper.map(available, AvailableDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Obter o endereço a partir do seu id
     * @param id id a ser buscado
     * @return retorna um optional de AvailableDTO
     */
    public Optional<AvailableDTO> getById(Long id){
        //Pega a avaliação pelo seu id
        Optional<Available> available = repository.findById(id);

        //Verifica se o optional está vazio, se sim, lança uma exceção
        if (available.isEmpty()){
            throw new ResourceNotFoundException("Endereço com id: "+id+ " não encontrado");
        }

        //Retorna o optional convertendo para DTO
        return Optional.of(mapper.map(available.get(), AvailableDTO.class));
    }

    /**
     * Método para registrar uma avaliação
     * @param availableDTO corpo da avaliação que vai ser salvo
     * @return retorna o mesmo corpo
     */
    public AvailableDTO register(AvailableDTO availableDTO){
        //Seto o id para null para garantir que é uma inserção
        availableDTO.setId(null);

        //Converter para DTO
        Available add = mapper.map(availableDTO, Available.class);

        //Salvar no repositório
        add = repository.save(add);

        //Seta o id que era nullo pro atual que foi salvo
        availableDTO.setId(add.getId());

        //Retorna o DTO
        return availableDTO;
    }

    /**
     * Método para deletar uma avaliação
     * @param id id da avaliação a ser deletado
     */
    public void delete(Long id){
        //Pega a avaliação no repositório
        Optional<Available> available = repository.findById(id);

        //Verifica se ta presente, se sim, deleta a apresentação
        available.ifPresent(value -> repository.delete(value));

        //Retorna a exceção caso não esteja presente
        throw new ResourceNotFoundException("Endereço não existe!");
    }

    /**
     * Método para atualizar a avaliação
     * @param id id da avaliação que vai no url
     * @param dto corpo que vai ser atualizado
     * @return retorna o próprio corpo
     */
    public AvailableDTO update(Long id, AvailableDTO dto){
        //Verifica se o id é nulo
        if (id == null) {
            throw new ResourceNotFoundException("id inválido ");
        }

        //Procura a avaliação pelo id
        Optional<Available> available = repository.findById(id);

        //Se estiver vazio, lança uma exceção
        if (available.isEmpty()) {
            throw new ResourceNotFoundException("Usuário com id " + id + " não encontrado!");
        }

        //seta o id do corpo para o id certo
        dto.setId(id);

        //Converte para DTO
        Available add = mapper.map(dto, Available.class);

        //Salva no repositório
        repository.save(add);

        //Retorna o dto
        return dto;
    }
}
