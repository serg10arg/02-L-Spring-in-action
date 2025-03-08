## 3.1.4 Insertando Datos

En esta sección, el texto profundiza en cómo insertar datos en la base de datos utilizando `JdbcTemplate`. Aunque el ejemplo anterior con `JdbcIngredientRepository` era simple, la inserción de datos puede ser más compleja, especialmente cuando se trata de entidades relacionadas, como `TacoOrder` y `Taco`.

---

#### 1. **Repositorio para `TacoOrder`**

En el diseño de la aplicación, `TacoOrder` es la raíz de un agregado, lo que significa que los objetos `Taco` no existen fuera del contexto de un `TacoOrder`. Por lo tanto, solo necesitamos un repositorio para persistir `TacoOrder`, que a su vez persistirá los objetos `Taco` asociados.

##### Interfaz `OrderRepository`

```java
package tacos.data;

import tacos.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}
```

- **`save(TacoOrder order)`**: Este método guarda un `TacoOrder` en la base de datos, junto con los `Taco` asociados.

---

#### 2. **Clase `IngredientRef`**

Para representar la relación entre un `Taco` y sus `Ingredient`, se utiliza la clase `IngredientRef`:

```java
package tacos;

import lombok.Data;

@Data
public class IngredientRef {
    private final String ingredient;
}
```

- **`ingredient`**: Representa el ID del ingrediente que forma parte de un `Taco`.

---

#### 3. **Implementación de `JdbcOrderRepository`**

La implementación de `OrderRepository` es más compleja que la de `JdbcIngredientRepository`, ya que debe manejar la persistencia de `TacoOrder`, `Taco` y `IngredientRef`.

##### Método `save(TacoOrder order)`

```java
@Override
@Transactional
public TacoOrder save(TacoOrder order) {
    // Crear un PreparedStatementCreatorFactory para insertar en Taco_Order
    PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
        "insert into Taco_Order (delivery_name, delivery_street, delivery_city, " +
        "delivery_state, delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at) " +
        "values (?,?,?,?,?,?,?,?,?)",
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
    );
    pscf.setReturnGeneratedKeys(true);  // Habilitar la obtención de la clave generada

    // Establecer la fecha de creación del pedido
    order.setPlacedAt(new Date());

    // Crear un PreparedStatementCreator con los valores del pedido
    PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
        Arrays.asList(
            order.getDeliveryName(),
            order.getDeliveryStreet(),
            order.getDeliveryCity(),
            order.getDeliveryState(),
            order.getDeliveryZip(),
            order.getCcNumber(),
            order.getCcExpiration(),
            order.getCcCVV(),
            order.getPlacedAt()
        )
    );

    // Ejecutar la inserción y obtener la clave generada
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcOperations.update(psc, keyHolder);

    // Asignar el ID generado al pedido
    long orderId = keyHolder.getKey().longValue();
    order.setId(orderId);

    // Guardar los tacos asociados al pedido
    List<Taco> tacos = order.getTacos();
    int i = 0;
    for (Taco taco : tacos) {
        saveTaco(orderId, i++, taco);
    }

    return order;
}
```

- **`PreparedStatementCreatorFactory`**: Se utiliza para definir la consulta SQL y los tipos de datos de los parámetros.
- **`GeneratedKeyHolder`**: Permite obtener el ID generado automáticamente por la base de datos.
- **`saveTaco()`**: Guarda cada `Taco` asociado al pedido.

---

#### 4. **Método `saveTaco()`**

Este método guarda un `Taco` en la base de datos y establece la relación con el `TacoOrder`.

```java
private long saveTaco(Long orderId, int orderKey, Taco taco) {
    taco.setCreatedAt(new Date());

    // Crear un PreparedStatementCreatorFactory para insertar en Taco
    PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
        "insert into Taco (name, created_at, taco_order, taco_order_key) " +
        "values (?, ?, ?, ?)",
        Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
    );
    pscf.setReturnGeneratedKeys(true);

    // Crear un PreparedStatementCreator con los valores del taco
    PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
        Arrays.asList(
            taco.getName(),
            taco.getCreatedAt(),
            orderId,
            orderKey
        )
    );

    // Ejecutar la inserción y obtener la clave generada
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcOperations.update(psc, keyHolder);

    // Asignar el ID generado al taco
    long tacoId = keyHolder.getKey().longValue();
    taco.setId(tacoId);

    // Guardar las referencias a los ingredientes del taco
    saveIngredientRefs(tacoId, taco.getIngredients());

    return tacoId;
}
```

- **`saveIngredientRefs()`**: Guarda las relaciones entre un `Taco` y sus `Ingredient`.

---

#### 5. **Método `saveIngredientRefs()`**

Este método guarda las referencias a los ingredientes de un `Taco` en la tabla `Ingredient_Ref`.

```java
private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
    int key = 0;
    for (IngredientRef ingredientRef : ingredientRefs) {
        jdbcOperations.update(
            "insert into Ingredient_Ref (ingredient, taco, taco_key) " +
            "values (?, ?, ?)",
            ingredientRef.getIngredient(), tacoId, key++
        );
    }
}
```

- **`Ingredient_Ref`**: Esta tabla relaciona un `Taco` con sus `Ingredient`.
- **`key`**: Se utiliza para mantener el orden de los ingredientes.

---

#### 6. **Integración en `OrderController`**

Finalmente, el repositorio `OrderRepository` se inyecta en `OrderController` para guardar los pedidos.

```java
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
    private OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        orderRepo.save(order);  // Guardar el pedido en la base de datos
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
```

- **`orderRepo.save(order)`**: Guarda el pedido y sus tacos asociados en la base de datos.

---

### Resumen

1. **`OrderRepository`**:
    - Define un método `save()` para guardar `TacoOrder` y sus `Taco` asociados.

2. **`JdbcOrderRepository`**:
    - Implementa la lógica para guardar `TacoOrder`, `Taco` y `IngredientRef`.
    - Utiliza `PreparedStatementCreatorFactory` y `GeneratedKeyHolder` para manejar inserciones y obtener IDs generados.

3. **`OrderController`**:
    - Inyecta `OrderRepository` y lo utiliza para guardar pedidos.

Con esto, la aplicación Taco Cloud puede persistir pedidos completos, incluyendo tacos y sus ingredientes, en la base de datos. 🎉