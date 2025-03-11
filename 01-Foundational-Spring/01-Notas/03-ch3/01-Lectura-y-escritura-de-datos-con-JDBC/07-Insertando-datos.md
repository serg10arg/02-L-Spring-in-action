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

Claro, te explicaré paso a paso cómo funciona la lógica de este código en el contexto de una aplicación Spring Boot que utiliza JDBC para interactuar con una base de datos. Este método `save` está diseñado para persistir un objeto `TacoOrder` en la base de datos, junto con los tacos asociados a ese pedido.

---

### 1. **Anotaciones**
```java
@Override
@Transactional
```
- **`@Override`**: Indica que este método está sobrescribiendo un método de una interfaz o clase padre.
- **`@Transactional`**: Esta anotación asegura que el método se ejecute dentro de una transacción de base de datos. Si ocurre algún error durante la ejecución, todos los cambios se revertirán (rollback). Esto es especialmente útil cuando se realizan múltiples operaciones de base de datos que deben ser atómicas.

---

### 2. **Preparación de la Inserción en la Tabla `Taco_Order`**
```java
PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
    "insert into Taco_Order (delivery_name, delivery_street, delivery_city, " +
    "delivery_state, delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at) " +
    "values (?,?,?,?,?,?,?,?,?)",
    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
);
pscf.setReturnGeneratedKeys(true);  // Habilitar la obtención de la clave generada
```
- **`PreparedStatementCreatorFactory`**: Es una clase de Spring JDBC que facilita la creación de un `PreparedStatement` para ejecutar consultas SQL parametrizadas.
- **Consulta SQL**: Se define una consulta `INSERT` para insertar un nuevo registro en la tabla `Taco_Order`. Los valores se especifican como `?` (placeholders) para evitar inyecciones SQL.
- **Tipos de Datos**: Se especifican los tipos de datos de cada columna (`Types.VARCHAR` para cadenas y `Types.TIMESTAMP` para la fecha).
- **`setReturnGeneratedKeys(true)`**: Habilita la obtención de la clave primaria generada automáticamente por la base de datos (por ejemplo, un `ID` autoincremental).

---

### 3. **Establecer la Fecha del Pedido**
```java
order.setPlacedAt(new Date());
```
- Se establece la fecha y hora actual (`placed_at`) en el objeto `TacoOrder`. Esto se almacenará en la base de datos como un `TIMESTAMP`.

---

### 4. **Crear el `PreparedStatementCreator`**
```java
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
```
- **`PreparedStatementCreator`**: Es una interfaz que permite crear un `PreparedStatement` con los valores proporcionados.
- **Valores**: Se pasan los valores del objeto `TacoOrder` (como el nombre de entrega, dirección, detalles de la tarjeta de crédito, etc.) en el mismo orden en que aparecen en la consulta SQL.

---

### 5. **Ejecutar la Inserción y Obtener la Clave Generada**
```java
GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
jdbcOperations.update(psc, keyHolder);
```
- **`GeneratedKeyHolder`**: Es una clase de Spring JDBC que almacena la clave primaria generada por la base de datos después de una inserción.
- **`jdbcOperations.update()`**: Ejecuta la consulta SQL de inserción utilizando el `PreparedStatementCreator`. La clave generada se almacena en `keyHolder`.

---

### 6. **Asignar el ID Generado al Pedido**
```java
long orderId = keyHolder.getKey().longValue();
order.setId(orderId);
```
- Se obtiene la clave primaria generada (`orderId`) y se asigna al objeto `TacoOrder`. Esto es útil para mantener la consistencia entre el objeto en memoria y el registro en la base de datos.

---

### 7. **Guardar los Tacos Asociados al Pedido**
```java
List<Taco> tacos = order.getTacos();
int i = 0;
for (Taco taco : tacos) {
    saveTaco(orderId, i++, taco);
}
```
- **`order.getTacos()`**: Obtiene la lista de tacos asociados al pedido.
- **Bucle `for`**: Itera sobre cada taco y lo guarda en la base de datos utilizando el método `saveTaco`.
- **`saveTaco(orderId, i++, taco)`**: Este método (no mostrado en el código) probablemente inserta cada taco en una tabla `Taco` y lo relaciona con el pedido mediante el `orderId`. El parámetro `i++` podría usarse para asignar un índice o posición a cada taco dentro del pedido.

---

### 8. **Retornar el Pedido Guardado**
```java
return order;
```
- Finalmente, el método retorna el objeto `TacoOrder` con su ID asignado y los tacos asociados.

---

