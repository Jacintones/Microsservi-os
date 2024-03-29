package br.thiago.autenticacao.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {

    public static boolean fazerUploadImagem(MultipartFile imagem){

        boolean sucessoUpload = false;

        if (!imagem.isEmpty()) {
            String nomeDoArquivo = imagem.getOriginalFilename();
            try {
                // Criando o diretório para armazenar o arquivo
                String pastaUploadImagem = "C:\\Users\\thiag\\OneDrive\\Documentos\\GitHub\\Microsservices\\autenticacao\\src\\main\\resources\\static\\img-uploads";
                File dir = new File(pastaUploadImagem);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Criando o arquivo
                File serverFile = new File(dir.getAbsolutePath() + File.separator + nomeDoArquivo);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(imagem.getBytes());
                stream.close();

                System.out.println("Armazenando em: " + serverFile.getAbsolutePath());
                System.out.println("Upload do arquivo: " + nomeDoArquivo + " com sucesso");
                sucessoUpload = true;

            } catch (Exception e) {
                System.out.println("Falhou em carregar o arquivo " + nomeDoArquivo + " => " + e.getMessage());
            }
        } else {
            System.out.println("Falhou em carregar o arquivo pois ele está vazio!");
        }
        return sucessoUpload;
    }
}
