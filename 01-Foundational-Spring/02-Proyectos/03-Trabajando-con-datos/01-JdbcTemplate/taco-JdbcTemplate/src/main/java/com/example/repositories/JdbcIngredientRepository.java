package com.example.repositories;

import com.example.interfaces.IngredientRepository;
import com.example.model.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.sql.SQLException;
import java.sql.ResultSet;


@Repository
public class JdbcIngredientRepository implements IngredientRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, type FROM Ingredient",
                this::mapRowToIngredient);
    }

    @Override
    public Optional<Ingredient> findById(String id) {
        List<Ingredient> results = jdbcTemplate.query(
                "SELECT id, name, type FROM Ingredient WHERE id = ?",
                this::mapRowToIngredient,
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));

    }

    private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
        return new Ingredient(
                row.getString("id"),
                row.getString("name"),
                Ingredient.Type.valueOf(row.getString("type"))
        );
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update(
                "INSERT INTO Ingredient (id, name, type) VALUES (?,?,?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString()
        );
        return ingredient;
    }

}
