package br.edu.catalogocursos.api;

import br.edu.catalogocursos.application.CursoService;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.domain.NivelCurso;
import br.edu.catalogocursos.repository.InstrutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CatalogoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private CursoService cursoService;

    @Test
    void cadastroHttpDeInstrutorInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "A",
                                  "email": "email-invalido"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/instrutores"));
    }

    @Test
    void listagemPorInstrutorDeveRetornarSomenteSeusCursosOrdenadosPorTitulo() throws Exception {
        Instrutor primeiro = instrutorRepository.save(
                new Instrutor("Marina Costa", "marina.api@example.com")
        );
        Instrutor segundo = instrutorRepository.save(
                new Instrutor("Carlos Souza", "carlos.api@example.com")
        );

        cursoService.cadastrar("Java orientado a objetos", 60, NivelCurso.INICIANTE, primeiro.getId());
        cursoService.cadastrar("APIs REST com Spring Boot", 40, NivelCurso.INTERMEDIARIO, primeiro.getId());
        cursoService.cadastrar("Banco de dados", 30, NivelCurso.INICIANTE, segundo.getId());

        mockMvc.perform(get("/api/instrutores/{id}/cursos", primeiro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo").value("APIs REST com Spring Boot"))
                .andExpect(jsonPath("$[1].titulo").value("Java orientado a objetos"));
    }

    @Test
    void cadastroDeInstrutorValidoDeveRetornar201ELocation() throws Exception {
        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana Lima",
                                  "email": "ana.api@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void cadastroEConsultaDeCursoDevemSeguirContratoRest() throws Exception {
        Instrutor instrutor = instrutorRepository.save(
                new Instrutor("Paulo Mendes", "paulo.api@example.com")
        );

        MvcResult resultado = mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Persistência com JPA",
                                  "cargaHoraria": 32,
                                  "nivel": "INTERMEDIARIO",
                                  "instrutorId": %d
                                }
                                """.formatted(instrutor.getId())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.instrutor.id").value(instrutor.getId()))
                .andExpect(jsonPath("$.instrutor.nome").value("Paulo Mendes"))
                .andReturn();

        String location = resultado.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Persistência com JPA"))
                .andExpect(jsonPath("$.instrutor.id").value(instrutor.getId()));
    }

    @Test
    void cadastroDeCursoComInstrutorInexistenteDeveRetornar404() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Persistência com JPA",
                                  "cargaHoraria": 32,
                                  "nivel": "INTERMEDIARIO",
                                  "instrutorId": 999999
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/cursos"));
    }

    @Test
    void emailDuplicadoDeveRetornar409() throws Exception {
        String corpo = """
                {
                  "nome": "Ana Lima",
                  "email": "ana.duplicada@example.com"
                }
                """;

        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.path").value("/api/instrutores"));
    }
}
