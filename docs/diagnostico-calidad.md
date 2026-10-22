
# Diagnóstico de calidad interna y deuda técnica

## 1. Objetivo y criterio de análisis

Se revisó la implementación inicial para identificar debilidades que puedan dificultar la lectura, el mantenimiento y la evolución del sistema. El diagnóstico está basado en el código de la versión identificada con el tag `estado-inicial`; no implica que las funcionalidades observadas actualmente presenten fallas.

Se tuvieron en cuenta la distribución de responsabilidades, el tamaño de los métodos, la cantidad de parámetros, las duplicaciones, la representación de datos y el acoplamiento. La prioridad asignada es una estimación del riesgo y del beneficio esperado de corregir cada problema.

**Referencias de ubicación:** las rutas y los números de línea corresponden a los archivos Java de `estado-inicial`. Las líneas podrían cambiar en commits posteriores.

## 2. Resumen de hallazgos

| ID | Code Smell o debilidad técnica | Ubicación principal | Impacto / riesgo de mantenimiento | Prioridad |
|---|---|---|---|---|
| CS-01 | Long Method | `GestorSolicitudes.java`, `registrar()` (15–54) | La validación, normalización, creación y persistencia quedan mezcladas. | Alta |
| CS-02 | Long Parameter List | `GestorSolicitudes.java`, `registrar()` (15–17) | La llamada tiene ocho argumentos y es fácil confundir su orden. | Alta |
| CS-03 | Duplicated Code | `GestorSolicitudes.java`, `registrar()` (18–23) y `asignarResponsable()` (62–67) | Se repiten validaciones de nombres y emails; pueden divergir. | Media |
| CS-04 | Responsabilidades concentradas / SRP | `GestorSolicitudes.java` (15–148) | Cambios en validación, consultas o reportes afectan la misma clase. | Alta |
| CS-05 | Lógica de presentación en el servicio | `GestorSolicitudes.java`, `generarReporte()` (113–143) | El servicio construye textos y formatea datos de otras entidades. | Media |
| CS-06 | Dependencia de implementación concreta / DIP | `GestorSolicitudes.java` (12) | Es difícil sustituir el almacenamiento o aislarlo en pruebas. | Alta |
| CS-07 | Primitive Obsession | `Solicitud.java` (8–10) y `GestorSolicitudes.java` (37–45, 76–88) | Los estados, tipos y prioridades carecen de un tipo explícito común. | Media |
| CS-08 | Validación incompleta del dominio | `GestorSolicitudes.java`, `registrar()` (30–51) | Se aceptan tipos y prioridades no reconocidos, con datos potencialmente inconsistentes. | Alta |

**Nota:** CS-01, CS-02 y CS-04 se relacionan, pero representan problemas diferentes: complejidad de un método, interfaz de invocación y distribución de responsabilidades de la clase. CS-07 y CS-08 también se relacionan: uno describe la representación de los valores y el otro, las reglas de validación que faltan. Se documentan por separado porque requieren decisiones de mejora distintas.

## 3. Evidencias y análisis de cada hallazgo

### CS-01 — Long Method

**Ubicación:** `src/main/java/ar/edu/utn/ms2/servicio/GestorSolicitudes.java`, método `registrar()`, líneas 15–54.

**Evidencia del código original:**

```java
if (nombreEstudiante == null || nombreEstudiante.trim().isEmpty()) {
    throw new IllegalArgumentException("Nombre requerido");
}
// ... otras validaciones ...
String prioridadFinal = prioridad;
if (prioridadFinal == null || prioridadFinal.isBlank()) {
    if (tipo.equalsIgnoreCase("CERTIFICADO")) {
        prioridadFinal = "BAJA";
    } else if (tipo.equalsIgnoreCase("RECLAMO")) {
        prioridadFinal = "ALTA";
    } else {
        prioridadFinal = "MEDIA";
    }
}
// ... creación de entidades y guardado ...
```

**Impacto:** `registrar()` valida múltiples campos, determina una prioridad predeterminada, prepara la descripción, crea entidades y guarda la solicitud. Esto obliga a revisar una misma operación cuando cambia cualquiera de esas reglas.

**Riesgo de mantenimiento:** una modificación puntual puede afectar involuntariamente otra tarea del método y volver más complejas las pruebas unitarias.

**Prioridad:** Alta.

**Mejora propuesta:** aplicar **Extract Method** para separar validaciones, cálculo de prioridad y preparación de datos, manteniendo el resultado observable.

### CS-02 — Long Parameter List

**Ubicación:** `GestorSolicitudes.java`, método `registrar()`, líneas 15–17. También se observa su uso en `Main.java`, líneas 9–17.

