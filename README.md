# Sistema de Solicitudes Académicas — Versión inicial

Prototipo de consola en **Java 21**, diseñado para el TPI de Metodología de Sistemas II (UTN).

## Alcance funcional

- Registrar solicitudes académicas de estudiantes.
- Asignar un responsable a cada solicitud.
- Cambiar estado (`PENDIENTE`, `EN_PROCESO`, `RESUELTA`).
- Buscar por estado y prioridad.
- Actualizar el título de una solicitud.
- Generar un reporte de solicitudes y totales por estado.

## Modelo

- `Estudiante`, `Solicitud`, `Responsable`.
- Persistencia **en memoria** durante la ejecución (no conserva datos al cerrar).
- Ejemplos de tipos: `CERTIFICADO`, `RECLAMO` y `CONSULTA`.

## Requisitos

JDK 21. Sin bibliotecas ni servicios externos.

## Ejecutar con IntelliJ IDEA

Abrir la carpeta como proyecto Java, seleccionar JDK 21 y ejecutar:

`ar.edu.utn.ms2.app.Main`

Para las pruebas, ejecutar:

`ar.edu.utn.ms2.PruebasCaracterizacion`

## Ejecutar desde terminal (Windows PowerShell)

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
java -cp out ar.edu.utn.ms2.app.Main
javac -encoding UTF-8 -cp out -d out (Get-ChildItem -Recurse src/test/java -Filter *.java).FullName
java -cp out ar.edu.utn.ms2.PruebasCaracterizacion
```

## Nota de alcance

La información es completamente ficticia. Esta es la **versión base** para ejecutar, caracterizar y posteriormente mejorar mediante refactoring. No se han aplicado aún las mejoras de diseño.
