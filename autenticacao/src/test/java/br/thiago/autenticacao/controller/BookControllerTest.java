package br.thiago.autenticacao.controller;

import br.thiago.autenticacao.services.impl.BookService;
import br.thiago.autenticacao.shared.BookDTO;
import br.thiago.autenticacao.view.controllers.BookController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@WebMvcTest
public class BookControllerTest {

    @Autowired
    private BookController controller;

    @MockBean
    private BookService service;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        // Configuração do mockMvc
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void deve_retornar_200_ok_no_metodo_obter_todos() throws Exception {
        // Arrange
        List<BookDTO> books = new ArrayList<>();

        var requestBuilder = MockMvcRequestBuilders.get("/api/livros");
        when(this.service.obterTodos()).thenReturn(books);

        // Act
        this.mockMvc.perform(requestBuilder)
                // Assert
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