**Evidencia del código original:**

```java
public Solicitud registrar(int estudianteId, String nombreEstudiante, String emailEstudiante,
                          String titulo, String descripcion, String tipo,
                          String prioridad, String area) {
```

**Impacto:** el método recibe ocho argumentos, mayoritariamente `String`. La llamada no permite reconocer fácilmente qué representa cada posición y se vuelve difícil extenderla con nuevos datos.

**Riesgo de mantenimiento:** intercambio accidental de argumentos del mismo tipo y necesidad de actualizar todas las llamadas al cambiar la firma.

**Prioridad:** Alta.

**Mejora propuesta:** aplicar **Introduce Parameter Object** para agrupar los datos de registro en un objeto con nombres de campos explícitos. Actualizar las llamadas y comprobar equivalencia mediante pruebas.

### CS-03 — Duplicated Code

**Ubicación:** `GestorSolicitudes.java`, `registrar()` (líneas 18–23) y `asignarResponsable()` (líneas 62–67).

**Evidencia del código original:**

```java
if (nombreEstudiante == null || nombreEstudiante.trim().isEmpty()) {
    throw new IllegalArgumentException("Nombre requerido");
}
if (emailEstudiante == null || !emailEstudiante.contains("@")) {
    throw new IllegalArgumentException("Email inválido");
}
```

En `asignarResponsable()` se repite el mismo criterio de nombre obligatorio y de email con `@`, utilizando otros parámetros.

**Impacto:** las reglas equivalentes se mantienen en varios lugares, lo que aumenta el trabajo cuando una validación debe cambiar.

**Riesgo de mantenimiento:** que una operación acepte entradas que otra rechaza por haberse actualizado solamente una de las dos.

**Prioridad:** Media.

**Mejora propuesta:** extraer validaciones reutilizables con nombres claros o encapsularlas en componentes de dominio apropiados.

### CS-04 — Concentración de responsabilidades / SRP

**Ubicación:** `GestorSolicitudes.java`, métodos `registrar()`, `asignarResponsable()`, `cambiarEstado()`, `cambiarTitulo()`, `buscar()` y `generarReporte()` (líneas 15–148).

**Evidencia del código original:**

```java
public Solicitud registrar(/* ... */) { /* ... */ }
public void asignarResponsable(/* ... */) { /* ... */ }
public List<Solicitud> buscar(String estado, String prioridad) { /* ... */ }
public String generarReporte() { /* ... */ }
```

**Impacto:** una sola clase concentra reglas de registro, cambios sobre solicitudes, consultas y presentación de reportes. No todas esas tareas responden a la misma razón de cambio.

**Riesgo de mantenimiento:** una modificación en el formato del reporte obliga a intervenir en la misma clase donde se gestionan las operaciones del negocio.

**Prioridad:** Alta.

**Mejora propuesta:** separar responsabilidades cuando aporte valor, por ejemplo, moviendo la generación de reportes a un componente específico (**Extract Class / reubicación de responsabilidad**). No dividir automáticamente cada método en una clase distinta.

### CS-05 — Lógica de presentación en el servicio

**Ubicación:** `GestorSolicitudes.java`, `generarReporte()`, líneas 113–143, especialmente 123–135.

**Evidencia del código original:**

```java
Responsable responsable = solicitud.getResponsable();
if (responsable == null) {
    sb.append("Sin asignar");
} else {
    sb.append(responsable.getNombre()).append(" (")
            .append(responsable.getArea()).append(", ")
            .append(responsable.getEmail()).append(")");
}
```

**Impacto:** el servicio conoce los detalles de presentación de los datos del responsable y construye directamente el texto final del informe. Este es un problema de ubicación de responsabilidades; no se lo considera necesariamente un caso puro de *Feature Envy*.

**Riesgo de mantenimiento:** cambiar el formato del responsable o incorporar otro formato de salida implica modificar la lógica de `GestorSolicitudes`.

**Prioridad:** Media.

**Mejora propuesta:** aplicar **Move Method** o una reubicación equivalente de la responsabilidad de formateo, sin alterar el texto del reporte ya verificado.

### CS-06 — Dependencia de implementación concreta / DIP

**Ubicación:** `GestorSolicitudes.java`, línea 12.

**Evidencia del código original:**

```java
private final RepositorioSolicitudesMemoria repositorio = new RepositorioSolicitudesMemoria();
```

**Impacto:** el gestor instancia directamente la clase de persistencia en memoria. La lógica de aplicación queda vinculada a una implementación concreta de almacenamiento.

**Riesgo de mantenimiento:** reemplazar el almacenamiento por un archivo o una base de datos exige modificar el gestor; también dificulta pasar un repositorio de prueba controlado.

