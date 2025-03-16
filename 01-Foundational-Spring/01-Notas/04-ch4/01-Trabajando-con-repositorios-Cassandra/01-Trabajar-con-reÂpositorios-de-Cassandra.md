El texto introduce **Cassandra**, una base de datos NoSQL, y explica cómo **Spring Data Cassandra** facilita la integración de Cassandra en aplicaciones Spring Boot. A continuación, te lo explico de manera clara y detallada:

---

### 1. **¿Qué es Cassandra?**

Cassandra es una base de datos NoSQL distribuida, de alto rendimiento, siempre disponible y eventualmente consistente. Algunas de sus características clave son:

- **Distribuida**: Los datos se dividen y almacenan en múltiples nodos (servidores), lo que permite escalabilidad horizontal.
- **Alto rendimiento**: Diseñada para manejar grandes volúmenes de datos y operaciones de lectura/escritura rápidas.
- **Siempre disponible**: No tiene un punto único de fallo, ya que los datos se replican en varios nodos.
- **Eventualmente consistente**: Los cambios en los datos se propagan a todos los nodos con el tiempo, pero no de manera inmediata.
- **Almacenamiento en columnas**: Los datos se organizan en filas y columnas, pero a diferencia de las bases de datos relacionales, Cassandra es un almacén de columnas particionado.

---

### 2. **Spring Data Cassandra**

Spring Data Cassandra es un módulo de Spring Data que proporciona soporte para integrar Cassandra en aplicaciones Spring Boot. Ofrece:

- **Repositorios automáticos**: Similar a Spring Data JPA, permite crear repositorios para interactuar con Cassandra sin necesidad de escribir implementaciones manuales.
- **Anotaciones de mapeo**: Proporciona anotaciones para mapear clases Java a estructuras de datos en Cassandra (como tablas y columnas).

---

### 3. **Diferencias entre Cassandra y bases de datos relacionales**

Aunque Cassandra comparte algunos conceptos con las bases de datos relacionales (como tablas y filas), hay diferencias importantes:

- **No es relacional**: No soporta operaciones JOIN ni relaciones entre tablas como en SQL.
- **Escalabilidad horizontal**: Cassandra está diseñada para escalar agregando más nodos, a diferencia de las bases de datos relacionales que suelen escalar verticalmente.
- **Modelo de consistencia**: Cassandra prioriza la disponibilidad y la tolerancia a particiones sobre la consistencia inmediata (principio CAP).

---

### 4. **Habilitar Spring Data Cassandra en un proyecto Spring Boot**

Para usar Spring Data Cassandra en un proyecto Spring Boot, debes agregar la dependencia correspondiente en tu archivo de configuración de Maven o Gradle.

#### Dependencia Maven:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-cassandra</artifactId>
</dependency>
```

#### Dependencia Gradle:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-cassandra'
```

---

### 5. **Configuración de Cassandra en Spring Boot**

Una vez agregada la dependencia, debes configurar la conexión a Cassandra en el archivo `application.properties` o `application.yml`.

#### Ejemplo de configuración en `application.properties`:
```properties
spring.data.cassandra.keyspace-name=tacocloud
spring.data.cassandra.contact-points=127.0.0.1
spring.data.cassandra.port=9042
```

- **`keyspace-name`**: Es el equivalente a una base de datos en Cassandra. Define un espacio de nombres para las tablas.
- **`contact-points`**: Es la dirección IP o nombre del host del nodo de Cassandra.
- **`port`**: El puerto en el que Cassandra escucha (por defecto, 9042).

---

### 6. **Mapeo de entidades con Spring Data Cassandra**

Spring Data Cassandra proporciona anotaciones para mapear clases Java a tablas y columnas en Cassandra. Algunas de las anotaciones más comunes son:

- **`@Table`**: Indica que la clase se mapea a una tabla en Cassandra.
- **`@PrimaryKey`**: Marca el campo como la clave primaria de la tabla.
- **`@Column`**: Mapea un campo a una columna en la tabla (opcional si el nombre del campo coincide con el de la columna).

#### Ejemplo de una entidad mapeada:
```java
package tacos;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Table("ingredients")
public class Ingredient {

    @PrimaryKey
    private final String id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

- **`@Table("ingredients")`**: Mapea la clase a la tabla `ingredients` en Cassandra.
- **`@PrimaryKey`**: Indica que el campo `id` es la clave primaria.
- **`@Column`**: No es necesario en este caso porque los nombres de los campos coinciden con los nombres de las columnas.

---

### 7. **Creación de repositorios**

Spring Data Cassandra permite crear repositorios de manera similar a Spring Data JPA. Puedes extender la interfaz `CassandraRepository` para obtener métodos CRUD básicos.

#### Ejemplo de un repositorio:
```java
package tacos.data;

import org.springframework.data.cassandra.repository.CassandraRepository;
import tacos.Ingredient;

public interface IngredientRepository extends CassandraRepository<Ingredient, String> {
}
```

- **`CassandraRepository<Ingredient, String>`**:
    - `Ingredient`: Es el tipo de la entidad.
    - `String`: Es el tipo de la clave primaria.

---

### 8. **Resumen**

- **Cassandra**: Es una base de datos NoSQL distribuida, de alto rendimiento y siempre disponible.
- **Spring Data Cassandra**: Facilita la integración de Cassandra en aplicaciones Spring Boot mediante repositorios automáticos y anotaciones de mapeo.
- **Configuración**: Agrega la dependencia `spring-boot-starter-data-cassandra` y configura la conexión en `application.properties`.
- **Mapeo de entidades**: Usa anotaciones como `@Table`, `@PrimaryKey` y `@Column` para mapear clases Java a tablas y columnas en Cassandra.
- **Repositorios**: Extiende `CassandraRepository` para crear repositorios que interactúen con Cassandra.

---

### 9. **Próximos pasos**

Una vez configurado Spring Data Cassandra, puedes comenzar a:
1. Definir más entidades y repositorios.
2. Implementar consultas personalizadas usando métodos de repositorio o la anotación `@Query`.
3. Explorar características avanzadas de Cassandra, como la configuración de replicación y consistencia.

Para más detalles, consulta la documentación oficial de Cassandra: [Apache Cassandra Documentation](http://cassandra.apache.org/doc/latest/).