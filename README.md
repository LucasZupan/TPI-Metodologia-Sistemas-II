
# Sistema de Solicitudes Académicas

Trabajo Práctico Integrador de Metodología de Sistemas II (UTN).

Aplicación de consola desarrollada en Java 21 para gestionar solicitudes académicas de estudiantes. El proyecto incluye el análisis de calidad de una versión inicial, la aplicación de refactorings, pruebas de verificación y revisión de código.

## Funcionalidades

- Registrar solicitudes académicas.
- Asignar responsables.
- Cambiar el estado de una solicitud.
- Buscar solicitudes por estado y prioridad.
- Modificar el título de una solicitud.
- Generar reportes con cantidades por estado.

## Estructura del proyecto

- `app`: punto de entrada mediante `Main.java`.
- `modelo`: entidades y enumeraciones del dominio.
- `servicio`: gestión de solicitudes, datos de registro y generación de reportes.
- `datos`: interfaz del repositorio e implementación en memoria.
- `src/test`: pruebas de caracterización y validaciones.

Las entidades principales son `Estudiante`, `Solicitud` y `Responsable`.

Los tipos, estados y prioridades se representan internamente mediante enumeraciones.

## Mejoras implementadas

A partir del diagnóstico de ocho debilidades técnicas se realizaron las siguientes mejoras:

- **CS-01:** Extract Method para separar validaciones y cálculo de prioridad.
- **CS-02:** Introduce Parameter Object mediante `DatosRegistroSolicitud`.
- **CS-03:** centralización de validaciones duplicadas.
- **CS-04:** Extract Class mediante `GeneradorReporteSolicitudes`.
- **CS-05:** Move Method para el formato del responsable.
- **CS-06:** interfaz de repositorio e inyección de dependencias.
- **CS-07 y CS-08:** enumeraciones y validación de valores del dominio.

Durante el Code Review también se corrigieron la validación del repositorio recibido por constructor (CR-01) y la normalización del tipo de solicitud al calcular la prioridad (CR-02).

## Requisitos

- JDK 21.
- No requiere bibliotecas ni servicios externos.
- Persistencia en memoria: los datos no se conservan al finalizar la ejecución.

## Ejecución desde IntelliJ IDEA

Abrir la carpeta como proyecto Java, configurar JDK 21 y ejecutar:

`ar.edu.utn.ms2.app.Main`

Para ejecutar las pruebas:

`ar.edu.utn.ms2.PruebasCaracterizacion`

## Ejecución desde Windows PowerShell

Compilar las clases principales:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
```

Ejecutar el programa:

```powershell
java -cp out ar.edu.utn.ms2.app.Main
```

Compilar y ejecutar las pruebas:

```powershell
javac -encoding UTF-8 -cp out -d out (Get-ChildItem -Recurse src/test/java -Filter *.java).FullName
java -cp out ar.edu.utn.ms2.PruebasCaracterizacion
```

## Pruebas

El sistema comenzó con seis pruebas de caracterización.

Durante las mejoras y el Code Review se incorporaron cuatro pruebas adicionales:

- Rechazo de tipos de solicitud inválidos.
- Rechazo de prioridades inválidas.
- Validación de repositorio nulo.
- Cálculo de prioridad con espacios adicionales en el tipo.

**Resultado final: 10 pruebas superadas.**

## Documentación y versiones

La carpeta `docs` contiene el diagnóstico técnico y las evidencias de ejecución y verificación.

Entre los hitos registrados mediante tags se encuentran:

- `estado-inicial`: versión original.
- `etapa-diagnostico`: análisis de debilidades técnicas.
- `etapa-refactoring`: implementación de las mejoras previstas.
- `etapa-pruebas`: verificación y evidencias posteriores al refactoring.

El historial de commits permite consultar las modificaciones realizadas.

## Alcance

El sistema utiliza datos ficticios y almacenamiento en memoria. Su objetivo es demostrar el análisis, la mejora y la verificación de la calidad del software.
