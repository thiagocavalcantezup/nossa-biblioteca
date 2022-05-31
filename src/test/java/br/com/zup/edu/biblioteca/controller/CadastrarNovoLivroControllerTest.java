package br.com.zup.edu.biblioteca.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.repository.LivroRepository;
import br.com.zup.edu.biblioteca.util.MensagemDeErro;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
public class CadastrarNovoLivroControllerTest {

    private static final String LIVRO_URI = "/livros";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
    }

    @Test
    void deveCadastrarUmLivroComDadosValidos() throws Exception {
        // cenário (given)
        //
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        LivroRequest livroRequest = new LivroRequest(
            "Harry Potter", "Livro de ficção.", "1234567890005", LocalDate.now().minusYears(15)
        );

        String payload = objectMapper.writeValueAsString(livroRequest);

        MockHttpServletRequestBuilder request = post(LIVRO_URI).contentType(APPLICATION_JSON)
                                                               .content(payload)
                                                               .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        mockMvc.perform(request)
               .andExpect(status().isCreated())
               .andExpect(redirectedUrlPattern(baseUrl + LIVRO_URI + "/*"));

        List<Livro> livros = livroRepository.findAll();

        assertEquals(1, livros.size());
    }

    @Test
    void naoDeveCadastrarUmLivroComDadosInvalidos() throws Exception {
        // cenário (given)
        //
        LivroRequest livroRequest = new LivroRequest(null, null, null, null);

        String payload = objectMapper.writeValueAsString(livroRequest);

        MockHttpServletRequestBuilder request = post(LIVRO_URI).contentType(APPLICATION_JSON)
                                                               .content(payload)
                                                               .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        String response = mockMvc.perform(request)
                                 .andExpect(status().isBadRequest())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString(UTF_8);

        MensagemDeErro mensagemDeErro = objectMapper.readValue(response, MensagemDeErro.class);
        List<String> mensagens = mensagemDeErro.getMensagens();

        assertEquals(4, mensagens.size());
        assertThat(
            mensagens,
            containsInAnyOrder(
                "O campo titulo não deve estar em branco",
                "O campo descricao não deve estar em branco",
                "O campo isbn não deve estar em branco", "O campo dataPublicacao não deve ser nulo"
            )
        );
    }

    @Test
    void naoDeveCadastrarUmLivroComTituloComTamanhoMaiorQue200() throws Exception {
        // cenário (given)
        //
        LivroRequest livroRequest = new LivroRequest(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " + "Mauris a varius enim. "
                    + "Proin id commodo augue, id malesuada nunc. "
                    + "In sit amet magna dictum, elementum risus quis, luctus eros. "
                    + "Morbi interdum et.",
            "Livro de ficção.", "1234567890005", LocalDate.now().minusYears(15)
        );

        String payload = objectMapper.writeValueAsString(livroRequest);

        MockHttpServletRequestBuilder request = post(LIVRO_URI).contentType(APPLICATION_JSON)
                                                               .content(payload)
                                                               .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        String response = mockMvc.perform(request)
                                 .andExpect(status().isBadRequest())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString(UTF_8);

        MensagemDeErro mensagemDeErro = objectMapper.readValue(response, MensagemDeErro.class);
        List<String> mensagens = mensagemDeErro.getMensagens();

        assertEquals(1, mensagens.size());
        assertThat(mensagens, containsInAnyOrder("O campo titulo tamanho deve ser entre 0 e 200"));
    }

    @Test
    void naoDeveCadastrarUmLivroComDescricaoComTamanhoMaiorQue2000() throws Exception {
        // cenário (given)
        //
        LivroRequest livroRequest = new LivroRequest(
            "Harry Potter",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque luctus velit "
                    + "eu dolor congue euismod. Quisque ultrices dolor vitae eleifend posuere. "
                    + "Mauris nec tellus eu sem rutrum faucibus quis sit amet felis. Proin aliquet "
                    + "mauris mi, in facilisis lorem feugiat in. Sed ut lacus gravida, vestibulum "
                    + "leo a, aliquet diam. Maecenas pellentesque non metus sit amet tempor. Donec "
                    + "imperdiet turpis tortor, ac bibendum purus tristique sit amet. Etiam id "
                    + "gravida velit. In vel consequat leo. Aliquam tristique bibendum est, a "
                    + "egestas lectus efficitur ac. Nullam suscipit elit enim, ac blandit nisi "
                    + "posuere a. Quisque pulvinar, lectus ac pellentesque semper, lorem velit "
                    + "consectetur lorem, id ornare eros lectus ut nisl. Nam nulla sem, venenatis "
                    + "sed nisl eu, aliquet suscipit ex. Aenean quam nunc, varius at rhoncus id, "
                    + "vestibulum vel libero. Aliquam interdum, ligula id sagittis rutrum, mauris "
                    + "augue congue lacus, id efficitur massa felis a ex. Vestibulum finibus "
                    + "mauris ac diam dapibus pharetra. Quisque a pretium felis. Mauris diam dui,"
                    + "euismod eu elit non, pharetra blandit sem. Vivamus aliquet elit in lacus "
                    + "blandit bibendum. Nunc viverra ante felis, id porttitor nibh elementum et. "
                    + "Vivamus sollicitudin maximus libero, pharetra maximus augue scelerisque et. "
                    + "Nulla vitae varius tellus. Pellentesque eu convallis sem. In vel "
                    + "ullamcorper nibh. Maecenas accumsan, ante a interdum feugiat, quam sem "
                    + "tempus neque, in feugiat purus urna quis massa. Quisque semper blandit "
                    + "felis. Nulla et scelerisque magna. Ut imperdiet, elit vel aliquam mollis, "
                    + "enim turpis sodales augue, sit amet luctus sem neque quis lacus. In "
                    + "volutpat luctus metus vitae facilisis. Aliquam fringilla consectetur "
                    + "feugiat. Phasellus non maximus lectus, non aliquet lorem. Nunc non neque "
                    + "arcu. Aliquam nec felis eget quam consequat varius. Morbi id convallis "
                    + "metus. Curabitur pellentesque auctor nunc. Class aptent taciti sociosqu "
                    + "ad litora torquent per conubia nostra, per inceptos himenaeos. Morbi semper "
                    + "diam quis urna blandit vivamus.",
            "1234567890005", LocalDate.now().minusYears(15)
        );

        String payload = objectMapper.writeValueAsString(livroRequest);

        MockHttpServletRequestBuilder request = post(LIVRO_URI).contentType(APPLICATION_JSON)
                                                               .content(payload)
                                                               .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        String response = mockMvc.perform(request)
                                 .andExpect(status().isBadRequest())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString(UTF_8);

        MensagemDeErro mensagemDeErro = objectMapper.readValue(response, MensagemDeErro.class);
        List<String> mensagens = mensagemDeErro.getMensagens();

        assertEquals(1, mensagens.size());
        assertThat(
            mensagens, containsInAnyOrder("O campo descricao tamanho deve ser entre 0 e 2000")
        );
    }

    @Test
    void naoDeveCadastrarUmLivroComIsbnInvalido() throws Exception {
        // cenário (given)
        //
        LivroRequest livroRequest = new LivroRequest(
            "Harry Potter", "Livro de ficção.", "1234567890000", LocalDate.now().minusYears(15)
        );

        String payload = objectMapper.writeValueAsString(livroRequest);

        MockHttpServletRequestBuilder request = post(LIVRO_URI).contentType(APPLICATION_JSON)
                                                               .content(payload)
                                                               .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        String response = mockMvc.perform(request)
                                 .andExpect(status().isBadRequest())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString(UTF_8);

        MensagemDeErro mensagemDeErro = objectMapper.readValue(response, MensagemDeErro.class);
        List<String> mensagens = mensagemDeErro.getMensagens();

        assertEquals(1, mensagens.size());
        assertThat(mensagens, containsInAnyOrder("O campo isbn ISBN inválido"));
    }

    @Test
    void naoDeveCadastrarUmLivroComDataDePublicacaoNoFuturo() throws Exception {
        // cenário (given)
        //
        LivroRequest livroRequest = new LivroRequest(
            "Harry Potter", "Livro de ficção.", "1234567890005", LocalDate.now().plusYears(15)
        );

        String payload = objectMapper.writeValueAsString(livroRequest);

        MockHttpServletRequestBuilder request = post(LIVRO_URI).contentType(APPLICATION_JSON)
                                                               .content(payload)
                                                               .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        String response = mockMvc.perform(request)
                                 .andExpect(status().isBadRequest())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString(UTF_8);

        MensagemDeErro mensagemDeErro = objectMapper.readValue(response, MensagemDeErro.class);
        List<String> mensagens = mensagemDeErro.getMensagens();

        assertEquals(1, mensagens.size());
        assertThat(
            mensagens,
            containsInAnyOrder("O campo dataPublicacao deve ser uma data no passado ou no presente")
        );
    }

}
