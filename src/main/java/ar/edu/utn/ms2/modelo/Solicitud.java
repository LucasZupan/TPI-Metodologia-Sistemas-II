
package ar.edu.utn.ms2.modelo;

public class Solicitud {
    private final int id;
    private final Estudiante estudiante;
    private String titulo;
    private final String descripcion;
    private final TipoSolicitud tipo;
    private final PrioridadSolicitud prioridad;
    private EstadoSolicitud estado;
    private Responsable responsable;

    public Solicitud(int id, Estudiante estudiante, String titulo, String descripcion,
                     String tipo, String prioridad) {
        this.id = id;
        this.estudiante = estudiante;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = TipoSolicitud.valueOf(tipo.trim().toUpperCase());
        this.prioridad = PrioridadSolicitud.valueOf(prioridad.trim().toUpperCase());
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public int getId() { return id; }
    public Estudiante getEstudiante() { return estudiante; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return tipo.name(); }
    public String getPrioridad() { return prioridad.name(); }
    public String getEstado() { return estado.name(); }
    public Responsable getResponsable() { return responsable; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setEstado(String estado) { this.estado = EstadoSolicitud.valueOf(estado.trim().toUpperCase()); }
    public void setResponsable(Responsable responsable) { this.responsable = responsable; }
}

