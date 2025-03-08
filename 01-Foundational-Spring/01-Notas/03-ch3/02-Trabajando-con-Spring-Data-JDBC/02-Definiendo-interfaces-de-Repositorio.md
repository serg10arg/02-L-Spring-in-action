## 3.2.2 Definiendo Interfaces de Repositorio

En esta sección, el texto explica cómo definir interfaces de repositorio utilizando Spring Data JDBC. La ventaja principal de Spring Data es que no necesitas escribir implementaciones manuales para los repositorios; Spring Data genera automáticamente las implementaciones en tiempo de ejecución.

---

#### 1. **Extender `Repository` o `CrudRepository`**

Para que Spring Data genere automáticamente una implementación de un repositorio, la interfaz del repositorio debe extender una de las interfaces proporcionadas por Spring Data, como `Repository` o `CrudRepository`.

##### Ejemplo con `Repository`:

```java
package tacos.data;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import tacos.Ingredient;

public interface IngredientRepository extends Repository<Ingredient, String> {
    Iterable<Ingredient> findAll();
    Optional<Ingredient> findById(String id);
    Ingredient save(Ingredient ingredient);
}
```

- **`Repository<Ingredient, String>`**:
    - **Primer parámetro (`Ingredient`)**: El tipo de objeto que se persistirá.
    - **Segundo parámetro (`String`)**: El tipo del ID del objeto.

---

#### 2. **Extender `CrudRepository`**

En lugar de extender `Repository`, es más común extender `CrudRepository`, que ya define métodos comunes como `findAll()`, `findById()`, `save()`, `delete()`, etc.

##### Ejemplo con `CrudRepository`:

```java
package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
```

- **`CrudRepository<Ingredient, String>`**:
    - Proporciona métodos CRUD (Crear, Leer, Actualizar, Eliminar) listos para usar.
    - No es necesario definir métodos como `findAll()` o `save()` manualmente, ya que `CrudRepository` ya los incluye.

---

#### 3. **Repositorio para `TacoOrder`**

De manera similar, puedes definir un repositorio para `TacoOrder` extendiendo `CrudRepository`.

##### Ejemplo de `OrderRepository`:

```java
package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
}
```

- **`CrudRepository<TacoOrder, Long>`**:
    - **Primer parámetro (`TacoOrder`)**: El tipo de objeto que se persistirá.
    - **Segundo parámetro (`Long`)**: El tipo del ID del objeto.

---

#### 4. **Ventajas de Usar `CrudRepository`**

- **Métodos predefinidos**: `CrudRepository` incluye métodos comunes como `findAll()`, `findById()`, `save()`, `delete()`, etc.
- **Menos código**: No necesitas escribir implementaciones manuales para estos métodos.
- **Mantenimiento simplificado**: Spring Data genera automáticamente las implementaciones en tiempo de ejecución.

---

#### 5. **Eliminar Implementaciones Manuales**

Dado que Spring Data genera automáticamente las implementaciones de los repositorios, ya no necesitas las clases `JdbcIngredientRepository` y `JdbcOrderRepository`. Puedes eliminarlas de tu proyecto.

##### Clases a Eliminar:
- `JdbcIngredientRepository`
- `JdbcOrderRepository`

---

#### 6. **Inyectar Repositorios en los Controladores**

Una vez definidas las interfaces de repositorio, puedes inyectarlas en los controladores y usarlas directamente.

##### Ejemplo de Inyección en `DesignTacoController`:

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
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();
        // Lógica para agregar ingredientes al modelo
    }
}
```

##### Ejemplo de Inyección en `OrderController`:

```java
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
    private final OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        orderRepo.save(order);  // Guardar el pedido usando el repositorio
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
```

---

### Resumen

1. **Definir Interfaces de Repositorio**:
    - Extiende `CrudRepository` para obtener métodos CRUD predefinidos.
    - No es necesario escribir implementaciones manuales.

2. **Eliminar Implementaciones Manuales**:
    - Las clases como `JdbcIngredientRepository` y `JdbcOrderRepository` ya no son necesarias.

3. **Inyectar Repositorios en Controladores**:
    - Usa las interfaces de repositorio directamente en los controladores.

Con Spring Data JDBC, el código es más limpio, mantenible y fácil de escribir. 🚀