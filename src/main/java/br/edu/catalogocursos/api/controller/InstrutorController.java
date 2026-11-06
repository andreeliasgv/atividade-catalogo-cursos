package br.edu.catalogocursos.api.controller;

import br.edu.catalogocursos.api.dto.CursoResumoResponse;
import br.edu.catalogocursos.api.dto.InstrutorRequest;
import br.edu.catalogocursos.api.dto.InstrutorResponse;
import br.edu.catalogocursos.api.mapper.CursoMapper;
import br.edu.catalogocursos.api.mapper.InstrutorMapper;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.application.InstrutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {

    private final InstrutorService instrutorService;
    private final InstrutorMapper instrutorMapper;
    private final CursoMapper cursoMapper;

    public InstrutorController(
            InstrutorService instrutorService,
            InstrutorMapper instrutorMapper,
            CursoMapper cursoMapper
    ) {
        this.instrutorService = instrutorService;
        this.instrutorMapper = instrutorMapper;
        this.cursoMapper = cursoMapper;
    }

    @PostMapping
    public ResponseEntity<InstrutorResponse> cadastrar(@Valid @RequestBody InstrutorRequest request) {
        Instrutor instrutor = instrutorService.cadastrar(request.nome(), request.email());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(instrutor.getId())
                .toUri();
        return ResponseEntity.created(location).body(instrutorMapper.toResponse(instrutor));
    }

    @GetMapping("/{id}/cursos")
    public List<CursoResumoResponse> listarCursos(@PathVariable Long id) {
        return instrutorService.listarCursos(id).stream()
                .map(cursoMapper::toResumoResponse)
                .toList();
    }
}
