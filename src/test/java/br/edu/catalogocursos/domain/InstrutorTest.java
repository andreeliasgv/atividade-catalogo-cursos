package br.edu.catalogocursos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class InstrutorTest {

    @Test
    void deveAdicionarCursoEManterOsDoisLadosDaAssociacao() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina@example.com");
        Curso curso = new Curso("APIs REST com Spring Boot", 40, NivelCurso.INTERMEDIARIO);

        instrutor.adicionarCurso(curso);
        instrutor.adicionarCurso(curso);

        assertSame(instrutor, curso.getInstrutor());
        assertEquals(1, instrutor.getCursos().size());
        assertSame(curso, instrutor.getCursos().getFirst());
    }
}
