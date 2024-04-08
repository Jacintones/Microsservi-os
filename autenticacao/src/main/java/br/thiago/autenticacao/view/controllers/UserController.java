package br.thiago.autenticacao.view.controllers;

import br.thiago.autenticacao.shared.UserDTO;
import br.thiago.autenticacao.models.Email;
import br.thiago.autenticacao.services.impl.UsuarioServiceImpl;
import br.thiago.autenticacao.view.models.UserRequest;
import br.thiago.autenticacao.view.models.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;

import static br.thiago.autenticacao.util.UploadUtil.fazerUploadImagem;


@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    @Autowired
    private UsuarioServiceImpl service;
    private final ModelMapper modelMapper = new ModelMapper();

    private final Path imageDirectory = Paths.get("C:\\Users\\thiag\\OneDrive\\Documentos\\GitHub\\Microsservices\\autenticacao\\src\\main\\resources\\static\\img-uploads");

    @GetMapping("/lista")
    public ResponseEntity<List<UserResponse>> obterTodos(){
        List<UserDTO> userDTOS = service.obterTodos();

        List<UserResponse> response = userDTOS
                .stream()
                .map(usuario -> modelMapper
                        .map(usuario, UserResponse.class))
                .toList();



        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Optional<UserResponse>> obterPorId(@PathVariable Long id){

        //Pegando o dto pelo email
        Optional<UserDTO> dto = service.obterPorId(id);

        //Convertendo para response
        UserResponse userResponse = modelMapper.map(dto.get(), UserResponse.class);

        //Retornando o response entity
        return new ResponseEntity<>(Optional.of(userResponse), HttpStatus.OK);
    }
    /**
     * Método para salvar usuários
     * @param userRequest usuário a ser salvo
     * @return
     */
    @PostMapping
    public ResponseEntity<UserResponse> salvar(@RequestBody UserRequest userRequest) {

        //Convertendo o request pra dto
        UserDTO userDTO = modelMapper.map(userRequest, UserDTO.class);

        Email email = new Email(userRequest.getEmail(), "Conta criada" ,
         "Conta Criada com sucesso, seja Bem-vindo(a)");
         
        //Salvando no banco
        service.salvar(userDTO, email);

        //Convertendo para response
        UserResponse response = modelMapper.map(userDTO, UserResponse.class);

        //Retornando o Response Entity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{email}")
    public Long obterIdUsuario(@PathVariable String email){
       return service.obterIdUsuario(email);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UserResponse> atualizar(@PathVariable Long id, @RequestBody UserRequest entity) {
        
        //Converter de request para dto
        UserDTO dto = modelMapper.map(entity, UserDTO.class);

        dto = service.atualizar(id, dto);

        return new ResponseEntity<>(modelMapper.map(dto, UserResponse.class), HttpStatus.OK);
    }

    @PutMapping("/imagem/{id}")
    public ResponseEntity<?> atualizarImagem(@PathVariable Long id, @RequestParam("file") MultipartFile imagem){
        
        Optional<UserDTO> usuario = service.obterPorId(id);

        if (usuario.isEmpty()){
            throw new RuntimeException("Usuário com id: "+id+ " não encontrado");
        }

        if (fazerUploadImagem(imagem)) {
            usuario.get().setImage(imagem.getOriginalFilename());

            service.atualizar(id, usuario.get());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/imagem/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws MalformedURLException {
        // Crie o caminho completo para a imagem
        Path imagePath = imageDirectory.resolve(imageName);
        Resource imageResource = new UrlResource(imagePath.toUri());

        if (imageResource.exists() || imageResource.isReadable()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageResource);
        } else {
            // Se a imagem não existir ou não for legível, retorne uma resposta de erro
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{email}")
    public ResponseEntity<Integer> generateCode(@PathVariable String email){
        //Get the user by email
        Optional<UserDTO> dto = service.getByEmail(email);

        //Generate code
        Integer num = service.generateCode(email);

        //Create e-mail
        Email mail = new Email(dto.get().getEmail(), "Alterar senha", num.toString());

        //Generate e-mail
        service.generateEmail(mail);

        //Return the code
        return ResponseEntity.ok(num);
    }

    @PutMapping("/update/password/{email}")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable String email, @RequestBody String password){
        Optional<UserDTO> dto = service.getByEmail(email);

        UserDTO userDTO = dto.get();

        userDTO = service.updatePassword(email, password);

        UserResponse response = modelMapper.map(userDTO, UserResponse.class);

        return ResponseEntity.ok(response);

    }


}
