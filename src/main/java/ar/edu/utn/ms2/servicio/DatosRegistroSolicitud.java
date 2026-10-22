package ar.edu.utn.ms2.servicio;

public record DatosRegistroSolicitud(
        int estudianteId,
        String nombreEstudiante,
        String emailEstudiante,
        String titulo,
        String descripcion,
        String tipo,
        String prioridad,
        String area
) {
}