**Prioridad:** Alta.

**Mejora propuesta:** introducir una abstracción de repositorio e **inyección de dependencias**, conservando un modo de construcción sencillo para la ejecución por consola.

### CS-07 — Primitive Obsession: estados, tipos y prioridades como texto

**Ubicación:** `Solicitud.java`, atributos `tipo`, `prioridad` y `estado` (líneas 8–10); `GestorSolicitudes.java`, cálculo de prioridad y validación de estado (líneas 37–45 y 76–88).

**Evidencia del código original:**

```java
private final String tipo;
private final String prioridad;
private String estado;
```

```java
if (!estado.equals("PENDIENTE") && !estado.equals("EN_PROCESO") && !estado.equals("RESUELTA")) {
    throw new IllegalArgumentException("Estado inválido");
}
```

**Impacto:** las categorías del negocio se expresan como texto libre y dependen de comparaciones manuales y literales distribuidos por el código.

**Riesgo de mantenimiento:** errores tipográficos, inconsistencias en los valores y mayor cantidad de condicionales al agregar estados o tipos.

**Prioridad:** Media.

**Mejora propuesta:** evaluar **enum** para estado, tipo y prioridad, con conversión controlada desde las entradas actuales y preservación de las salidas esperadas.

### CS-08 — Validación incompleta de tipos y prioridades

**Ubicación:** `GestorSolicitudes.java`, `registrar()`, líneas 30–51.

**Evidencia del código original:**

```java
if (tipo == null || tipo.trim().isEmpty()) {
    throw new IllegalArgumentException("Tipo requerido");
}
// ...
String prioridadFinal = prioridad;
if (prioridadFinal == null || prioridadFinal.isBlank()) {
    if (tipo.equalsIgnoreCase("CERTIFICADO")) {
        prioridadFinal = "BAJA";
    } else if (tipo.equalsIgnoreCase("RECLAMO")) {
        prioridadFinal = "ALTA";
    } else {
        prioridadFinal = "MEDIA";
    }
}
```

**Impacto:** se comprueba que `tipo` no esté vacío, pero no que pertenezca a un conjunto de tipos válidos. Cuando se pasa una prioridad explícita, tampoco se verifica que sea reconocida. Por ejemplo, la operación actual puede aceptar un tipo `DESCONOCIDO` o una prioridad `URGENTISIMA`.

**Riesgo de mantenimiento:** datos inconsistentes, reglas de negocio aplicadas incorrectamente y resultados de filtros que no coincidan con las categorías previstas.

**Prioridad:** Alta.

**Mejora propuesta:** definir los valores admitidos y centralizar su validación. **Antes de imponer nuevas restricciones debe confirmarse la regla de negocio**, ya que rechazar valores previamente aceptados representa un cambio de comportamiento y no un refactoring estrictamente neutro.

## 4. Plan preliminar de mejoras

| Orden propuesto | Problemas vinculados | Acción técnica | Evidencia esperada |
|---|---|---|---|
| 1 | CS-01, parcialmente CS-03 | Extract Method en el registro | Método más corto; pruebas sin regresiones. |
| 2 | CS-02 | Introduce Parameter Object | Firma simplificada; llamadas actualizadas; mismas salidas. |
| 3 | CS-05 y CS-04 | Move Method / extraer formateo del reporte | Texto del reporte idéntico al original. |
| 4 | CS-04 | Extract Class para responsabilidades de reporte | Menor concentración de responsabilidades. |
| 5 | CS-06 | Introducir interfaz e inyección del repositorio | Posibilidad de cambiar implementación sin alterar el gestor. |
| 6 | CS-07 y CS-08 | Definir tipos de dominio y revisar validaciones | Datos más consistentes y pruebas para valores aceptados/rechazados. |

Este es un **plan**, no una declaración de correcciones ya realizadas. Las mejoras se documentarán en commits posteriores y se verificarán con pruebas. El orden podrá ajustarse si aparecen dependencias o riesgos durante la implementación.

## 5. Criterios de verificación del trabajo posterior

- Conservar el comportamiento documentado de `Main` y el resultado de las seis pruebas de caracterización de la versión inicial.
- Ejecutar las pruebas después de cada refactoring y registrar los resultados reales.
- Agregar casos de prueba relevantes, especialmente para los límites y validaciones del dominio.
- Diferenciar los refactorings que preservan el comportamiento de las correcciones funcionales que cambian reglas de entrada.
- Registrar los commits correspondientes para mantener una comparación verificable entre `estado-inicial` y la versión refactorizada.

**Estado del documento:** diagnóstico elaborado sobre la versión inicial. No se modificó el código fuente durante esta etapa.
