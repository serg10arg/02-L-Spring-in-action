
## 1.1 ¿Qué es Spring?

**Introducción a Spring:**
Spring es un framework de desarrollo de aplicaciones en Java que simplifica la construcción de aplicaciones empresariales. Aunque el texto menciona que probablemente estés ansioso por empezar a escribir una aplicación con Spring, primero es importante entender algunos conceptos básicos que te ayudarán a comprender cómo funciona Spring.

**Componentes y la necesidad de coordinación:**
En cualquier aplicación no trivial, existen múltiples componentes, cada uno responsable de una parte específica de la funcionalidad general de la aplicación. Estos componentes necesitan coordinarse entre sí para que la aplicación funcione correctamente. Cuando la aplicación se ejecuta, estos componentes deben ser creados y conectados entre sí.

**El contenedor de Spring (Spring Application Context):**
En el núcleo de Spring se encuentra el **contenedor de Spring**, también conocido como el **Spring Application Context**. Este contenedor es responsable de crear y gestionar los componentes de la aplicación, conocidos como **beans**. Los beans son objetos que Spring instancia, ensambla y gestiona. Estos beans se conectan entre sí dentro del contexto de la aplicación para formar una aplicación completa, de manera similar a cómo los ladrillos, el mortero, la madera, los clavos, la plomería y el cableado se unen para construir una casa.

**Inyección de Dependencias (Dependency Injection - DI):**
La forma en que los beans se conectan entre sí se basa en un patrón llamado **Inyección de Dependencias (DI)**. En lugar de que los componentes creen y gestionen el ciclo de vida de otros beans de los que dependen, una aplicación que utiliza DI delega esta responsabilidad a un contenedor (en este caso, el Spring Application Context). Este contenedor crea y mantiene todos los componentes y los inyecta en los beans que los necesitan. Esto se hace típicamente a través de argumentos del constructor o métodos de acceso a propiedades.

**Ejemplo de Inyección de Dependencias:**
Supongamos que tienes dos componentes en tu aplicación: un **servicio de inventario** (para obtener niveles de inventario) y un **servicio de productos** (para proporcionar información básica sobre productos). El servicio de productos depende del servicio de inventario para poder proporcionar información completa sobre los productos. Spring se encarga de inyectar el servicio de inventario en el servicio de productos.

**Más allá del núcleo:**
Además del contenedor central, Spring ofrece una amplia gama de bibliotecas y funcionalidades adicionales, como un framework web, opciones de persistencia de datos, un framework de seguridad, integración con otros sistemas, monitoreo en tiempo real, soporte para microservicios, un modelo de programación reactiva y muchas otras características necesarias para el desarrollo de aplicaciones modernas.

**Configuración de Spring:**
Históricamente, la forma de configurar el contexto de la aplicación de Spring era a través de archivos XML, donde se describían los componentes y sus relaciones. Por ejemplo, el siguiente código XML declara dos beans: `InventoryService` y `ProductService`, y conecta `InventoryService` en `ProductService` a través de un argumento del constructor:

```xml
<bean id="inventoryService" class="com.example.InventoryService" />
<bean id="productService" class="com.example.ProductService">
  <constructor-arg ref="inventoryService" />
</bean>
```

**Configuración basada en Java:**
En versiones más recientes de Spring, la configuración basada en Java es más común. La siguiente clase de configuración en Java es equivalente al XML anterior:

```java
@Configuration
public class ServiceConfiguration {
  @Bean
  public InventoryService inventoryService() {
    return new InventoryService();
  }

  @Bean
  public ProductService productService() {
    return new ProductService(inventoryService());
  }
}
```

- **@Configuration**: Indica que esta clase es una clase de configuración que proporcionará beans al contexto de la aplicación de Spring.
- **@Bean**: Indica que los objetos devueltos por estos métodos deben ser agregados como beans en el contexto de la aplicación. Por defecto, los IDs de los beans serán los mismos que los nombres de los métodos que los definen.

**Ventajas de la configuración basada en Java:**
La configuración basada en Java ofrece varias ventajas sobre la basada en XML, incluyendo mayor seguridad de tipos y mejor capacidad de refactorización. Sin embargo, la configuración explícita (ya sea en Java o XML) solo es necesaria si Spring no puede configurar automáticamente los componentes.

**Configuración automática:**
Spring también soporta la configuración automática a través de técnicas como **autowiring** (autoconexión) y **component scanning** (escaneo de componentes). Con el escaneo de componentes, Spring puede descubrir automáticamente los componentes en el classpath de la aplicación y crearlos como beans en el contexto de la aplicación. Con la autoconexión, Spring inyecta automáticamente los beans que un componente necesita.

**Spring Boot:**
Con la introducción de **Spring Boot**, la configuración automática ha ido más allá del escaneo de componentes y la autoconexión. Spring Boot es una extensión de Spring que ofrece varias mejoras de productividad, siendo la más conocida la **autoconfiguración**. Spring Boot puede hacer suposiciones razonables sobre qué componentes necesitan ser configurados y conectados, basándose en entradas en el classpath, variables de entorno y otros factores.

**Reducción de la configuración explícita:**
Spring Boot ha reducido drásticamente la cantidad de configuración explícita necesaria para construir una aplicación. De hecho, al final del capítulo, tendrás una aplicación Spring funcionando con solo una línea de código de configuración.

**Conclusión:**
Spring Boot ha mejorado tanto el desarrollo con Spring que es difícil imaginar el desarrollo de aplicaciones Spring sin él. Por esta razón, el texto trata Spring y Spring Boot como si fueran lo mismo. Se utilizará Spring Boot tanto como sea posible y la configuración explícita solo cuando sea necesario. Además, dado que la configuración XML es considerada "antigua", el enfoque principal será la configuración basada en Java.

**Próximos pasos:**
El texto concluye animándote a empezar a escribir tu primera aplicación con Spring, ya que el título del libro incluye la frase "en acción", lo que sugiere un enfoque práctico.
---
### Resumen:
- **Spring** es un framework que simplifica el desarrollo de aplicaciones Java empresariales.
- **Spring Application Context** es el contenedor que gestiona los beans (componentes) de la aplicación.
- **Inyección de Dependencias (DI)** es el mecanismo que Spring utiliza para conectar los beans entre sí.
- **Configuración** puede ser mediante XML o Java, siendo esta última más moderna y segura.
- **Spring Boot** facilita la configuración automática, reduciendo la necesidad de configuración explícita.

