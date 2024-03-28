package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.http.LivrosFeignClient;
import br.thiago.autenticacao.shared.LivroDTO;
import br.thiago.autenticacao.shared.UsuarioDTO;
import br.thiago.autenticacao.models.Email;
import br.thiago.autenticacao.models.Usuario;
import br.thiago.autenticacao.repository.UsuarioRepository;
import br.thiago.autenticacao.services.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LivrosFeignClient livrosFeignClient;

    private final JavaMailSender javaMailSender;

    private final ModelMapper modelMapper = new ModelMapper();

    public UsuarioServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    /**
     * Método para salvar usuários no banco de dados,
     * Gera-se primeiro uma instancia de usuario com os dados do parametro
     * @param usuarioDTO usuario a ser salvo
     * @return retorna o usuário salvo
     */
    @Override
    public UsuarioDTO salvar(UsuarioDTO usuarioDTO, Email email) {
        usuarioDTO.setId(null);

        //Verificar se o usuario existe
        Optional<Usuario> usuarioJaExiste = repository.findByEmail(usuarioDTO.getEmail());

        //Se existe, lança uma exceção
        if (usuarioJaExiste.isPresent()){
            throw new RuntimeException("Usuário já existe");
        }

        //Criptografando a senha
        var passwordHash = passwordEncoder.encode(usuarioDTO.getSenha());

        //Criar a instância do usuário
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        //Setando a senha
        usuario.setSenha(passwordHash);

        //Salvar no banco e pegar a variável usuário atualizada com ID
        usuario = repository.save(usuario);

        //Setando o id do DTO
        usuarioDTO.setId(usuario.getId());

        var message = new SimpleMailMessage();
        message.setFrom("noreply@email.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        javaMailSender.send(message);

        //Retorna um usuárioDTO com os dados do novo usuário
        return usuarioDTO;
    }
    @Override
    public List<UsuarioDTO> obterTodos() {
        //Pega todos os usuários, mapea eles e converte para DTO
        return repository.findAll()
                .stream()
                .map(usuario -> modelMapper
                        .map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Método para obter o usuário pelo id dele
     * @param id id a ser procurado
     * @return retorna o Optional DTO
     */
    public Optional<UsuarioDTO> obterPorId(Long id){

        if (id == null) {
            throw new RuntimeException();
        }

        //Pegando o usuario pelo email
        Optional<Usuario> usuario = repository.findById(id);

        if (usuario.isPresent()){
            //Convertendo para dto
            UsuarioDTO dto = modelMapper.map(usuario.get(), UsuarioDTO.class);

            List<LivroDTO> livros = livrosFeignClient.obterLivros(id);

            //Adiciona a lista de livros na pessoa dto
            dto.setLivros(livros);

            //Retornando
            return Optional.of(dto);
        }

        return Optional.empty();
    }

    public Long obterIdUsuario(String email){
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isEmpty()){
            throw new RuntimeException("Usuário não existe no banco");
        }

        return usuario.get().getId();
    }

    public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO){

        if (id == null) {
            throw new RuntimeException("id inválido ");
        }

        Optional<Usuario> usuario = repository.findById(id);

        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário com id " + id + " não encontrado!");
        }

        //Setar id para o request
        usuarioDTO.setId(id);

        Usuario usu = modelMapper.map(usuarioDTO, Usuario.class);

        if (usu == null) {
            throw new RuntimeException("Usuário inválido");
        }

        repository.save(usu);

        return usuarioDTO;
    }
}
