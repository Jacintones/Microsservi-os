package com.livraria.jacintoslibrary.Services;

import com.livraria.jacintoslibrary.Model.Exceptions.ResourceNotFoundException;
import com.livraria.jacintoslibrary.Model.LivroLoja;
import com.livraria.jacintoslibrary.Repository.LivroLojaRepository;
import com.livraria.jacintoslibrary.Shared.LivroLojaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class LojaService {


    @Autowired
    private LivroLojaRepository livroLojaRepository;
    
    private ModelMapper mapper = new ModelMapper();
    /**
     * Método para obter os livros comerciais
     * @return
     */
    public List<LivroLojaDTO> obterTodosLivrosComerciais(){
        List<LivroLoja> livros = livroLojaRepository.findAll();

        //Converter para DTO
        return  livros.stream()
                .map(livroLoja -> mapper.map(livroLoja, LivroLojaDTO.class))
                .collect(Collectors.toList());
    }

    public LivroLojaDTO cadastroComercio(LivroLojaDTO livroDTO){
        //Regras de negócio
        if (livroDTO.getAutor() != null && livroDTO.getTitulo() != null && livroDTO.getPreco() >=0
                && livroDTO.getEstoque() >= 0 && livroDTO.getPaginas() >= 0 && livroDTO.getIsbn() != null
                && livroDTO.getSinopse() != null) {

            //Garantir que é uma inserção
            livroDTO.setId(null);

            //Converter para Livro para poder salvar
            LivroLoja livro = mapper.map(livroDTO, LivroLoja.class);

            if (livro != null) {
                //Salvo no banco
                livro = livroLojaRepository.save(livro);

                //Digo que o id do dto agora é igual ao id criado quando salvou o livro
                livroDTO.setId(livro.getId());

                //Retorno o próprio dto
                return livroDTO;
            }else{
                throw new ResourceNotFoundException("Livro inválido!");
            }

        }else{
            throw new ResourceNotFoundException("Livro não atende as especificações pedidas!");
        }
    }
    public void deletarComercio(Long id){
        if (id == null) {
            throw new ResourceNotFoundException("Digite um id válido");
        }

        //Retorna um livro dto o obter por id
        Optional<LivroLoja> livro = livroLojaRepository.findById(id);

        if (livro.isEmpty()) {
            throw new ResourceNotFoundException("Livro com id: "+id+" não existe");
        }

        //Converter para Livro
        livroLojaRepository.deleteById(id);
    }

    public LivroLojaDTO atualizar(Long id, LivroLojaDTO livroDTO){
        if (id == null) {
            throw new ResourceNotFoundException("Id inválido");
        }
        
        Optional<LivroLoja> livroBusca = livroLojaRepository.findById(id);

        if (livroBusca.isEmpty()) {
            throw new ResourceNotFoundException("Livro com id: "+id+" não existe");
        }

        //Atualizar o livro em questão pra não mudar o id
        livroDTO.setId(id);
        
        //Converter o DTO recebido pra Livro
        LivroLoja livro = mapper.map(livroDTO, LivroLoja.class);
        
        if (livro != null) {
            //Substitui
            livroLojaRepository.save(livro);

            //Retorna o livro
            return livroDTO;              
        }else{
            throw new ResourceNotFoundException("Livro inválido");
        }
    }  
}
