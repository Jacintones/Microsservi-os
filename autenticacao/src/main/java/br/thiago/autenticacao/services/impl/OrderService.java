package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.models.Order;
import br.thiago.autenticacao.models.User;
import br.thiago.autenticacao.repository.OrderRepository;
import br.thiago.autenticacao.shared.OrderDTO;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    private final ModelMapper mapper = new ModelMapper();

    /**
     * Método para obter todos os pedidos
     * @return retorna uma lista de pedidos
     */
    public List<OrderDTO> getAll(){
        //Converter para DTO
        return repository
                .findAll()
                .stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Método para obter o pedido pelo seu id
     * @param id id a ser buscado na url
     * @return retorna um optional de pedido
     */
    public Optional<OrderDTO> getById(Long id){
        //Pega o pedido pelo id no repositório
        Optional<Order> order = repository.findById(id);

        //Verifica se o pedido está vazio, se sim, lança a exceção
        if (order.isEmpty()){
            throw new ResourceNotFoundException("Endereço com id: "+id+ " não encontrado");
        }

        //Retorna um optional de DTO
        return Optional.of(mapper.map(order.get(), OrderDTO.class));
    }

    /**
     * Método para registrar um pedido
     * @param orderDTO corpo do pedido
     * @return retorna o próprio corpo
     */
    public OrderDTO register(OrderDTO orderDTO){
        //É um post, então para garantir isso, seto o id para null
        orderDTO.setId(null);

        //Regras de negócio
        if (orderDTO.getAmount() == null || orderDTO.getDate() == null || orderDTO.getUser() == null){
            throw new ResourceNotFoundException("Dados inválidos");
        }

        //Converto para DTO
        Order order = mapper.map(orderDTO, Order.class);

        //Salvo no repositório
        order = repository.save(order);

        //Seto o id para o salvo
        orderDTO.setId(order.getId());

        //Retorna o dto
        return orderDTO;
    }

    /**
     * Método para deletar algum pedido
     * @param id do pedido a ser deletado
     */
    public void delete(Long id) {
        //Pega o pedido pelo id dele
        Optional<Order> orderOptional = repository.findById(id);

        //Se tiver vazio, lança uma exceção
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Pedido com id: " + id + " não existe");
        }

        //Deleta no banco
        repository.deleteById(id);
    }

    /**
     * Método para atualizar um pedido
     * @param id id a ser atualizado
     * @param dto corpo que vai ser atualizado
     * @return retorna o dto
     */
    public OrderDTO update(Long id, OrderDTO dto){
        if (id == null) {
            throw new ResourceNotFoundException("id inválido ");
        }

        //Procuro o usuário
        Optional<Order> orderOptional = repository.findById(id);

        //Se estiver vazio, não existe esse id cadastrado no banco
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Pedido com id " + id + " não encontrado!");
        }

        //Setar id para o request
        dto.setId(id);

        //Converto o dto para o formato padrão de dados para poder salvar
        Order order = mapper.map(dto, Order.class);

        //Validação simples
        if (order == null) {
            throw new ResourceNotFoundException("Pedido inválido");
        }

        //Salvo no banco
        repository.save(order);

        //Retorno o próprio corpo
        return dto;
    }

    /**
     * Método para atualizar o status do pedido
     * @param id id do Pedido a ser atualizado
     * @return retorna o dto
     */
    public OrderDTO updateStatus(Long id){
        if (id == null) {
            throw new ResourceNotFoundException("id inválido ");
        }

        //Procuro o usuário
        Optional<Order> orderOptional = repository.findById(id);

        //Se estiver vazio, não existe esse id cadastrado no banco
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Pedido com id " + id + " não encontrado!");
        }

        //Salvo o pedido em uma variável
        Order order = orderOptional.get();

        //Atualiza o status
        order.setComplet(true);

        //Salva no repositório
        repository.save(order);

        //Retorna o dto
        return mapper.map(order, OrderDTO.class);

    }

}
