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

    public List<OrderDTO> getAll(){
        //Converter para DTO
        return repository
                .findAll()
                .stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> getById(Long id){
        //Catch the address by id
        Optional<Order> order = repository.findById(id);

        //Check if the address is null, if yes, return a exception
        if (order.isEmpty()){
            throw new ResourceNotFoundException("Endereço com id: "+id+ " não encontrado");
        }

        //Return the Optional
        return Optional.of(mapper.map(order.get(), OrderDTO.class));
    }

    public OrderDTO register(OrderDTO orderDTO){
        //It's a post
        orderDTO.setId(null);

        //convert dto in data
        Order order = mapper.map(orderDTO, Order.class);

        //save in the data
        order = repository.save(order);

        //set the id of dto
        orderDTO.setId(order.getId());

        //return dto
        return orderDTO;
    }

    public void delete(Long id) {
        Optional<Order> orderOptional = repository.findById(id);

        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Pedido com id: " + id + " não existe");
        }

        repository.deleteById(id);
    }


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

        Order order = orderOptional.get();

        order.setComplet(true);

        repository.save(order);

        OrderDTO dto = mapper.map(order, OrderDTO.class);

        return dto;

    }

}
