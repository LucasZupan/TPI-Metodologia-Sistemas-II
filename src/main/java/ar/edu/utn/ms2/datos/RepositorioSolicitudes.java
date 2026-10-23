
package ar.edu.utn.ms2.datos;

import ar.edu.utn.ms2.modelo.Solicitud;
import java.util.List;

public interface RepositorioSolicitudes {
    void guardar(Solicitud solicitud);
    Solicitud obtenerPorId(int id);
    List<Solicitud> obtenerTodas();
}