### Resumen de la Lógica
1. **Preparar la Inserción**: Se crea una consulta SQL parametrizada para insertar un nuevo pedido en la tabla `Taco_Order`.
2. **Establecer la Fecha**: Se asigna la fecha y hora actual al pedido.
3. **Ejecutar la Inserción**: Se ejecuta la consulta y se obtiene la clave primaria generada.
4. **Asignar el ID**: El ID generado se asigna al objeto `TacoOrder`.
5. **Guardar Tacos**: Cada taco asociado al pedido se guarda en la base de datos y se relaciona con el pedido.
6. **Retornar el Pedido**: Se retorna el objeto `TacoOrder` con todos los datos actualizados.

---

### Consideraciones Importantes
- **Transaccionalidad**: Gracias a la anotación `@Transactional`, si ocurre un error al guardar los tacos, toda la operación se revertirá, incluyendo la inserción del pedido.
- **Seguridad**: Los datos sensibles (como el número de tarjeta de crédito) deberían manejarse con cuidado, idealmente cifrándolos antes de almacenarlos.
- **Eficiencia**: Si hay muchos tacos en un pedido, podrías considerar optimizar el proceso de inserción (por ejemplo, usando inserciones por lotes).

Este método es un buen ejemplo de cómo combinar Spring JDBC con transacciones para manejar operaciones complejas de base de datos en una aplicación Spring Boot.

---
Este código es una implementación de un repositorio (`Repository`) en Spring Boot que utiliza JDBC para interactuar con una base de datos relacional. A continuación, te explicaré en detalle cómo funciona cada parte del código.

---

### 1. **Método `saveTaco`**
Este método guarda un objeto `Taco` en la base de datos y también guarda las referencias a los ingredientes asociados a ese taco.

#### Lógica del Método:
```java
private long saveTaco(Long orderId, int orderKey, Taco taco) {
    taco.setCreateDat(new Date());  // Establecer la fecha de creación del taco

    // Crear un PreparedStatementCreatorFactory para insertar en la tabla Taco
    PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
        "INSERT INTO Taco (name, created_at, taco_order, taco_order_key) " +
        "VALUES (?, ?, ?, ?)",
        Types.VARCHAR, Types.TIMESTAMP, Types.LONG, Types.LONG
    );
    pscf.setReturnGeneratedKeys(true);  // Habilitar la obtención de la clave generada

    // Crear un PreparedStatementCreator con los valores del taco
    PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
        Arrays.asList(
            taco.getName(),
            taco.getCreateDat(),
            orderId,
            orderKey
        )
    );

    // Ejecutar la inserción y obtener la clave generada
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcOperations.update(psc, keyHolder);

    // Asignar el ID generado al taco
    long tacoID = keyHolder.getKey().longValue();
    taco.setId(tacoID);

    // Guardar las referencias a los ingredientes del taco
    saveIngredientRefs(tacoID, taco.getIngredients());

    return tacoID;
}
```

- **`taco.setCreateDat(new Date())`**: Establece la fecha y hora actual en el objeto `Taco`.
- **`PreparedStatementCreatorFactory`**: Crea una consulta SQL parametrizada para insertar un nuevo registro en la tabla `Taco`.
- **`pscf.setReturnGeneratedKeys(true)`**: Habilita la obtención de la clave primaria generada automáticamente.
- **`PreparedStatementCreator`**: Crea un `PreparedStatement` con los valores del taco.
- **`jdbcOperations.update(psc, keyHolder)`**: Ejecuta la inserción y almacena la clave generada en `keyHolder`.
- **`saveIngredientRefs(tacoID, taco.getIngredients())`**: Guarda las referencias a los ingredientes asociados al taco.

---

### 2. **Método `saveIngredientRefs`**
Este método guarda las referencias a los ingredientes asociados a un taco en la tabla `Ingredient_Ref`.

#### Lógica del Método:
```java
private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
    int key = 0;
    for (IngredientRef ingredientRef : ingredientRefs) {
        jdbcOperations.update(
            "INSERT INTO Ingredient_Ref(ingredient, taco, taco_key) " +
            "VALUES(?, ?, ?)",
            ingredientRef.getIngredient(), tacoId, key++
        );
    }
}
```

- **Bucle `for`**: Itera sobre la lista de `IngredientRef` asociados al taco.
- **`jdbcOperations.update`**: Inserta cada referencia de ingrediente en la tabla `Ingredient_Ref`. El parámetro `key++` se utiliza para asignar un índice o posición a cada ingrediente dentro del taco.

---

