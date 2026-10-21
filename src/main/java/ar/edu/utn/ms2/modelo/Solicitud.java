package ar.edu.utn.ms2.modelo;

public class Solicitud {
    private final int id;
    private final Estudiante estudiante;
    private String titulo;
    private final String descripcion;
    private final String tipo;
    private final String prioridad;
    private String estado;
    private Responsable responsable;

    public Solicitud(int id, Estudiante estudiante, String titulo, String descripcion,
                     String tipo, String prioridad) {
        this.id = id;
        this.estudiante = estudiante;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.estado = "PENDIENTE";
    }

    public int getId() { return id; }
    public Estudiante getEstudiante() { return estudiante; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return tipo; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public Responsable getResponsable() { return responsable; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setResponsable(Responsable responsable) { this.responsable = responsable; }
}
