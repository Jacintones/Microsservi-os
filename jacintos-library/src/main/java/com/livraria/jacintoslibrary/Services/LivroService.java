package com.livraria.jacintoslibrary.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.livraria.jacintoslibrary.Model.Livro;
import com.livraria.jacintoslibrary.Model.Exceptions.ResourceNotFoundException;
import com.livraria.jacintoslibrary.Repository.LivroRepository;
import com.livraria.jacintoslibrary.Shared.LivroDTO;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    private ModelMapper mapper = new ModelMapper();

    /**
     * Método para obter todos os livros do banco,
     * converte Livro em LivroDTO 
     * @return retorna uma lista de livrosDTO
     */
    public List<LivroDTO> obterTodos(){
        //Retorna uma lista de produto model
        List<Livro> livros = livroRepository.findAll();

        return  livros.stream() 
                        .map(livro -> mapper.map(livro, LivroDTO.class))
                        .collect(Collectors.toList()); //Retorna uma lista de produtos
    }

    /**
     * Método para obter o livro pelo id informado,
     * converto um livro em DTO
     * @param id id do livro a ser procurado
     * @return um Optional de LivroDTO
     */
    public Optional<LivroDTO> obterPorId(Long id){
        if (id == null) {
            throw new ResourceNotFoundException("Id é nulo");
        }
    
        //Pegando a info do livro
        Optional<Livro> livroOptional = livroRepository.findById(id);
    
        //Verifica se o livro foi encontrado
        if (livroOptional.isEmpty()) {
            throw new ResourceNotFoundException("Livro não encontrado para o id: " + id);
        }
    
        //Converter para livroDTO
        LivroDTO dto = mapper.map(livroOptional.get(), LivroDTO.class);
    
        //Retornar o optional de DTO
        return Optional.of(dto);        
    }

    /**
     * Método para obter os livros pelo DONO
      * @param dono id do dono
     * @return retorna a lista de livros do dono
     */
    public List<LivroDTO> obterPorDono(Long dono) {
        List<Livro> livros = livroRepository.findByDono(dono);

        return livros.stream()
                .map(livro -> new ModelMapper().map(livro, LivroDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Método para adicionar livros com suas devidas regras de negócio
     * , verifico se o livro ja existe na lista, caso não existam adiciono
     * @param livrodDTO livro a ser adicionado
     * @return retorna o livro cadastrado
     */
    public LivroDTO cadastrar(LivroDTO livrodDTO){
        //Regras de negócio 
        if (livrodDTO.getAutor() != null && livrodDTO.getTitulo() != null && livrodDTO.getPreco() >=0
        && livrodDTO.getPaginas() >= 0 && livrodDTO.getIsbn() != null
        && livrodDTO.getSinopse() != null) {
                 
            //Garantir que é uma inserção
            livrodDTO.setId(null);

            //Converter para Livro para poder salvar
            Livro livro = mapper.map(livrodDTO, Livro.class);
            
            if (livro != null) {
                //Salvo no banco
                livro = livroRepository.save(livro);

                //Digo que o id do dto agora é igual ao id criado quando salvou o livro
                livrodDTO.setId(livro.getId());

                //Retorno o próprio dto
                return livrodDTO;                
            }else{
                throw new ResourceNotFoundException("Livro inválido!");
            }

    }else{
        throw new ResourceNotFoundException("Livro não atende as especificações pedidas!");
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
        Optional<Livro> livro = livroRepository.findById(id);
        
        if (livro.isEmpty()) {
            throw new ResourceNotFoundException("Livro com id: "+id+" não existe");
        }

        //Converter para Livro
        livroRepository.deleteById(id);
    }

    /**
     * Método para atualizar livros, seto o id do body para o buscado
     * @param id id procurado para atualizar
     * @param livroDTO body que vai ser atualizado
     * @return retorna o proprio DTO
     */
    public LivroDTO atualizar(Long id, LivroDTO livroDTO){
        if (id == null) {
            throw new ResourceNotFoundException("Id inválido");
        }
        
        Optional<Livro> livroBusca = livroRepository.findById(id);

        if (livroBusca.isEmpty()) {
            throw new ResourceNotFoundException("Livro com id: "+id+" não existe");
        }

        //Atualizar o livro em questão pra não mudar o id
        livroDTO.setId(id);
        
        //Converter o DTO recebido pra Livro
        Livro livro = mapper.map(livroDTO, Livro.class);
        
        if (livro != null) {
            //Substitui
            livroRepository.save(livro);

            //Retorna o livro
            return livroDTO;              
        }else{
            throw new ResourceNotFoundException("Livro inválido");
        }
    }
}
