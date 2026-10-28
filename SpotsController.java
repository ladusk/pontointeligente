

package com.pontointeligente.ponto_inteligente;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotsController {

    private final JdbcTemplate jdbcTemplate;

    public SpotsController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/v1/spots")
    public List<Map<String, Object>> listarSpots() {
        String sql = """
            SELECT id, nome, descricao, endereco,
                   criado_por, data_cadastro
            FROM pontos
            ORDER BY id
            """;

        return jdbcTemplate.queryForList(sql);
    }
}
