
# Revisión de código – Code Review

**Versión revisada:** posterior a `etapa-refactoring`.

## 1. Objetivo

Evaluar la calidad del código después de los refactorings, comprobar las mejoras implementadas e identificar posibles problemas pendientes.

La revisión se planteó como una actividad grupal, utilizando una checklist basada en SOLID y Clean Code.

## 2. Checklist de revisión

| Criterio | Resultado | Observaciones |
|---|---|---|
| Separación de responsabilidades | Conforme | La generación de reportes tiene una clase independiente. |
| Complejidad de métodos | Conforme | Se extrajeron validaciones y cálculo de prioridad de `registrar()`. |
| Código duplicado | Conforme | Se centralizaron las validaciones de nombre y email. |
| Manejo de parámetros | Conforme | Se utiliza `DatosRegistroSolicitud`. |
| Inversión de dependencias | Con observación | Se detectó falta de validación del repositorio recibido. |
| Validación del dominio | Con observación | Se detectó una inconsistencia al normalizar el tipo. |
| Pruebas | Conforme | Las ocho pruebas existentes funcionaban correctamente. |
| Documentación | Requiere actualización | El README conservaba referencias a la versión inicial. |

## 3. Hallazgos y correcciones

### CR-01 – Repositorio nulo

**Ubicación:** `GestorSolicitudes.java`, constructor con parámetro `RepositorioSolicitudes`.

**Hallazgo:** el constructor permitía recibir un repositorio nulo, lo que podía generar un error al utilizarlo posteriormente.

**Prioridad:** Media.

**Corrección:** se incorporó `Objects.requireNonNull()` para rechazar inmediatamente una dependencia nula.

**Verificación:** se agregó una prueba que comprueba el rechazo del valor nulo y el mensaje de error correspondiente.

**Estado:** Corregido.

### CR-02 – Normalización inconsistente del tipo

**Ubicación:** `GestorSolicitudes.java`, método `determinarPrioridad()`.

**Hallazgo:** un tipo con espacios adicionales, como `" RECLAMO "`, podía recibir una prioridad incorrecta.

**Prioridad:** Media.

**Corrección:** se incorporó `trim()` en las comparaciones utilizadas para determinar la prioridad predeterminada.

**Verificación:** se agregó una prueba que registra un reclamo con espacios adicionales y comprueba que reciba prioridad `ALTA`.

**Estado:** Corregido.

### CR-03 – README desactualizado

**Ubicación:** `README.md`.

**Hallazgo:** la documentación conservaba descripciones correspondientes a la versión inicial del proyecto.

**Prioridad:** Baja.

**Corrección:** se actualizó el README para reflejar la estructura actual, las mejoras implementadas, las pruebas y las instrucciones de ejecución.

**Verificación:** revisión del contenido actualizado y su correspondencia con la estructura del repositorio.

**Estado:** Corregido.

## 4. Resultados de la revisión

Las correcciones CR-01 y CR-02 se registraron conjuntamente en el commit:

`fix(CR-01,CR-02): corregir validaciones detectadas en code review`

Después de las modificaciones, se ejecutaron las diez pruebas disponibles y todas finalizaron correctamente. También se verificó el funcionamiento de `Main.java`.

El sistema pasó de ocho a diez pruebas mediante la incorporación de dos casos específicos derivados del Code Review.

La actualización del README y este registro de revisión se incorporan como documentación de cierre de la etapa.

## 5. Conclusión

La revisión permitió confirmar las principales mejoras de diseño e identificar tres observaciones adicionales.

Los hallazgos fueron abordados mediante correcciones puntuales, pruebas adicionales y actualización de la documentación, mejorando la robustez y la mantenibilidad del proyecto.
