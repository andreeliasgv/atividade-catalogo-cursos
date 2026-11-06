package br.edu.catalogocursos.api.mapper;

import br.edu.catalogocursos.api.dto.InstrutorResponse;
import br.edu.catalogocursos.domain.Instrutor;
import org.springframework.stereotype.Component;

@Component
public class InstrutorMapper {

    public InstrutorResponse toResponse(Instrutor instrutor) {
        return new InstrutorResponse(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail()
        );
    }
}