### 3. **Método `findById`**
Este método busca un pedido (`TacoOrder`) por su ID y devuelve un `Optional` que puede contener el pedido si se encuentra.

#### Lógica del Método:
```java
@Override
public Optional<TacoOrder> findById(Long id) {
    try {
        TacoOrder order = jdbcOperations.queryForObject(
            "SELECT id, delivery_name, delivery_street, delivery_city, "
            + "delivery_state, delivery_zip, cc_number, cc_expiration, "
            + "cc_cvv, placed_at FROM Taco_Order WHERE id = ?",
            (row, rowNum) -> {
                TacoOrder tacoOrder = new TacoOrder();
                tacoOrder.setId(row.getLong("id"));
                tacoOrder.setDeliveryName(row.getString("delivery_name"));
                tacoOrder.setDeliveryStreet(row.getString("delivery_street"));
                tacoOrder.setDeliveryCity(row.getString("delivery_city"));
                tacoOrder.setDeliveryState(row.getString("delivery_state"));
                tacoOrder.setDeliveryZip(row.getString("delivery_zip"));
                tacoOrder.setCcNumber(row.getString("cc_number"));
                tacoOrder.setCcExpiration(row.getString("cc_expiration"));
                tacoOrder.setCcCVV(row.getString("cc_cvv"));
                tacoOrder.setPlacedAt(new Date(row.getTimestamp("placed_at").getTime()));
                tacoOrder.setTacos(findTacosByOrderId(row.getLong("id")));
                return tacoOrder;
            }, id);
        return Optional.of(order);
    } catch (IncorrectResultSizeDataAccessException e) {
        return Optional.empty();
    }
}
```

- **`jdbcOperations.queryForObject`**: Ejecuta una consulta SQL para obtener un pedido por su ID.
- **Mapeo de Resultados**: Utiliza un `RowMapper` para convertir cada fila del resultado en un objeto `TacoOrder`.
- **`findTacosByOrderId`**: Obtiene los tacos asociados al pedido.
- **`Optional.of(order)`**: Devuelve el pedido envuelto en un `Optional`.
- **`IncorrectResultSizeDataAccessException`**: Captura la excepción si no se encuentra ningún pedido con el ID proporcionado y devuelve `Optional.empty()`.

---

### 4. **Método `findTacosByOrderId`**
Este método busca los tacos asociados a un pedido por su ID.

#### Lógica del Método:
```java
private List<Taco> findTacosByOrderId(long orderId) {
    return jdbcOperations.query(
        "SELECT id, name, created_at FROM Taco "
        + "WHERE taco_order = ? ORDER BY taco_order_key",
        (row, rowNum) -> {
            Taco taco = new Taco();
            taco.setId(row.getLong("id"));
            taco.setName(row.getString("name"));
            taco.setCreateDat(new Date(row.getTimestamp("created_at").getTime()));
            taco.setIngredients(findIngredientsByTacoId(row.getLong("id")));
            return taco;
        },
        orderId);
}
```

- **`jdbcOperations.query`**: Ejecuta una consulta SQL para obtener los tacos asociados a un pedido.
- **Mapeo de Resultados**: Convierte cada fila del resultado en un objeto `Taco`.
- **`findIngredientsByTacoId`**: Obtiene los ingredientes asociados a cada taco.

---

### 5. **Método `findIngredientsByTacoId`**
Este método busca los ingredientes asociados a un taco por su ID.

#### Lógica del Método:
```java
private List<IngredientRef> findIngredientsByTacoId(long tacoId) {
    return jdbcOperations.query(
        "SELECT ingredient FROM Ingredient_Ref "
        + "WHERE taco = ? ORDER BY taco_key",
        (row, rowNum) -> {
            return new IngredientRef(row.getString("ingredient"));
        },
        tacoId);
}
```

- **`jdbcOperations.query`**: Ejecuta una consulta SQL para obtener los ingredientes asociados a un taco.
- **Mapeo de Resultados**: Convierte cada fila del resultado en un objeto `IngredientRef`.

---

### Resumen
- **`saveTaco`**: Guarda un taco en la base de datos y sus ingredientes asociados.
- **`saveIngredientRefs`**: Guarda las referencias a los ingredientes de un taco.
- **`findById`**: Busca un pedido por su ID y devuelve un `Optional`.
- **`findTacosByOrderId`**: Busca los tacos asociados a un pedido.
- **`findIngredientsByTacoId`**: Busca los ingredientes asociados a un taco.

Este código es un ejemplo de cómo utilizar Spring JDBC para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en una base de datos relacional, manteniendo un diseño limpio y modular.


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