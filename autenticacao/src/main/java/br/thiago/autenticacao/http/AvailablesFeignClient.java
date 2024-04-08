//package br.thiago.autenticacao.http;
//
//import br.thiago.autenticacao.shared.AvailableDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@FeignClient(name = "store-service")
//public interface AvailablesFeignClient {
//
//    @GetMapping(path = "/available/{owner}/lista")
//    List<AvailableDTO> obterAvaliaçoes(@PathVariable Long owner);
//}
