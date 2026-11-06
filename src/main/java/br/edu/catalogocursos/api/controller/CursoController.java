package br.edu.catalogocursos.api.controller;

import br.edu.catalogocursos.api.dto.CursoRequest;
import br.edu.catalogocursos.api.dto.CursoResponse;
import br.edu.catalogocursos.api.mapper.CursoMapper;
import br.edu.catalogocursos.application.CursoService;
import br.edu.catalogocursos.domain.Curso;
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

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final CursoMapper cursoMapper;

    public CursoController(CursoService cursoService, CursoMapper cursoMapper) {
        this.cursoService = cursoService;
        this.cursoMapper = cursoMapper;
    }

    @PostMapping
    public ResponseEntity<CursoResponse> cadastrar(@Valid @RequestBody CursoRequest request) {
        Curso curso = cursoService.cadastrar(
                request.titulo(),
                request.cargaHoraria(),
                request.nivel(),
                request.instrutorId()
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(curso.getId())
                .toUri();
        return ResponseEntity.created(location).body(cursoMapper.toResponse(curso));
    }

    @GetMapping("/{id}")
    public CursoResponse buscarPorId(@PathVariable Long id) {
        return cursoMapper.toResponse(cursoService.buscarPorId(id));
    }
}
