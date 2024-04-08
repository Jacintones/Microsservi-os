package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.models.Sale;
import br.thiago.autenticacao.models.User;
import br.thiago.autenticacao.repository.SaleRepository;
import br.thiago.autenticacao.repository.UsuarioRepository;
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

    private final ModelMapper mapper = new ModelMapper();

    /**
     * Method to retrieve all sales.
     * @return Returns a list of sales.
     */
    public List<SaleDTO> getAll(){
        // Retrieve all sales from the repository and map them to DTOs
        return saleRepository
                .findAll()
                .stream()
                .map(sale -> mapper.map(sale, SaleDTO.class)) // Map the sale to a DTO
                .collect(Collectors.toList()); // Collect into a list
    }

    /**
     * Method to retrieve a sale by its id.
     * @param id Sale id.
     * @return Returns an Optional with the sale DTO.
     */
    public Optional<SaleDTO> getById(Long id){
        // Retrieve the sale by id
        Optional<Sale> sale = saleRepository.findById(id);

        // Check if the sale is null and throw an exception if so
        if (sale.isEmpty()){
            throw new ResourceNotFoundException("Sale with id: "+id+ " not found");
        }

        // Return the Optional containing the DTO of the sale
        return Optional.of(mapper.map(sale.get(), SaleDTO.class));
    }

    /**
     * Get the user associated with a sale.
     * @param id Sale id.
     * @return Returns an Optional with the user DTO.
     */
    public Optional<UserDTO> getOrdersByUser(Long id){
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()){
            throw new ResourceNotFoundException("Order with id : "+id+ " does not exist");
        }

        User user = sale.get().getUser();

        UserDTO dto = mapper.map(user, UserDTO.class);

        return Optional.of(dto);
    }

    /**
     * Register a new sale.
     * @param saleDTO Sale DTO to be registered.
     * @return Returns the same object with parameters.
     */
    public SaleDTO register(SaleDTO saleDTO) {
        // Check business rules
        if (saleDTO.getUser() == null || saleDTO.getOrders() == null){
            throw new ResourceNotFoundException("Invalid data");
        }

        // Set the ID to null as it's a new sale
        saleDTO.setId(null);

        // Convert DTO to data object
        Sale sale = mapper.map(saleDTO, Sale.class);

        // Save the sale in the database
        sale = saleRepository.save(sale);

        // Set the ID of the sale DTO
        saleDTO.setId(sale.getId());

        // Return the DTO of the sale
        return saleDTO;
    }

    /**
     * Method to delete a sale by its id.
     * @param id Sale id.
     */
    public void delete(Long id){
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()){
            throw new ResourceNotFoundException("Sale with id: "+id+ " does not exist");
        }

        saleRepository.deleteById(id);

        // Throw an exception indicating that the sale was successfully deleted
        throw new ResourceNotFoundException("Sale deleted successfully!");
    }

    /**
     * Method to update a sale by its id.
     * @param id Sale id.
     * @param dto Sale to be changed.
     * @return Returns the updated sale.
     */
    public SaleDTO update(Long id, SaleDTO dto){
        if (id == null) {
            throw new ResourceNotFoundException("Invalid id");
        }

        // Find the sale by id
        Optional<Sale> sale = saleRepository.findById(id);

        // If not found, throw an exception
        if (sale.isEmpty()) {
            throw new ResourceNotFoundException("Sale with id " + id + " not found!");
        }

        // Keep the same ID of the DTO for this request
        dto.setId(id);

        // Convert back to the original data object
        Sale saleEntity = mapper.map(dto, Sale.class);

        // Save in the database
        saleRepository.save(saleEntity);

        // Return the DTO
        return dto;
    }

}
