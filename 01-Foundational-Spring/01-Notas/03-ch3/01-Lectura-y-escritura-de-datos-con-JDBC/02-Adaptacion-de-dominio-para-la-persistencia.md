### 3.1.1 Adaptación del Dominio para la Persistencia

En esta sección, el texto explica cómo adaptar las clases de dominio (en este caso, `Taco` y `TacoOrder`) para que sean compatibles con la persistencia en una base de datos relacional. Esto implica agregar campos que permitan identificar de manera única cada objeto (`id`) y campos para almacenar la fecha y hora en que se crean o modifican los objetos (`createdAt` y `placedAt`).

#### 1. **Identificador Único (`id`)**

Cuando se persisten objetos en una base de datos, es esencial que cada objeto tenga un campo que lo identifique de manera única. Este campo suele ser un **ID** (generalmente un número o un UUID). En el caso de la clase `Ingredient`, ya existe un campo `id`, pero las clases `Taco` y `TacoOrder` necesitan agregar este campo.

#### 2. **Marca de Tiempo (`createdAt` y `placedAt`)**

Además del ID, es útil almacenar la fecha y hora en que un objeto es creado o modificado. Esto puede ser útil para auditorías, seguimiento o simplemente para saber cuándo se generó un registro. Para ello, se agregan campos como `createdAt` (en `Taco`) y `placedAt` (en `TacoOrder`).

#### 3. **Modificaciones en la Clase `Taco`**

El texto muestra cómo modificar la clase `Taco` para incluir estos nuevos campos:

```java
@Data
public class Taco {
    private Long id;           // Identificador único
    private Date createdAt = new Date();  // Fecha y hora de creación
    // ... otros campos y métodos
}
```

- **`id`**: Un campo de tipo `Long` que almacenará el identificador único del `Taco`.
- **`createdAt`**: Un campo de tipo `Date` que almacenará la fecha y hora en que se crea el `Taco`. Se inicializa automáticamente con la fecha y hora actuales (`new Date()`).

El uso de la anotación `@Data` de **Lombok** evita la necesidad de escribir manualmente los métodos `getter`, `setter`, `equals`, `hashCode` y `toString`. Lombok los genera automáticamente en tiempo de compilación.

#### 4. **Modificaciones en la Clase `TacoOrder`**

De manera similar, la clase `TacoOrder` también necesita un identificador único y una marca de tiempo:

```java
@Data
public class TacoOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;           // Identificador único
    private Date placedAt;     // Fecha y hora en que se realizó el pedido
    // ... otros campos y métodos
}
```

- **`id`**: Un campo de tipo `Long` que almacenará el identificador único del `TacoOrder`.
- **`placedAt`**: Un campo de tipo `Date` que almacenará la fecha y hora en que se realizó el pedido.

La clase `TacoOrder` también implementa `Serializable`, lo que permite que los objetos de esta clase puedan ser serializados (por ejemplo, para almacenarlos en sesiones HTTP o enviarlos a través de una red).

#### 5. **Uso de Lombok**

El texto enfatiza que, gracias a Lombok, no es necesario escribir manualmente los métodos `getter` y `setter` para los nuevos campos (`id`, `createdAt`, `placedAt`). Lombok los genera automáticamente en tiempo de ejecución. Sin embargo, si no se utiliza Lombok, el desarrollador tendría que escribir estos métodos manualmente.

#### 6. **¿Por qué son necesarios estos cambios?**

- **Identificador único (`id`)**: Es esencial para identificar de manera única cada registro en la base de datos. Esto es especialmente importante para operaciones como actualizar, eliminar o buscar registros.
- **Marca de tiempo (`createdAt`, `placedAt`)**: Proporciona información adicional sobre cuándo se creó o modificó un registro, lo que puede ser útil para análisis, auditorías o simplemente para mostrar al usuario cuándo se realizó una acción.

#### 7. **Próximos Pasos**

Una vez que las clases de dominio están adaptadas para la persistencia, el siguiente paso es utilizar `JdbcTemplate` para leer y escribir estos objetos en la base de datos. Esto implica:

- **Configurar `JdbcTemplate`**: Asegurarse de que `JdbcTemplate` esté correctamente configurado en la aplicación Spring.
- **Escribir consultas SQL**: Utilizar `JdbcTemplate` para ejecutar consultas SQL que inserten, actualicen, eliminen o consulten registros en la base de datos.
- **Mapear resultados**: Convertir los resultados de las consultas SQL en objetos Java (como `Taco` o `TacoOrder`).

### Resumen

- **Identificador único (`id`)**: Es necesario para identificar de manera única cada objeto en la base de datos.
- **Marca de tiempo (`createdAt`, `placedAt`)**: Proporciona información sobre cuándo se creó o modificó un objeto.
- **Lombok**: Simplifica el código al generar automáticamente los métodos `getter` y `setter`.
- **Próximos pasos**: Configurar `JdbcTemplate` y escribir consultas SQL para interactuar con la base de datos.

Con estas adaptaciones, las clases de dominio están listas para ser persistidas en una base de datos utilizando Spring JDBC.