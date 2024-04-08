package br.thiago.autenticacao.services.impl;

import br.thiago.autenticacao.models.Exceptions.ResourceNotFoundException;
import br.thiago.autenticacao.models.User;
import br.thiago.autenticacao.shared.UserDTO;
import br.thiago.autenticacao.models.Email;
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
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final JavaMailSender javaMailSender;

    private final ModelMapper modelMapper = new ModelMapper();

    public UsuarioServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Método para salvar usuários no banco de dados,
     * Gera-se primeiro uma instancia de usuario com os dados do parametro
     * @param userDTO usuario a ser salvo
     * @return retorna o usuário salvo
     */
    @Override
    public UserDTO salvar(UserDTO userDTO, Email email) {
        userDTO.setId(null);

        //Verificar se o user existe
        Optional<User> usuarioJaExiste = repository.findByEmail(userDTO.getEmail());

        //Se existe, lança uma exceção
        if (usuarioJaExiste.isPresent()){
            throw new ResourceNotFoundException("Usuário já existe");
        }

        //business rules
        if (userDTO.getEmail() == null || userDTO.getPassword() == null || userDTO.getRole() == null ){
            throw new ResourceNotFoundException("Dados inválidas");
        }

        //Criptografando a senha
        var passwordHash = passwordEncoder.encode(userDTO.getPassword());

        //Criar a instância do usuário
        User user = modelMapper.map(userDTO, User.class);

        //Setando a senha
        user.setPassword(passwordHash);

        //Salvar no banco e pegar a variável usuário atualizada com ID
        user = repository.save(user);

        //Setando o id do DTO
        userDTO.setId(user.getId());

        //Criando o e-mail
        generateEmail(email);

        //Retorna um usuárioDTO com os dados do novo usuário
        return userDTO;
    }
    @Override
    public List<UserDTO> obterTodos() {
        //Pega todos os usuários, mapea eles e converte para DTO
        return repository
                .findAll()
                .stream()
                .map(usuario -> modelMapper.map(usuario, UserDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Método para obter o usuário pelo id dele
     * @param id id a ser procurado
     * @return retorna o Optional DTO
     */
    public Optional<UserDTO> obterPorId(Long id){

        if (id == null) {
            throw new ResourceNotFoundException("Id: "+id+ " é nulo");
        }

        //Pegando o usuario pelo email
        Optional<User> usuario = repository.findById(id);

        if (usuario.isPresent()){
            //Convertendo para dto
            UserDTO dto = modelMapper.map(usuario.get(), UserDTO.class);

            //Retornando
            return Optional.of(dto);
        }

        //Retorna o optional vazio caso não esteja presetne
        return Optional.empty();
    }

    public Optional<UserDTO> getByEmail(String email){

        if (email == null) {
            throw new ResourceNotFoundException("email: "+email+ " é nulo");
        }

        //Pegando o usuario pelo email
        Optional<User> usuario = repository.findByEmail(email);

        if (usuario.isPresent()){
            //Convertendo para dto
            UserDTO dto = modelMapper.map(usuario.get(), UserDTO.class);

            //Retornando
            return Optional.of(dto);
        }

        //Retorna o optional vazio caso não esteja presetne
        return Optional.empty();
    }

    /**
     * Método para obter o id atráves do e-mail
     * @param email e-mail a ser passado
     * @return retorna o id do usuario
     */
    public Long obterIdUsuario(String email){
        //Busco pelo método do e-mail
        Optional<User> usuario = repository.findByEmail(email);

        //Verifico se está vazio, se sim, é porque o usuário não existe no banco
        if (usuario.isEmpty()){
            throw new ResourceNotFoundException("Usuário não existe no banco");
        }

        //Retorno o id do usuário
        return usuario.get().getId();
    }

    /**
     * Método para fazer atualizações de usuário
     * @param id id do usuario desejado
     * @param userDTO corpo do usuario que vai ser atualizado para o @RequestBody
     * @return retorna o próprio usuário
     */
    public UserDTO atualizar(Long id, UserDTO userDTO){
        //Regra pra verificar a validação do id
        if (id == null) {
            throw new ResourceNotFoundException("id inválido ");
        }

        //Procuro o usuário
        Optional<User> usuario = repository.findById(id);

        //Se estiver vazio, não existe esse id cadastrado no banco
        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuário com id " + id + " não encontrado!");
        }

        if (userDTO.getRole() == null || userDTO.getPassword() == null || userDTO.getName() == null){
            throw new ResourceNotFoundException("Dados inválidos");
        }

        //Setar id para o request
        userDTO.setId(id);

        //Converto o dto para o formato padrão de dados para poder salvar
        User usu = modelMapper.map(userDTO, User.class);

        //Validação simples
        if (usu == null) {
            throw new ResourceNotFoundException("Usuário inválido");
        }

        //Salvo no banco
        repository.save(usu);

        //Retorno o próprio corpo
        return userDTO;
    }

    public UserDTO updatePassword(String email, String password){
        Optional<User> userOptional = repository.findByEmail(email);

        if (userOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário com email: "+email+ " não existe");
        }

        User user = userOptional.get();

        var passwordHash = passwordEncoder.encode(password);

        user.setPassword(passwordHash);

        repository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    public Integer generateCode(String email ){
        Optional<User> userOptional = repository.findByEmail(email);

        if (userOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário com email: "+email+ " não existe");
        }
        Random rand = new Random();

        // Gerando um número aleatório de 4 dígitos
        Integer code = rand.nextInt(9000) + 1000;

        //return secret code
        return code;
    }

    public void generateEmail(Email email){
        var message = new SimpleMailMessage();
        message.setFrom("noreply@email.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        javaMailSender.send(message);
    }
}
