package br.thiago.autenticacao.view.controllers;

import br.thiago.autenticacao.shared.UsuarioDTO;
import br.thiago.autenticacao.models.Email;
import br.thiago.autenticacao.services.impl.UsuarioServiceImpl;
import br.thiago.autenticacao.view.models.UsuarioRequest;
import br.thiago.autenticacao.view.models.UsuarioResponse;
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
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl service;
    private final ModelMapper modelMapper = new ModelMapper();

    private final Path imageDirectory = Paths.get("C:\\Users\\thiag\\OneDrive\\Documentos\\GitHub\\Microsservices\\autenticacao\\src\\main\\resources\\static\\img-uploads");

    @GetMapping("/lista")
    public ResponseEntity<List<UsuarioResponse>> obterTodos(){
        List<UsuarioDTO> usuarioDTOS = service.obterTodos();

        List<UsuarioResponse> response = usuarioDTOS
                .stream()
                .map(usuario -> modelMapper
                        .map(usuario, UsuarioResponse.class))
                .toList();



        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Optional<UsuarioResponse>> obterPorId(@PathVariable Long id){

        //Pegando o dto pelo email
        Optional<UsuarioDTO> dto = service.obterPorId(id);

        //Convertendo para response
        UsuarioResponse usuarioResponse = modelMapper.map(dto.get(), UsuarioResponse.class);

        //Retornando o response entity
        return new ResponseEntity<>(Optional.of(usuarioResponse), HttpStatus.OK);
    }
    /**
     * Método para salvar usuários
     * @param usuarioRequest usuário a ser salvo
     * @return
     */
    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@RequestBody UsuarioRequest usuarioRequest) {

        //Convertendo o request pra dto
        UsuarioDTO usuarioDTO = modelMapper.map(usuarioRequest, UsuarioDTO.class);

        Email email = new Email(usuarioRequest.getEmail(), "Conta criada" ,
         "Conta Criada com sucesso, seja Bem-vindo(a)");
         
        //Salvando no banco
        service.salvar(usuarioDTO, email);

        //Convertendo para response
        UsuarioResponse response = modelMapper.map(usuarioDTO, UsuarioResponse.class);

        //Retornando o Response Entity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{email}")
    public Long obterIdUsuario(@PathVariable String email){
       return service.obterIdUsuario(email);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody UsuarioRequest entity) {
        
        //Converter de request para dto
        UsuarioDTO dto = modelMapper.map(entity, UsuarioDTO.class);

        dto = service.atualizar(id, dto);

        return new ResponseEntity<>(modelMapper.map(dto, UsuarioResponse.class), HttpStatus.OK);
    }

    @PutMapping("/imagem/{id}")
    public ResponseEntity<?> atualizarImagem(@PathVariable Long id, @RequestParam("file") MultipartFile imagem){
        
        Optional<UsuarioDTO> usuario = service.obterPorId(id);

        if (usuario.isEmpty()){
            throw new RuntimeException("Usuário com id: "+id+ " não encontrado");
        }

        if (fazerUploadImagem(imagem)) {
            usuario.get().setImagem(imagem.getOriginalFilename());

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
}
