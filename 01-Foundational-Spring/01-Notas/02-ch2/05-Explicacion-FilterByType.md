
---

### **Fragmento de Código**

```java
private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
    return ingredients
            .stream()
            .filter(x -> x.getType().equals(type))
            .collect(Collectors.toList());
}
```

Este método se llama `filterByType` y su propósito es filtrar una lista de ingredientes (`ingredients`) según un tipo específico (`type`). Vamos a analizarlo línea por línea.

---

### **1. Parámetros del Método**

```java
private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type)
```

- **`List<Ingredient> ingredients`**: Es una lista de objetos de tipo `Ingredient`. Cada `Ingredient` tiene propiedades como `id`, `name` y `type`.
- **`Ingredient.Type type`**: Es un valor del enum `Ingredient.Type` (por ejemplo, `WRAP`, `PROTEIN`, `VEGGIES`, etc.). Este parámetro especifica el tipo de ingrediente que queremos filtrar.

El método devuelve un `Iterable<Ingredient>`, que es una lista filtrada de ingredientes del tipo especificado.

---

### **2. Uso de Streams**

```java
return ingredients.stream()
```

- **`ingredients.stream()`**: Convierte la lista `ingredients` en un **stream**. Un stream es una secuencia de elementos que permite realizar operaciones funcionales (como filtrar, mapear, reducir, etc.) de manera eficiente y expresiva.

---

### **3. Filtrado del Stream**

```java
.filter(x -> x.getType().equals(type))
```

- **`.filter(...)`**: Es una operación intermedia en el stream que filtra los elementos según una condición.
- **`x -> x.getType().equals(type)`**: Esta es una **expresión lambda** que define la condición de filtrado:
    - `x` representa cada elemento del stream (en este caso, un objeto `Ingredient`).
    - `x.getType()` obtiene el tipo del ingrediente.
    - `equals(type)` compara el tipo del ingrediente con el tipo proporcionado como parámetro.
    - Solo los ingredientes cuyo tipo coincida con `type` pasarán el filtro.

---

### **4. Colectar los Resultados**

```java
.collect(Collectors.toList());
```

- **`.collect(Collectors.toList())`**: Es una operación terminal en el stream que recolecta los elementos filtrados en una nueva lista.
    - `Collectors.toList()` es un método de la clase `Collectors` que convierte el stream en una lista.
    - El resultado es una lista (`List<Ingredient>`) que contiene solo los ingredientes del tipo especificado.

---

### **Resumen del Método**

Este método toma una lista de ingredientes y un tipo de ingrediente, filtra la lista para incluir solo los ingredientes del tipo especificado y devuelve la lista filtrada.

---

### **Cómo se Conecta con el Código Principal**

Ahora, veamos cómo este método se integra con el resto del código en la clase `DesignTacoController`.

#### **1. Uso en `addIngredientsToModel`**

El método `filterByType` se utiliza dentro del método `addIngredientsToModel`:

```java
@ModelAttribute
public void addIngredientsToModel(Model model) {
    List<Ingredient> ingredients = Arrays.asList(
        new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
        new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
        // ...
    );

    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
        model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
    }
}
```

- **`filterByType(ingredients, type)`**: Para cada tipo de ingrediente (`WRAP`, `PROTEIN`, etc.), se llama a `filterByType` para obtener una lista de ingredientes de ese tipo.
- **`model.addAttribute(...)`**: La lista filtrada se agrega al modelo con un nombre que corresponde al tipo de ingrediente en minúsculas (por ejemplo, `"wrap"`, `"protein"`, etc.).

#### **2. Propósito en el Flujo de la Aplicación**

- El método `addIngredientsToModel` se ejecuta antes de que se muestre el formulario de diseño de tacos (`showDesignForm`).
- Su objetivo es preparar los datos que la vista (`design.html`) necesitará para renderizar dinámicamente las opciones de ingredientes.
- Gracias a `filterByType`, el modelo contiene listas de ingredientes organizadas por tipo, lo que facilita la creación de menús desplegables o listas de selección en la vista.

#### **3. Ejemplo de Uso en la Vista**

En la vista (por ejemplo, `design.html`), podrías tener algo como esto:

```html
<h2>Wraps</h2>
<select name="wrap">
    <option th:each="ingredient : ${wrap}" 
            th:value="${ingredient.id}" 
            th:text="${ingredient.name}">
    </option>
</select>

<h2>Proteins</h2>
<select name="protein">
    <option th:each="ingredient : ${protein}" 
            th:value="${ingredient.id}" 
            th:text="${ingredient.name}">
    </option>
</select>
```

- **`${wrap}`**: Accede a la lista de ingredientes de tipo `WRAP` que se agregó al modelo.
- **`${protein}`**: Accede a la lista de ingredientes de tipo `PROTEIN`.

---

### **Conclusión**

El método `filterByType` es una pieza clave en la lógica del controlador:
1. Filtra una lista de ingredientes según su tipo.
2. Se utiliza en `addIngredientsToModel` para organizar los ingredientes en el modelo.
3. La vista utiliza estas listas filtradas para mostrar opciones dinámicas al usuario.

En resumen, este método permite separar y organizar los ingredientes por tipo, lo que facilita la creación de una interfaz de usuario clara y funcional para diseñar tacos.