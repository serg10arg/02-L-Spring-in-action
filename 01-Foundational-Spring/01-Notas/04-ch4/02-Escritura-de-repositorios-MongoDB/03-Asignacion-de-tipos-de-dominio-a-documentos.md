El texto explica cómo mapear tipos de dominio (clases Java) a documentos en **MongoDB** utilizando anotaciones específicas de **Spring Data MongoDB**. A continuación, te lo explico de manera clara y detallada:

---

### 1. **Anotaciones clave para el mapeo de documentos**

Spring Data MongoDB proporciona varias anotaciones para mapear clases Java a documentos en MongoDB. Las más comunes son:

- **`@Id`**: Designa una propiedad como el identificador único del documento.
- **`@Document`**: Declara una clase como un documento que se persistirá en MongoDB.
- **`@Field`**: Especifica el nombre del campo (y, opcionalmente, el orden) en el documento persistido.
- **`@Transient`**: Indica que una propiedad no se persistirá en MongoDB.

---

### 2. **Mapeo de la clase `Ingredient`**

La clase `Ingredient` es un ejemplo sencillo de cómo mapear una entidad a un documento en MongoDB.

#### Código de `Ingredient`:
```java
package tacos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
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
- **`@Document`**: Indica que la clase se mapea a un documento en MongoDB. Por defecto, el nombre de la colección será `ingredient` (el nombre de la clase en minúscula).
- **`@Id`**: Marca el campo `id` como el identificador único del documento.
- **Lombok**: Las anotaciones de Lombok (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`) generan automáticamente getters, setters, constructores y otros métodos.

#### Cambiar el nombre de la colección:
Si deseas que los documentos se guarden en una colección con un nombre diferente, puedes usar el atributo `collection` de `@Document`:
```java
@Document(collection = "ingredients")
```

---

### 3. **Mapeo de la clase `Taco`**

La clase `Taco` no necesita anotaciones de persistencia porque, en el modelo de dominio de Taco Cloud, `Taco` solo se persiste como parte de un agregado raíz (`TacoOrder`). Por lo tanto, `Taco` no es una entidad raíz y no necesita ser mapeada directamente a una colección en MongoDB.

#### Código de `Taco`:
```java
package tacos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Taco {

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    private Date createdAt = new Date();

    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
}
```

- **Sin anotaciones de persistencia**: `Taco` no necesita `@Document` ni `@Id` porque no es una entidad raíz.

---

### 4. **Mapeo de la clase `TacoOrder`**

La clase `TacoOrder` es el agregado raíz en el modelo de dominio, por lo que debe ser mapeada como un documento en MongoDB.

#### Código de `TacoOrder`:
```java
package tacos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class TacoOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Date placedAt = new Date();

    // Otros campos omitidos por brevedad
    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
```

#### Explicación de las anotaciones:
- **`@Document`**: Indica que la clase se mapea a un documento en MongoDB. Por defecto, el nombre de la colección será `tacoOrder`.
- **`@Id`**: Marca el campo `id` como el identificador único del documento. En este caso, `id` es de tipo `String`, lo que permite que MongoDB asigne automáticamente un valor si es `null`.
- **`List<Taco>`**: La lista de `Taco` se almacenará como un subdocumento dentro del documento `TacoOrder`.

---

### 5. **Resumen del mapeo**

- **`@Document`**: Se usa en la clase raíz del agregado (por ejemplo, `TacoOrder`).
- **`@Id`**: Se usa en la propiedad que actúa como identificador único.
- **Subdocumentos**: Las clases que no son raíz (por ejemplo, `Taco`) no necesitan anotaciones de persistencia y se almacenan como subdocumentos dentro del documento raíz.
- **`@Field` y `@Transient`**: Opcionales, se usan para personalizar el nombre del campo o excluir propiedades de la persistencia.

---

### 6. **Ejemplo completo**

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
@Document(collection = "ingredients")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
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

#### `TacoOrder` (Entidad raíz):
```java
package tacos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class TacoOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Date placedAt = new Date();

    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
```

---

### 7. **Próximos pasos**

Una vez mapeadas las entidades, el siguiente paso es crear repositorios para interactuar con MongoDB. Spring Data MongoDB proporciona interfaces como `MongoRepository` para facilitar esta tarea.

---

En resumen, el mapeo de entidades en MongoDB es sencillo gracias a las anotaciones de Spring Data MongoDB. Solo necesitas usar `@Document` y `@Id` para las entidades raíz, y las clases que no son raíz se almacenan automáticamente como subdocumentos. Esto permite trabajar con MongoDB de manera eficiente y con poco código repetitivo.