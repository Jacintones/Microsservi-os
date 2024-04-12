package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.models.Book;
import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.repository.BookRepository;
import br.thiago.autenticacao.shared.BookDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private final ModelMapper mapper = new ModelMapper();

    /**
     * Método para obter todos os livros do banco,
     * converte Book em BookDTO
     * @return retorna uma lista de livrosDTO
     */
    public List<BookDTO> obterTodos(){
        //Retorna uma lista de produto model
        List<Book> books = bookRepository.findAll();

        return  books.stream()
                        .map(livro -> mapper.map(livro, BookDTO.class))
                        .collect(Collectors.toList()); //Retorna uma lista de produtos
    }

    /**
     * Método para obter o livro pelo id informado,
     * converto um livro em DTO
     * @param id id do livro a ser procurado
     * @return um Optional de BookDTO
     */
    public Optional<BookDTO> obterPorId(Long id){
        if (id == null) {
            throw new ResourceNotFoundException("Id é nulo");
        }
    
        //Pegando a info do livro
        Optional<Book> livroOptional = bookRepository.findById(id);
    
        //Verifica se o livro foi encontrado
        if (livroOptional.isEmpty()) {
            throw new ResourceNotFoundException("Book não encontrado para o id: " + id);
        }
    
        //Converter para livroDTO
        BookDTO dto = mapper.map(livroOptional.get(), BookDTO.class);
    
        //Retornar o optional de DTO
        return Optional.of(dto);        
    }

    /**
     * Método para adicionar livros com suas devidas regras de negócio
     * , verifico se o livro ja existe na lista, caso não existam adiciono
     * @param livrodDTO livro a ser adicionado
     * @return retorna o livro cadastrado
     */
    public BookDTO cadastrar(BookDTO livrodDTO){
        //Regras de negócio 
        if (livrodDTO.getAuthor() != null && livrodDTO.getTitle() != null
        && livrodDTO.getPages() >= 0 && livrodDTO.getIsbn10() != null && livrodDTO.getIsbn13() != null
        && livrodDTO.getSynopsis() != null) {

            //Garantir que é uma inserção
            livrodDTO.setId(null);

            //Converter para Book para poder salvar
            Book book = mapper.map(livrodDTO, Book.class);

            if (book != null) {
                //Salvo no banco
                book = bookRepository.save(book);

                //Digo que o id do dto agora é igual ao id criado quando salvou o book
                livrodDTO.setId(book.getId());

                //Retorno o próprio dto
                return livrodDTO;
            }else{
                throw new ResourceNotFoundException("Book inválido!");
            }

    }else{
        throw new ResourceNotFoundException("Book não atende as especificações pedidas!");
        }
    }

    /**
     * Método para deletar um livro, 
     * verifico se o id é válido, se for
     * obtenho o id pelo método, se optional for null 
     * , o livro não está cadastrado
     * @param id id a ser removido
     */
    public void deletar(Long id){
        if (id == null) {
            throw new ResourceNotFoundException("Digite um id válido");
        }

        //Retorna um livro dto o obter por id
        Optional<Book> livro = bookRepository.findById(id);
        
        if (livro.isEmpty()) {
            throw new ResourceNotFoundException("Book com id: "+id+" não existe");
        }

        //Converter para Book
        bookRepository.deleteById(id);
    }

    /**
     * Método para atualizar livros, seto o id do body para o buscado
     * @param id id procurado para atualizar
     * @param bookDTO body que vai ser atualizado
     * @return retorna o proprio DTO
     */
    public BookDTO atualizar(Long id, BookDTO bookDTO){
        if (id == null) {
            throw new ResourceNotFoundException("Id inválido");
        }
        
        Optional<Book> livroBusca = bookRepository.findById(id);

        if (livroBusca.isEmpty()) {
            throw new ResourceNotFoundException("Book com id: "+id+" não existe");
        }

        //Atualizar o book em questão pra não mudar o id
        bookDTO.setId(id);
        
        //Converter o DTO recebido pra Book
        Book book = mapper.map(bookDTO, Book.class);
        
        if (book != null) {
            //Substitui
            bookRepository.save(book);

            //Retorna o book
            return bookDTO;
        }else{
            throw new ResourceNotFoundException("Book inválido");
        }
    }

    /**
     * Método para calcular o lucro geral de um produto
     * @param id id do livro a ser calculado
     * @return retorna o lucro
     */
    public BigDecimal calculateProfit(Long id){
        //Pega o livro pelo seu id
        Optional<Book> book = bookRepository.findById(id);

        //Verifica se ta vazio
        if (book.isEmpty()){
            throw new ResourceNotFoundException("Livro com id: "+id+ " não existe");
        }

        //Converto para DTO
        BookDTO bookDTO = mapper.map(book.get(), BookDTO.class);

        //Retorno o lucro
        return bookDTO.getPrice().subtract(bookDTO.getCostPrice());
    }

    public BookDTO updateStock(Long id){
        //Procuro o livro pelo seu id
        Optional<Book> bookOptional = bookRepository.findById(id);

        //Verifico se ta vazio
        if (bookOptional.isEmpty()){
            throw new ResourceNotFoundException("Livro com id: "+id+" não encontrado");
        }

        //Variável para salvar o livro
        Book book = bookOptional.get();

        //Update no stock
        book.setStock(book.getStock() - 1);

        //Salva no banco a alteração
        bookRepository.save(book);

        //Converte para DTO
        return mapper.map(book, BookDTO.class);
    }
}
