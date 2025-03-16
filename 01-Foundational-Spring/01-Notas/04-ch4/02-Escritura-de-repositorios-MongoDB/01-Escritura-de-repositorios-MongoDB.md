El texto introduce **MongoDB**, una base de datos NoSQL de tipo documento, y explica cómo habilitar **Spring Data MongoDB** en un proyecto Spring Boot. A continuación, te lo explico de manera clara y detallada:

---

### 1. **¿Qué es MongoDB?**

MongoDB es una base de datos NoSQL que almacena datos en formato **BSON** (Binary JSON), una representación binaria de JSON. A diferencia de las bases de datos relacionales, MongoDB no utiliza tablas ni filas; en su lugar, almacena **documentos** en **colecciones**. Cada documento es un conjunto de pares clave-valor, similar a un objeto JSON.

#### Características clave de MongoDB:
- **Documentos flexibles**: Los documentos en una colección no necesitan tener la misma estructura.
- **Escalabilidad horizontal**: MongoDB está diseñado para escalar fácilmente en un entorno distribuido.
- **Consultas potentes**: Permite realizar consultas complejas y operaciones de agregación.
- **Alto rendimiento**: Optimizado para operaciones de lectura y escritura rápidas.

---

### 2. **Habilitar Spring Data MongoDB en un proyecto Spring Boot**

Para usar Spring Data MongoDB en un proyecto Spring Boot, debes agregar la dependencia correspondiente en tu archivo de configuración de Maven o Gradle.

#### Dependencia Maven:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

#### Dependencia Gradle:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
```

---

### 3. **Configurar MongoDB en Spring Boot**

Una vez agregada la dependencia, debes configurar la conexión a MongoDB en el archivo `application.properties` o `application.yml`.

#### Ejemplo de configuración en `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/tacocloud
```

- **`spring.data.mongodb.uri`**: Especifica la URI de conexión a MongoDB. En este caso, se conecta a una instancia local en el puerto 27017 y usa la base de datos `tacocloud`.

#### Ejemplo de configuración en `application.yml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/tacocloud
```

---

### 4. **Mapeo de entidades en MongoDB**

Spring Data MongoDB proporciona anotaciones para mapear clases Java a documentos en MongoDB. Algunas de las anotaciones más comunes son:

- **`@Document`**: Indica que la clase se mapea a una colección en MongoDB.
- **`@Id`**: Marca el campo como el identificador único del documento.
- **`@Field`**: Mapea un campo a una propiedad específica en el documento (opcional si el nombre del campo coincide con el de la propiedad).

---

### 5. **Ejemplo de mapeo de la clase `Ingredient`**

Supongamos que tienes la clase `Ingredient` que deseas persistir en MongoDB. Aquí está el código adaptado:

```java
package tacos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Document(collection = "ingredients")
public class Ingredient {

    @Id
    private String id;
    private String name;
    private Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

#### Explicación de las anotaciones:
- **`@Document(collection = "ingredients")`**: Indica que la clase se mapea a la colección `ingredients` en MongoDB.
- **`@Id`**: Marca el campo `id` como el identificador único del documento.
- **Lombok**: Las anotaciones de Lombok (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`) generan automáticamente getters, setters, constructores y otros métodos.

---

### 6. **Creación de repositorios**

Spring Data MongoDB permite crear repositorios de manera similar a Spring Data JPA o Spring Data Cassandra. Puedes extender la interfaz `MongoRepository` para obtener métodos CRUD básicos.

#### Ejemplo de un repositorio para `Ingredient`:
```java
package tacos.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import tacos.Ingredient;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
}
```

- **`MongoRepository<Ingredient, String>`**:
    - `Ingredient`: Es el tipo de la entidad.
    - `String`: Es el tipo de la clave primaria (`id` en este caso).

---

### 7. **Métodos personalizados**

Si necesitas consultas específicas que no están cubiertas por los métodos de `MongoRepository`, puedes definir métodos personalizados en la interfaz del repositorio. Spring Data MongoDB interpretará el nombre del método y generará la consulta correspondiente.

#### Ejemplo de un método personalizado:
Supongamos que quieres buscar ingredientes por tipo. Puedes agregar un método personalizado en `IngredientRepository`:

```java
package tacos.data;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import tacos.Ingredient;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    List<Ingredient> findByType(Ingredient.Type type);
}
```

- **`findByType(Ingredient.Type type)`**:
    - Spring Data MongoDB interpreta este método y genera una consulta que busca todos los ingredientes donde `type` coincida con el valor proporcionado.

---

### 8. **Resumen**

- **MongoDB**: Es una base de datos NoSQL de tipo documento que almacena datos en formato BSON.
- **Spring Data MongoDB**: Facilita la integración de MongoDB en aplicaciones Spring Boot mediante repositorios automáticos y anotaciones de mapeo.
- **Configuración**: Agrega la dependencia `spring-boot-starter-data-mongodb` y configura la conexión en `application.properties` o `application.yml`.
- **Mapeo de entidades**: Usa anotaciones como `@Document`, `@Id` y `@Field` para mapear clases Java a documentos en MongoDB.
- **Repositorios**: Extiende `MongoRepository` para crear repositorios que interactúen con MongoDB.

---

### 9. **Próximos pasos**

Una vez configurado Spring Data MongoDB, puedes:
1. **Definir más entidades y repositorios**.
2. **Implementar consultas personalizadas** usando métodos de repositorio o la anotación `@Query`.
3. **Explorar características avanzadas** de MongoDB, como índices, agregaciones y transacciones.

---

### 10. **Ejemplo completo**

#### `Ingredient` (Entidad):
```java
package tacos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Document(collection = "ingredients")
public class Ingredient {

    @Id
    private String id;
    private String name;
    private Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

#### `IngredientRepository` (Repositorio):
```java
package tacos.data;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import tacos.Ingredient;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    List<Ingredient> findByType(Ingredient.Type type);
}
```

---

En resumen, Spring Data MongoDB simplifica enormemente la creación de repositorios para interactuar con MongoDB. Solo necesitas definir interfaces que extiendan `MongoRepository` y, opcionalmente, agregar métodos personalizados para consultas específicas. Esto permite trabajar con MongoDB de manera eficiente y con poco código repetitivo.