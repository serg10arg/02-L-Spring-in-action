## **2.1.1 Estableciendo el Dominio**

El **dominio** de una aplicación se refiere al área temática que aborda, es decir, los conceptos y objetos que son fundamentales para su funcionamiento. En el caso de **Taco Cloud**, el dominio incluye objetos como:

- **Ingredientes**: Los componentes que forman parte de los tacos.
- **Taco**: Un diseño de taco que combina varios ingredientes.
- **TacoOrder**: Representa un pedido realizado por un cliente, que incluye varios tacos y la información de entrega y pago.

---

### **Diagrama del Dominio**

La relación entre estos objetos se puede visualizar de la siguiente manera:

```
TacoOrder
- deliveryName: String
- deliveryStreet: String
- deliveryCity: String
- deliveryState: String
- deliveryZip: String
- ccNumber: String
- ccExpiration: String
- ccCVV: String
- tacos: List<Taco>

Taco
- name: String
- ingredients: List<Ingredient>

Ingredient
- id: String
- name: String
- type: Ingredient.Type

Ingredient.Type (enum)
- WRAP
- PROTEIN
- VEGGIES
- CHEESE
- SAUCE
```

---

### **Clase de Dominio: Ingrediente**

La clase `Ingredient` representa un ingrediente en la aplicación. Aquí está su definición:

```java
package tacos;

import lombok.Data;

@Data
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

- **Atributos**:
    - `id`: Identificador único del ingrediente.
    - `name`: Nombre del ingrediente (por ejemplo, "Tortilla de Maíz").
    - `type`: Tipo de ingrediente (definido por el enum `Type`).
- **Lombok**:
    - La anotación `@Data` genera automáticamente los métodos `getter`, `setter`, `equals`, `hashCode` y `toString`.

---

### **Clase de Dominio: Taco**

La clase `Taco` representa un diseño de taco, que combina varios ingredientes:

```java
package tacos;

import java.util.List;
import lombok.Data;

@Data
public class Taco {
    private String name;
    private List<Ingredient> ingredients;
}
```

- **Atributos**:
    - `name`: Nombre del taco (por ejemplo, "Taco Picante").
    - `ingredients`: Lista de ingredientes que componen el taco.

---

### **Clase de Dominio: TacoOrder**

La clase `TacoOrder` representa un pedido realizado por un cliente, que incluye varios tacos y la información de entrega y pago:

```java
package tacos;

import java.util.List;
import java.util.ArrayList;
import lombok.Data;

@Data
public class TacoOrder {
    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;
    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
```

- **Atributos**:
    - Información de entrega (`deliveryName`, `deliveryStreet`, etc.).
    - Información de pago (`ccNumber`, `ccExpiration`, `ccCVV`).
    - Lista de tacos (`tacos`).
- **Método `addTaco`**:
    - Permite agregar un taco al pedido.

---

### **Uso de Lombok**

Lombok es una biblioteca que simplifica el código al generar automáticamente métodos como `getter`, `setter`, `equals`, `hashCode` y `toString`. Para usarlo, debes agregar la dependencia en el archivo `pom.xml`:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

Además, es necesario instalar el plugin de Lombok en tu IDE para evitar errores de compilación.

---

### **Resumen**

- **Dominio**: Define los conceptos clave de la aplicación, como ingredientes, tacos y pedidos.
- **Clases de Dominio**:
    - `Ingredient`: Representa un ingrediente con un ID, nombre y tipo.
    - `Taco`: Representa un diseño de taco con un nombre y una lista de ingredientes.
    - `TacoOrder`: Representa un pedido con información de entrega, pago y una lista de tacos.
- **Lombok**: Simplifica el código al generar automáticamente métodos comunes.

---

### **Preguntas de Opción Múltiple**

1. **¿Qué representa la clase `Ingredient` en el dominio de Taco Cloud?**
    - A) Un pedido de tacos.
    - B) Un diseño de taco.
    - C) Un ingrediente para tacos.
    - D) Un cliente.

   **Respuesta Correcta: C) Un ingrediente para tacos.**
    - **Justificación:** La clase `Ingredient` representa un ingrediente que puede ser utilizado en la preparación de tacos.

2. **¿Qué anotación de Lombok se utiliza para generar automáticamente métodos como `getter` y `setter`?**
    - A) `@Getter`
    - B) `@Setter`
    - C) `@Data`
    - D) `@ToString`

   **Respuesta Correcta: C) `@Data`**
    - **Justificación:** La anotación `@Data` genera automáticamente `getter`, `setter`, `equals`, `hashCode` y `toString`.

3. **¿Qué atributo de la clase `TacoOrder` almacena la lista de tacos en un pedido?**
    - A) `deliveryName`
    - B) `ccNumber`
    - C) `tacos`
    - D) `ingredients`

   **Respuesta Correcta: C) `tacos`**
    - **Justificación:** El atributo `tacos` es una lista que almacena los tacos incluidos en un pedido.

4. **¿Qué método de la clase `TacoOrder` permite agregar un taco al pedido?**
    - A) `addIngredient()`
    - B) `addTaco()`
    - C) `setTacos()`
    - D) `getTacos()`

   **Respuesta Correcta: B) `addTaco()`**
    - **Justificación:** El método `addTaco()` permite agregar un taco a la lista de tacos en un pedido.

5. **¿Qué tipo de dato se utiliza para representar el tipo de un ingrediente en la clase `Ingredient`?**
    - A) `String`
    - B) `int`
    - C) `enum`
    - D) `List`

   **Respuesta Correcta: C) `enum`**
    - **Justificación:** El tipo de un ingrediente se define como un `enum` con valores como `WRAP`, `PROTEIN`, `VEGGIES`, etc.

---

### **Conclusión**

En esta sección, has aprendido cómo definir las clases de dominio para Taco Cloud, incluyendo `Ingredient`, `Taco` y `TacoOrder`. Estas clases forman la base de la aplicación y serán utilizadas en los controladores y vistas para manejar solicitudes web.

