
package ar.edu.utn.ms2;

import ar.edu.utn.ms2.modelo.Solicitud;
import ar.edu.utn.ms2.modelo.TipoSolicitud;
import ar.edu.utn.ms2.modelo.PrioridadSolicitud;
import ar.edu.utn.ms2.modelo.EstadoSolicitud;
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
        validarTipoInvalido();
        validarPrioridadInvalida();
        System.out.println("OK: " + cantidad + " pruebas de caracterización superadas.");
    }

    private static void registroYPrioridadPorDefecto() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, TipoSolicitud.RECLAMO, null);
        verificar(s.getId() == 1, "ID inicial");
        verificar(PrioridadSolicitud.ALTA.name().equals(s.getPrioridad()), "Prioridad RECLAMO");
        verificar(EstadoSolicitud.PENDIENTE.name().equals(s.getEstado()), "Estado inicial");
        verificar(s.getDescripcion().contains("[Área: Alumnos]"), "Área en descripción");
        cantidad++;
    }

    private static void asignacionYEstado() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, TipoSolicitud.CERTIFICADO, null);
        gestor.asignarResponsable(s.getId(), 2, "Carla", "Alumnos", "carla@example.com");
        gestor.cambiarEstado(s.getId(), "en_proceso");
        verificar(PrioridadSolicitud.BAJA.name().equals(s.getPrioridad()), "Prioridad CERTIFICADO");
        verificar(EstadoSolicitud.EN_PROCESO.name().equals(s.getEstado()), "Cambio de estado");
        verificar("Carla".equals(s.getResponsable().getNombre()), "Asignación responsable");
        cantidad++;
    }

    private static void busquedaPorFiltros() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        crear(gestor, TipoSolicitud.RECLAMO, null);
        crear(gestor, TipoSolicitud.CONSULTA, null);
        verificar(gestor.buscar(EstadoSolicitud.PENDIENTE.name(),
                PrioridadSolicitud.ALTA.name()).size() == 1, "Filtro combinado");
        verificar(gestor.buscar(null, null).size() == 2, "Consulta completa");
        cantidad++;
    }

    private static void actualizacionDeTitulo() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, TipoSolicitud.CONSULTA, PrioridadSolicitud.MEDIA);
        gestor.cambiarTitulo(s.getId(), "Título actualizado");
        verificar("Título actualizado".equals(s.getTitulo()), "Actualización de título");
        cantidad++;
    }

    private static void reporte() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud s = crear(gestor, TipoSolicitud.RECLAMO, null);
        gestor.cambiarEstado(s.getId(), EstadoSolicitud.RESUELTA.name());
        String reporte = gestor.generarReporte();
        verificar(reporte.contains("#1 | Consulta de legajo"), "Detalle de reporte");
        verificar(reporte.contains("TOTAL: 1 | PENDIENTES: 0 | EN PROCESO: 0 | RESUELTAS: 1"), "Totales de reporte");
        cantidad++;
    }

    private static void validaciones() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        boolean fallo = false;
        try {
            gestor.registrar(new DatosRegistroSolicitud(1, "Ana", "correo-invalido", "Consulta",
                    "Detalle", TipoSolicitud.CONSULTA.name(), "", "Alumnos"));
        } catch (IllegalArgumentException e) {
            fallo = true;
        }
        verificar(fallo, "Validación email");
        cantidad++;
    }

    private static void validarTipoInvalido() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        boolean fallo = false;
        try {
            gestor.registrar(new DatosRegistroSolicitud(1, "Ana", "ana@example.com",
                    "Consulta", "Detalle", "DESCONOCIDO", "", "Alumnos"));
        } catch (IllegalArgumentException e) {
            fallo = "Tipo inválido".equals(e.getMessage());
        }
        verificar(fallo, "Validación de tipo inválido");
        cantidad++;
    }

    private static void validarPrioridadInvalida() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        boolean fallo = false;
        try {
            gestor.registrar(new DatosRegistroSolicitud(1, "Ana", "ana@example.com",
                    "Consulta", "Detalle", TipoSolicitud.CONSULTA.name(), "URGENTISIMA", "Alumnos"));
        } catch (IllegalArgumentException e) {
            fallo = "Prioridad inválida".equals(e.getMessage());
        }
        verificar(fallo, "Validación de prioridad inválida");
        cantidad++;
    }

    private static Solicitud crear(GestorSolicitudes gestor, TipoSolicitud tipo, PrioridadSolicitud prioridad) {
        return gestor.registrar(new DatosRegistroSolicitud(1, "Ana", "ana@example.com",
                "Consulta de legajo", "Solicito estado del trámite",
                tipo.name(), prioridad == null ? "" : prioridad.name(), "Alumnos"));
    }

    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError("Fallo: " + mensaje);
    }
}
