package br.edu.catalogocursos.api.exception;

public record ApiError(
        int status,
        String message,
        String path
) {
}
