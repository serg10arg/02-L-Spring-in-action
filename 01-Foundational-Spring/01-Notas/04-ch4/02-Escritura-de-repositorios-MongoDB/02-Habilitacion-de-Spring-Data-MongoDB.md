El texto explica cómo habilitar **Spring Data MongoDB** en un proyecto Spring Boot, incluyendo la configuración de una base de datos MongoDB local o remota, y el uso de una base de datos MongoDB embebida para desarrollo y pruebas. A continuación, te lo explico de manera clara y detallada:

---

### 1. **Habilitar Spring Data MongoDB**

Para usar Spring Data MongoDB en un proyecto Spring Boot, debes agregar la dependencia correspondiente en tu archivo de configuración de Maven o Gradle. Spring Data MongoDB ofrece dos tipos de dependencias:

- **No reactiva**: Para aplicaciones tradicionales (no reactivas).
- **Reactiva**: Para aplicaciones reactivas (se cubrirá en el capítulo 13).

En este caso, se utiliza la dependencia no reactiva.

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

### 2. **Configurar MongoDB en Spring Boot**

Spring Data MongoDB asume, por defecto, que tienes un servidor MongoDB ejecutándose localmente en el puerto **27017**. Si no tienes MongoDB instalado, puedes usar **Docker** para levantar una instancia rápidamente.

#### Ejemplo de configuración con Docker:
```bash
docker run -p 27017:27017 -d mongo:latest
```

- **`-p 27017:27017`**: Expone el puerto 27017 en el host.
- **`mongo:latest`**: Usa la imagen oficial de MongoDB.

---

### 3. **Usar una base de datos MongoDB embebida**

Para desarrollo y pruebas, puedes usar una base de datos MongoDB embebida (en memoria) en lugar de un servidor MongoDB real. Esto es similar a usar H2 en aplicaciones con bases de datos relacionales.

#### Dependencia Maven para MongoDB embebido (Flapdoodle):
```xml
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
    <!-- <scope>test</scope> -->
</dependency>
```

- **`de.flapdoodle.embed.mongo`**: Proporciona una instancia de MongoDB embebida.
- **`<scope>test</scope>`**: Opcionalmente, puedes limitar esta dependencia al ámbito de pruebas.

#### Nota:
- Los datos en una base de datos embebida se pierden al reiniciar la aplicación.
- Esta opción es ideal para desarrollo y pruebas, pero no para producción.

---

### 4. **Configuración de MongoDB en producción**

En un entorno de producción, es probable que MongoDB no esté ejecutándose localmente. En ese caso, debes configurar la conexión a MongoDB en el archivo `application.properties` o `application.yml`.

#### Ejemplo de configuración en `application.yml`:
```yaml
spring:
  data:
    mongodb:
      host: mongodb.tacocloud.com
      port: 27017
      username: tacocloud
      password: s3cr3tp455w0rd
      database: tacoclouddb
```

#### Explicación de las propiedades:
- **`host`**: El nombre del host donde se ejecuta MongoDB (por defecto: `localhost`).
- **`port`**: El puerto en el que MongoDB escucha (por defecto: `27017`).
- **`username`**: El nombre de usuario para acceder a una base de datos MongoDB segura.
- **`password`**: La contraseña para acceder a una base de datos MongoDB segura.
- **`database`**: El nombre de la base de datos (por defecto: `test`).

---

### 5. **Resumen de la configuración**

1. **Agrega la dependencia** `spring-boot-starter-data-mongodb`.
2. **Configura MongoDB**:
    - Usa Docker para levantar una instancia local (`docker run -p 27017:27017 -d mongo:latest`).
    - O usa una base de datos embebida agregando `de.flapdoodle.embed.mongo`.
3. **Configura la conexión** en `application.yml` o `application.properties` para producción.

---

### 6. **Anotar entidades para persistencia en MongoDB**

Una vez configurado Spring Data MongoDB, el siguiente paso es anotar tus clases de dominio para que puedan ser persistidas como documentos en MongoDB. Spring Data MongoDB proporciona anotaciones como `@Document`, `@Id`, y `@Field` para este propósito.

#### Ejemplo de una entidad `Ingredient`:
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

- **`@Document(collection = "ingredients")`**: Indica que la clase se mapea a la colección `ingredients` en MongoDB.
- **`@Id`**: Marca el campo `id` como el identificador único del documento.
- **Lombok**: Las anotaciones de Lombok (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`) generan automáticamente getters, setters, constructores y otros métodos.

---

### 7. **Próximos pasos**

Una vez configurado Spring Data MongoDB y mapeadas las entidades, puedes:
1. **Crear repositorios**: Extender `MongoRepository` para interactuar con MongoDB.
2. **Implementar consultas personalizadas**: Usar métodos de repositorio o la anotación `@Query`.
3. **Explorar características avanzadas**: Como índices, agregaciones y transacciones.

---

### 8. **Ejemplo completo**

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

En resumen, Spring Data MongoDB simplifica la integración de MongoDB en aplicaciones Spring Boot. Solo necesitas agregar la dependencia, configurar la conexión y anotar tus entidades para que puedan ser persistidas como documentos en MongoDB. Esto permite trabajar con MongoDB de manera eficiente y con poco código repetitivo.