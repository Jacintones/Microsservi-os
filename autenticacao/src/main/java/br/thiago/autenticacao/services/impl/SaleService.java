package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.models.Sale;
import br.thiago.autenticacao.models.User;
import br.thiago.autenticacao.repository.SaleRepository;
import br.thiago.autenticacao.shared.SaleDTO;
import br.thiago.autenticacao.shared.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    //Fazer a transferência de dados
    private final ModelMapper mapper = new ModelMapper();

    /**
     * Método para obter todas as vendas
     * @return Retorna uma lista de vendas
     */
    public List<SaleDTO> getAll(){
        // Recupera todas as vendas do repositório e as mapeia para DTOs
        return saleRepository
                .findAll()
                .stream()
                .map(sale -> mapper.map(sale, SaleDTO.class)) // Mapeia a venda para um DTO
                .collect(Collectors.toList()); // Coleta em uma lista
    }

    /**
     * Método para obter uma venda pelo seu id.
     * @param id Id da venda.
     * @return Retorna um Optional com o DTO da venda.
     */
    public Optional<SaleDTO> getById(Long id){
        // Recupera a venda pelo id
        Optional<Sale> sale = saleRepository.findById(id);

        // Verifica se a venda é nula e lança uma exceção se for
        if (sale.isEmpty()){
            throw new ResourceNotFoundException("Venda com id: "+id+ " não encontrada");
        }

        // Retorna o Optional contendo o DTO da venda
        return Optional.of(mapper.map(sale.get(), SaleDTO.class));
    }

    /**
     * Obter o usuário associado a uma venda.
     * @param id Id da venda.
     * @return Retorna um Optional com o DTO do usuário.
     */
    public Optional<UserDTO> getOrdersByUser(Long id){
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()){
            throw new ResourceNotFoundException("Pedido com id : "+id+ " não existe");
        }

        User user = sale.get().getUser();

        UserDTO dto = mapper.map(user, UserDTO.class);

        return Optional.of(dto);
    }

    /**
     * Registrar uma nova venda.
     * @param saleDTO DTO da venda a ser registrada.
     * @return Retorna o mesmo objeto com parâmetros.
     */
    public SaleDTO register(SaleDTO saleDTO) {
        // Verifica as regras de negócio
        if (saleDTO.getUser() == null || saleDTO.getOrders() == null){
            throw new ResourceNotFoundException("Dados inválidos");
        }

        // Define o ID como nulo, pois é uma nova venda
        saleDTO.setId(null);

        // Converte DTO para objeto de dados
        Sale sale = mapper.map(saleDTO, Sale.class);

        // Salva a venda no banco de dados
        sale = saleRepository.save(sale);

        // Define o ID do DTO da venda
        saleDTO.setId(sale.getId());

        // Retorna o DTO da venda
        return saleDTO;
    }

    /**
     * Método para excluir uma venda pelo seu id.
     * @param id Id da venda.
     */
    public void delete(Long id){
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()){
            throw new ResourceNotFoundException("Venda com id: "+id+ " não existe");
        }

        saleRepository.deleteById(id);

        // Lança uma exceção indicando que a venda foi excluída com sucesso
        throw new ResourceNotFoundException("Venda excluída com sucesso!");
    }

    /**
     * Método para atualizar uma venda pelo seu id.
     * @param id Id da venda.
     * @param dto Venda a ser alterada.
     * @return Retorna a venda atualizada.
     */
    public SaleDTO update(Long id, SaleDTO dto){
        if (id == null) {
            throw new ResourceNotFoundException("Id inválido");
        }

        // Encontra a venda pelo id
        Optional<Sale> sale = saleRepository.findById(id);

        // Se não encontrado, lança uma exceção
        if (sale.isEmpty()) {
            throw new ResourceNotFoundException("Venda com id " + id + " não encontrada!");
        }

        // Mantém o mesmo ID do DTO para esta solicitação
        dto.setId(id);

        // Converte de volta para o objeto de dados original
        Sale saleEntity = mapper.map(dto, Sale.class);

        // Salva no banco de dados
        saleRepository.save(saleEntity);

        // Retorna o DTO
        return dto;
    }

}
