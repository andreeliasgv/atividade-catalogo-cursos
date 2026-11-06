package br.edu.catalogocursos;

import br.edu.catalogocursos.application.CursoService;
import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.domain.NivelCurso;
import br.edu.catalogocursos.repository.CursoRepository;
import br.edu.catalogocursos.repository.InstrutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
@Transactional
class AtividadeCatalogoCursosApplicationTests {

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoService cursoService;

    @Test
    void devePersistirCursoComInstrutorValido() {
        Instrutor instrutor = instrutorRepository.save(
                new Instrutor("Marina Costa", "marina.persistencia@example.com")
        );

        Curso curso = cursoService.cadastrar(
                "APIs REST com Spring Boot",
                40,
                NivelCurso.INTERMEDIARIO,
                instrutor.getId()
        );

        List<Curso> cursos = cursoRepository.findByInstrutorIdOrderByTituloAsc(instrutor.getId());

        assertNotNull(curso.getId());
        assertSame(instrutor, curso.getInstrutor());
        assertEquals(1, cursos.size());
        assertEquals("APIs REST com Spring Boot", cursos.getFirst().getTitulo());
    }
}
