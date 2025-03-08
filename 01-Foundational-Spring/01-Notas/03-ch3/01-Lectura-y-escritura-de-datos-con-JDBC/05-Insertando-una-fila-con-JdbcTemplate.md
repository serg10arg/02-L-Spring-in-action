## Insertando una Fila con `JdbcTemplate`

En esta sección, el texto explica cómo usar el método `update()` de `JdbcTemplate` para insertar datos en una base de datos. Además, se muestra cómo integrar el repositorio `JdbcIngredientRepository` en un controlador Spring y cómo simplificar un conversor de ingredientes.

---

#### 1. **Método `save()` para Insertar Datos**

El método `save()` en `JdbcIngredientRepository` utiliza `JdbcTemplate.update()` para insertar un nuevo ingrediente en la base de datos. Aquí está el código:

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

- **`jdbcTemplate.update()`**: Este método se usa para ejecutar consultas SQL que modifican la base de datos (como `INSERT`, `UPDATE` o `DELETE`).
- **Consulta SQL**: La consulta `insert into Ingredient` inserta una nueva fila en la tabla `Ingredient`.
- **Parámetros**: Los valores del objeto `Ingredient` se pasan como parámetros a la consulta SQL.

---

#### 2. **Integración en el Controlador `DesignTacoController`**

El repositorio `JdbcIngredientRepository` se inyecta en el controlador `DesignTacoController` para reemplazar los valores hardcodeados de ingredientes que se usaban en el capítulo 2.

##### Código del Controlador

```java
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
    private final IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();  // Obtener todos los ingredientes
        Type[] types = Ingredient.Type.values();                      // Obtener los tipos de ingredientes
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                filterByType(ingredients, type));  // Filtrar y agregar al modelo
        }
    }

    // Otros métodos del controlador
}
```

- **Inyección del Repositorio**: El repositorio `IngredientRepository` se inyecta en el controlador a través del constructor.
- **`addIngredientsToModel()`**: Este método obtiene todos los ingredientes de la base de datos usando `ingredientRepo.findAll()`, los filtra por tipo y los agrega al modelo para que estén disponibles en la vista.

---

#### 3. **Simplificación del Conversor `IngredientByIdConverter`**

En el capítulo 2, se creó un conversor (`IngredientByIdConverter`) que usaba un `Map` hardcodeado para convertir un ID de ingrediente en un objeto `Ingredient`. Ahora, este conversor se simplifica utilizando el repositorio.

##### Código del Conversor

```java
@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private IngredientRepository ingredientRepo;

    @Autowired
    public IngredientByIdConverter(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @Override
    public Ingredient convert(String id) {
        return ingredientRepo.findById(id).orElse(null);  // Buscar el ingrediente por ID
    }
}
```

- **Inyección del Repositorio**: El repositorio `IngredientRepository` se inyecta en el conversor.
- **Método `convert()`**: Usa `ingredientRepo.findById(id)` para buscar un ingrediente por su ID. Si no se encuentra, devuelve `null`.

---

#### 4. **Creación de la Tabla `Ingredient`**

Antes de ejecutar la aplicación, es necesario crear la tabla `Ingredient` en la base de datos y poblarla con algunos datos iniciales. Esto se puede hacer mediante un script SQL o programáticamente.

##### Ejemplo de Script SQL

```sql
CREATE TABLE Ingredient (
    id   VARCHAR(4)  NOT NULL PRIMARY KEY,
    name VARCHAR(25) NOT NULL,
    type VARCHAR(10) NOT NULL
);

INSERT INTO Ingredient (id, name, type) VALUES ('FLTO', 'Flour Tortilla', 'WRAP');
INSERT INTO Ingredient (id, name, type) VALUES ('COTO', 'Corn Tortilla', 'WRAP');
INSERT INTO Ingredient (id, name, type) VALUES ('GRBF', 'Ground Beef', 'PROTEIN');
INSERT INTO Ingredient (id, name, type) VALUES ('CARN', 'Carnitas', 'PROTEIN');
INSERT INTO Ingredient (id, name, type) VALUES ('TMTO', 'Diced Tomatoes', 'VEGGIES');
INSERT INTO Ingredient (id, name, type) VALUES ('LETC', 'Lettuce', 'VEGGIES');
INSERT INTO Ingredient (id, name, type) VALUES ('CHED', 'Cheddar', 'CHEESE');
INSERT INTO Ingredient (id, name, type) VALUES ('JACK', 'Monterrey Jack', 'CHEESE');
INSERT INTO Ingredient (id, name, type) VALUES ('SLSA', 'Salsa', 'SAUCE');
INSERT INTO Ingredient (id, name, type) VALUES ('SRCR', 'Sour Cream', 'SAUCE');
```

- **Tabla `Ingredient`**: Define la estructura de la tabla con columnas para `id`, `name` y `type`.
- **Datos Iniciales**: Inserta algunos ingredientes de ejemplo en la tabla.

---

#### 5. **Ejecución de la Aplicación**

Una vez que la tabla `Ingredient` está creada y poblada con datos, puedes ejecutar la aplicación. El controlador `DesignTacoController` ahora obtendrá los ingredientes directamente de la base de datos, y el conversor `IngredientByIdConverter` usará el repositorio para buscar ingredientes por ID.

---

### Resumen

1. **Método `save()`**:
    - Usa `jdbcTemplate.update()` para insertar datos en la base de datos.
    - Es más simple que `query()` porque no requiere mapear un `ResultSet`.

2. **Integración en el Controlador**:
    - El repositorio se inyecta en `DesignTacoController`.
    - Los ingredientes se obtienen de la base de datos y se agregan al modelo.

3. **Conversor Simplificado**:
    - `IngredientByIdConverter` usa el repositorio para buscar ingredientes por ID.

4. **Creación de la Tabla**:
    - Es necesario crear y poblar la tabla `Ingredient` antes de ejecutar la aplicación.

Con estos cambios, la aplicación ahora interactúa con una base de datos real en lugar de usar datos hardcodeados. 🎉