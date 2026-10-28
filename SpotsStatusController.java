
package com.pontointeligente.ponto_inteligente;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/spots")
public class SpotsStatusController {

    private final JdbcTemplate jdbcTemplate;

    public SpotsStatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PatchMapping("/{id}/status")
    public Map<String, Object> atualizarStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> dados) {

        String status = dados.get("status");

        if (status == null ||
                !status.matches("LIVRE|OCUPADA|INDISPONIVEL")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Status inválido. Use LIVRE, OCUPADA ou INDISPONIVEL."
            );
        }

        int linhas = jdbcTemplate.update(
            "UPDATE pontos SET status = ? WHERE id = ?",
            status, id
        );

        if (linhas == 0) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Baia não encontrada."
            );
        }

        return jdbcTemplate.queryForMap(
            "SELECT * FROM pontos WHERE id = ?", id
        );
    }
}
