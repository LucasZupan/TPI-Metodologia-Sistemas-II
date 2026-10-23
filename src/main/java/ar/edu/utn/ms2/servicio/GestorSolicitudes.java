package ar.edu.utn.ms2.servicio;

import ar.edu.utn.ms2.datos.RepositorioSolicitudesMemoria;
import ar.edu.utn.ms2.datos.RepositorioSolicitudes;
import ar.edu.utn.ms2.modelo.Estudiante;
import ar.edu.utn.ms2.modelo.Responsable;
import ar.edu.utn.ms2.modelo.Solicitud;
import java.util.ArrayList;
import java.util.List;

/** Primera implementación funcional. Es una base de trabajo para evolución del diseño. */
public class GestorSolicitudes {
    private final RepositorioSolicitudes repositorio;
    private int proximoId = 1;

    public GestorSolicitudes() {
        this(new RepositorioSolicitudesMemoria());
    }

    public GestorSolicitudes(RepositorioSolicitudes repositorio) {
        this.repositorio = repositorio;
    }

    public Solicitud registrar(DatosRegistroSolicitud datos) {
        validarDatosRegistro(datos.nombreEstudiante(), datos.emailEstudiante(),
                datos.titulo(), datos.descripcion(), datos.tipo(), datos.area());

        String prioridadFinal = determinarPrioridad(datos.tipo(), datos.prioridad());

        // Se conserva el dato de área como parte de la descripción operativa.
        String descripcionCompleta = datos.descripcion().trim() + " [Área: " + datos.area().trim() + "]";
        Estudiante estudiante = new Estudiante(datos.estudianteId(),
                datos.nombreEstudiante().trim(), datos.emailEstudiante().trim());
        Solicitud solicitud = new Solicitud(proximoId++, estudiante, datos.titulo().trim(),
                descripcionCompleta, datos.tipo().trim().toUpperCase(), prioridadFinal.trim().toUpperCase());
        repositorio.guardar(solicitud);
        return solicitud;
    }

    private void validarDatosRegistro(String nombreEstudiante, String emailEstudiante,
                                      String titulo, String descripcion, String tipo, String area) {
        validarNombre(nombreEstudiante);
        validarEmail(emailEstudiante);

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título requerido");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("Descripción requerida");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo requerido");
        }
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("Área requerida");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    private String determinarPrioridad(String tipo, String prioridad) {
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
        return prioridadFinal;
    }

    public void asignarResponsable(int solicitudId, int responsableId, String nombreResponsable,
                                   String areaResponsable, String emailResponsable) {
        Solicitud solicitud = repositorio.obtenerPorId(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("No existe la solicitud");
        }
        validarNombre(nombreResponsable);
        validarEmail(emailResponsable);
        if (areaResponsable == null || areaResponsable.isBlank()) {
            throw new IllegalArgumentException("Área requerida");
        }
        Responsable responsable = new Responsable(responsableId, nombreResponsable.trim(),
                areaResponsable.trim(), emailResponsable.trim());
        solicitud.setResponsable(responsable);
    }

    public void cambiarEstado(int solicitudId, String nuevoEstado) {
        Solicitud solicitud = repositorio.obtenerPorId(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("No existe la solicitud");
        }
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("Estado requerido");
        }
        String estado = nuevoEstado.trim().toUpperCase();
        if (!estado.equals("PENDIENTE") && !estado.equals("EN_PROCESO") && !estado.equals("RESUELTA")) {
            throw new IllegalArgumentException("Estado inválido");
        }
        solicitud.setEstado(estado);
    }

    public void cambiarTitulo(int solicitudId, String nuevoTitulo) {
        Solicitud solicitud = repositorio.obtenerPorId(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("No existe la solicitud");
        }
        if (nuevoTitulo == null || nuevoTitulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título requerido");
        }
        solicitud.setTitulo(nuevoTitulo.trim());
    }

    public List<Solicitud> buscar(String estado, String prioridad) {
        List<Solicitud> resultado = new ArrayList<>();
        for (Solicitud solicitud : repositorio.obtenerTodas()) {
            if ((estado == null || solicitud.getEstado().equalsIgnoreCase(estado))
                    && (prioridad == null || solicitud.getPrioridad().equalsIgnoreCase(prioridad))) {
                resultado.add(solicitud);
            }
        }
        return resultado;
    }

    public String generarReporte() {
        GeneradorReporteSolicitudes generador = new GeneradorReporteSolicitudes();
        return generador.generar(repositorio.obtenerTodas());
    }

    public Solicitud consultarPorId(int id) {
        return repositorio.obtenerPorId(id);
    }
}
