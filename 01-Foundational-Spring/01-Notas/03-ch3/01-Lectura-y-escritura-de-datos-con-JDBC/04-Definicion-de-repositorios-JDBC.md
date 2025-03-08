## Definición de Repositorios JDBC

En esta sección, el texto explica cómo definir un repositorio JDBC para la entidad `Ingredient`. Este repositorio debe realizar las siguientes operaciones:

1. **Consultar todos los ingredientes** y devolverlos como una colección de objetos `Ingredient`.
2. **Consultar un solo ingrediente** por su ID.
3. **Guardar un ingrediente** en la base de datos.

A continuación, se detalla cómo implementar estas operaciones utilizando `JdbcTemplate`.

---

#### 1. **Definición de la Interfaz `IngredientRepository`**

Primero, se define una interfaz que describe las operaciones que el repositorio debe implementar:

```java
package tacos.data;

import java.util.Optional;
import tacos.Ingredient;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();          // Consultar todos los ingredientes
    Optional<Ingredient> findById(String id); // Consultar un ingrediente por ID
    Ingredient save(Ingredient ingredient);  // Guardar un ingrediente
}
```

Esta interfaz actúa como un contrato que define las operaciones que el repositorio debe soportar.

---

#### 2. **Implementación del Repositorio con `JdbcTemplate`**

La implementación del repositorio se realiza en la clase `JdbcIngredientRepository`, que utiliza `JdbcTemplate` para interactuar con la base de datos.

##### Estructura de la Clase

```java
package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Implementación de los métodos de la interfaz
}
```

- **`@Repository`**: Esta anotación indica que la clase es un componente de Spring que interactúa con la base de datos. Spring la detecta automáticamente y la registra como un bean en el contexto de la aplicación.
- **Inyección de `JdbcTemplate`**: El constructor recibe una instancia de `JdbcTemplate`, que Spring inyecta automáticamente. Esto permite que el repositorio use `JdbcTemplate` para ejecutar consultas SQL.

---

#### 3. **Implementación de `findAll()`**

El método `findAll()` devuelve todos los ingredientes de la base de datos:

```java
@Override
public Iterable<Ingredient> findAll() {
    return jdbcTemplate.query(
        "select id, name, type from Ingredient",  // Consulta SQL
        this::mapRowToIngredient                 // Mapeo de filas a objetos Ingredient
    );
}
```

- **`jdbcTemplate.query()`**: Ejecuta una consulta SQL y mapea cada fila del resultado a un objeto `Ingredient`.
- **`this::mapRowToIngredient`**: Es una referencia al método `mapRowToIngredient`, que se encarga de convertir una fila del `ResultSet` en un objeto `Ingredient`.

---

#### 4. **Implementación de `findById()`**

El método `findById()` busca un ingrediente por su ID:

```java
@Override
public Optional<Ingredient> findById(String id) {
    List<Ingredient> results = jdbcTemplate.query(
        "select id, name, type from Ingredient where id=?",  // Consulta SQL con parámetro
        this::mapRowToIngredient,                           // Mapeo de filas a objetos Ingredient
        id                                                  // Parámetro para el ID
    );
    return results.size() == 0 ?
        Optional.empty() :
        Optional.of(results.get(0));
}
```

- **`jdbcTemplate.query()`**: Ejecuta una consulta SQL con un parámetro (`id`).
- **`Optional`**: Devuelve un `Optional<Ingredient>` para manejar el caso en que no se encuentre ningún ingrediente con el ID especificado.

---

#### 5. **Método `mapRowToIngredient`**

Este método convierte una fila del `ResultSet` en un objeto `Ingredient`:

```java
private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
    return new Ingredient(
        row.getString("id"),                  // Obtener el ID
        row.getString("name"),                 // Obtener el nombre
        Ingredient.Type.valueOf(row.getString("type"))  // Obtener el tipo (convertido a enum)
    );
}
```

- **`ResultSet`**: Representa una fila de la base de datos.
- **`row.getString()`**: Obtiene el valor de una columna como un `String`.
- **`Ingredient.Type.valueOf()`**: Convierte el valor de la columna `type` en un valor del enum `Ingredient.Type`.

---

#### 6. **Implementación de `save()`**

El método `save()` guarda un nuevo ingrediente en la base de datos:

```java
@Override
public Ingredient save(Ingredient ingredient) {
    jdbcTemplate.update(
        "insert into Ingredient (id, name, type) values (?, ?, ?)",  // Consulta SQL
        ingredient.getId(),                                          // Parámetro 1: ID
        ingredient.getName(),                                        // Parámetro 2: Nombre
        ingredient.getType().toString()                              // Parámetro 3: Tipo (convertido a String)
    );
    return ingredient;
}
```

- **`jdbcTemplate.update()`**: Ejecuta una consulta SQL de inserción o actualización.
- **Parámetros**: Los valores del objeto `Ingredient` se pasan como parámetros a la consulta SQL.

---

#### 7. **Uso de `RowMapper` Explícito (Opcional)**

Si prefieres no usar una referencia de método (`this::mapRowToIngredient`), puedes implementar un `RowMapper` explícito:

```java
@Override
public Ingredient findById(String id) {
    return jdbcTemplate.queryForObject(
        "select id, name, type from Ingredient where id=?",
        new RowMapper<Ingredient>() {
            @Override
            public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Ingredient(
                    rs.getString("id"),
                    rs.getString("name"),
                    Ingredient.Type.valueOf(rs.getString("type"))
                );
            }
        },
        id
    );
}
```

- **`RowMapper`**: Es una interfaz que define cómo mapear una fila del `ResultSet` a un objeto.

---

### Resumen

1. **Interfaz `IngredientRepository`**:
    - Define las operaciones que el repositorio debe implementar.

2. **Clase `JdbcIngredientRepository`**:
    - Implementa la interfaz usando `JdbcTemplate`.
    - Usa `@Repository` para que Spring la detecte automáticamente.

3. **Métodos principales**:
    - **`findAll()`**: Consulta todos los ingredientes.
    - **`findById()`**: Busca un ingrediente por su ID.
    - **`save()`**: Guarda un nuevo ingrediente en la base de datos.

4. **Mapeo de filas**:
    - Se realiza mediante el método `mapRowToIngredient` o un `RowMapper` explícito.

Con esto, tienes un repositorio funcional que interactúa con la base de datos usando `JdbcTemplate`. 🚀