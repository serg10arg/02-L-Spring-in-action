## 3.1.2 Trabajando con `JdbcTemplate`

En esta sección, el texto explica cómo configurar y comenzar a usar `JdbcTemplate` en un proyecto Spring Boot. Aquí está el desglose detallado:

---

#### 1. **Agregar `JdbcTemplate` al Proyecto**

Para usar `JdbcTemplate`, primero debes agregar la dependencia de Spring Boot JDBC Starter a tu proyecto. Esto se hace agregando la siguiente dependencia en tu archivo `pom.xml` (si estás usando Maven):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

Esta dependencia incluye:
- **Spring JDBC**: Proporciona la clase `JdbcTemplate` y otras utilidades para trabajar con bases de datos.
- **Spring Transaction Management**: Permite gestionar transacciones de base de datos.

---

#### 2. **Configurar una Base de Datos**

Para trabajar con `JdbcTemplate`, necesitas una base de datos. Durante el desarrollo, es común usar una **base de datos embebida**, como H2, que es liviana y fácil de configurar.

Para agregar H2 a tu proyecto, agrega la siguiente dependencia en `pom.xml`:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

- **H2**: Es una base de datos en memoria que no requiere instalación externa. Es ideal para desarrollo y pruebas.
- **`<scope>runtime</scope>`**: Indica que esta dependencia solo es necesaria en tiempo de ejecución, no durante la compilación.

---

#### 3. **Configurar el Nombre de la Base de Datos**

Por defecto, Spring Boot genera un nombre único para la base de datos H2, lo que puede dificultar la conexión manual a la base de datos (por ejemplo, usando la consola H2). Para evitar esto, puedes configurar un nombre fijo para la base de datos.

Esto se hace agregando las siguientes propiedades en el archivo de configuración (`application.properties` o `application.yml`).

##### Opción 1: Usar `application.properties`

```properties
spring.datasource.generate-unique-name=false
spring.datasource.name=tacocloud
```

##### Opción 2: Usar `application.yml` (recomendado)

```yaml
spring:
  datasource:
    generate-unique-name: false
    name: tacocloud
```

- **`spring.datasource.generate-unique-name=false`**: Desactiva la generación de un nombre único para la base de datos.
- **`spring.datasource.name=tacocloud`**: Asigna el nombre `tacocloud` a la base de datos.

Con esta configuración, la URL de la base de datos será:  
**`jdbc:h2:mem:tacocloud`**

---

#### 4. **Acceder a la Consola H2**

Spring Boot DevTools habilita automáticamente la consola H2 en la siguiente URL:  
**`http://localhost:8080/h2-console`**

Para conectarte a la base de datos desde la consola H2, usa la siguiente configuración:
- **JDBC URL**: `jdbc:h2:mem:tacocloud`
- **Usuario**: `sa` (usuario predeterminado)
- **Contraseña**: (deja este campo vacío)

---

#### 5. **Ventajas de Usar YAML**

El texto menciona que, aunque puedes usar tanto `application.properties` como `application.yml`, se prefiere YAML por su estructura más legible y organizada. Por ejemplo:

```yaml
spring:
  datasource:
    generate-unique-name: false
    name: tacocloud
```

Es más fácil de leer y mantener, especialmente cuando hay muchas propiedades.

---

#### 6. **Próximos Pasos: Crear un Repositorio**

Una vez configurado `JdbcTemplate` y la base de datos, el siguiente paso es escribir un **repositorio** que permita interactuar con la base de datos. En este caso, el texto sugiere crear un repositorio para manejar datos de `Ingredient`.

Un repositorio típico con `JdbcTemplate` incluiría métodos para:
- **Consultar ingredientes**: Obtener todos los ingredientes o buscar uno por su ID.
- **Guardar ingredientes**: Insertar nuevos ingredientes en la base de datos.

---

### Resumen

1. **Dependencias necesarias**:
    - `spring-boot-starter-jdbc`: Para usar `JdbcTemplate`.
    - `h2`: Para usar una base de datos embebida H2.

2. **Configuración de la base de datos**:
    - Desactiva la generación de nombres únicos (`spring.datasource.generate-unique-name=false`).
    - Asigna un nombre fijo a la base de datos (`spring.datasource.name=tacocloud`).

3. **Consola H2**:
    - Accede a la consola en `http://localhost:8080/h2-console`.
    - Usa la URL `jdbc:h2:mem:tacocloud` para conectarte.

4. **YAML vs Properties**:
    - YAML es más legible y se recomienda para configuraciones complejas.

5. **Siguiente paso**:
    - Crear un repositorio para interactuar con la base de datos usando `JdbcTemplate`.

Con esto, tu aplicación Spring Boot está lista para usar `JdbcTemplate` y una base de datos H2 embebida. ¡El siguiente paso es implementar el repositorio! 🚀