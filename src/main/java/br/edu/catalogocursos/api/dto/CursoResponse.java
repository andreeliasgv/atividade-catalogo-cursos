package br.edu.catalogocursos.api.dto;

import br.edu.catalogocursos.domain.NivelCurso;

public record CursoResponse(
        Long id,
        String titulo,
        Integer cargaHoraria,
        NivelCurso nivel,
        InstrutorResumoResponse instrutor
) {
    public record InstrutorResumoResponse(Long id, String nome) {
    }
}
