package ar.edu.utn.ms2;

import ar.edu.utn.ms2.modelo.Solicitud;
import ar.edu.utn.ms2.servicio.GestorSolicitudes;
import ar.edu.utn.ms2.servicio.DatosRegistroSolicitud;

/** Pruebas sin dependencias externas para capturar el comportamiento inicial. */
public class PruebasCaracterizacion {
    private static int cantidad = 0;

    public static void main(String[] args) {
        registroYPrioridadPorDefecto();
        asignacionYEstado();
        busquedaPorFiltros();
        actualizacionDeTitulo();
        reporte();
        validaciones();
        System.out.println("OK: " + cantidad + " pruebas de caracterización superadas.");
    }

    private static void registroYPrioridadPorDefecto() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, "RECLAMO", "");
        verificar(s.getId() == 1, "ID inicial");
        verificar("ALTA".equals(s.getPrioridad()), "Prioridad RECLAMO");
        verificar("PENDIENTE".equals(s.getEstado()), "Estado inicial");
        verificar(s.getDescripcion().contains("[Área: Alumnos]"), "Área en descripción");
        cantidad++;
    }

    private static void asignacionYEstado() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, "CERTIFICADO", "");
        gestor.asignarResponsable(s.getId(), 2, "Carla", "Alumnos", "carla@example.com");
        gestor.cambiarEstado(s.getId(), "en_proceso");
        verificar("BAJA".equals(s.getPrioridad()), "Prioridad CERTIFICADO");
        verificar("EN_PROCESO".equals(s.getEstado()), "Cambio de estado");
        verificar("Carla".equals(s.getResponsable().getNombre()), "Asignación responsable");
        cantidad++;
    }

    private static void busquedaPorFiltros() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        crear(gestor, "RECLAMO", "");
        crear(gestor, "CONSULTA", "");
        verificar(gestor.buscar("PENDIENTE", "ALTA").size() == 1, "Filtro combinado");
        verificar(gestor.buscar(null, null).size() == 2, "Consulta completa");
        cantidad++;
    }

    private static void actualizacionDeTitulo() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, "CONSULTA", "MEDIA");
        gestor.cambiarTitulo(s.getId(), "Título actualizado");
        verificar("Título actualizado".equals(s.getTitulo()), "Actualización de título");
        cantidad++;
    }

    private static void reporte() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, "RECLAMO", "");
        gestor.cambiarEstado(s.getId(), "RESUELTA");
        String reporte = gestor.generarReporte();
        verificar(reporte.contains("#1 | Consulta de legajo"), "Detalle de reporte");
        verificar(reporte.contains("TOTAL: 1 | PENDIENTES: 0 | EN PROCESO: 0 | RESUELTAS: 1"), "Totales de reporte");
        cantidad++;
    }

    private static void validaciones() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        boolean fallo = false;
        try {
            gestor.registrar(new DatosRegistroSolicitud(1, "Ana", "correo-invalido", "Consulta", "Detalle", "CONSULTA", "", "Alumnos"));
        } catch (IllegalArgumentException e) {
            fallo = true;
        }
        verificar(fallo, "Validación email");
        cantidad++;
    }

    private static Solicitud crear(GestorSolicitudes gestor, String tipo, String prioridad) {
        return gestor.registrar(new DatosRegistroSolicitud(1, "Ana", "ana@example.com", "Consulta de legajo", "Solicito estado del trámite",
                tipo, prioridad, "Alumnos"));
    }

    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError("Fallo: " + mensaje);
    }
}
