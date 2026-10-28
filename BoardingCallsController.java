
package com.pontointeligente.ponto_inteligente;

import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/boarding-calls")
public class BoardingCallsController {

    private final JdbcTemplate jdbcTemplate;

    public BoardingCallsController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public Map<String, Object> criarChamada(
            @RequestBody Map<String, String> dados) {

        String placa = dados.get("placa");
        String nome = dados.get("nome");

        if (placa == null || placa.isBlank()
                || nome == null || nome.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe a placa e o nome do passageiro."
            );
        }

        jdbcTemplate.update(
                "INSERT INTO boarding_calls (placa, nome) VALUES (?, ?)",
                placa, nome
        );

        return jdbcTemplate.queryForMap(
                "SELECT * FROM boarding_calls WHERE id = LAST_INSERT_ID()"
        );
    }
@PatchMapping("/{id}/cancel")
public Map<String, Object> cancelarChamada(@PathVariable int id) {

    int linhas = jdbcTemplate.update(
        "UPDATE boarding_calls SET status = 'CANCELADA' WHERE id = ? AND status = 'AGUARDANDO'",
        id
    );

    if (linhas == 0) {
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Chamada não encontrada ou já cancelada."
        );
    }

    return jdbcTemplate.queryForMap(
        "SELECT * FROM boarding_calls WHERE id = ?", id
    );
}
}
