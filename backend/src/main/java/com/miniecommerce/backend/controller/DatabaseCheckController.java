package com.miniecommerce.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller tạm thời để kiểm tra kết nối MySQL. Xóa khi đã làm xong các API thật.
@RestController
@RequestMapping("/api/db-check")
public class DatabaseCheckController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Map<String, Object> check() {
        String databaseName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        String mysqlVersion = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM connection_test");

        return Map.of(
                "status", "CONNECTED",
                "database", databaseName,
                "mysqlVersion", mysqlVersion,
                "connectionTestRows", rows
        );
    }
}