package ar.edu.utn.ms2.datos;

import ar.edu.utn.ms2.modelo.Solicitud;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudesMemoria implements RepositorioSolicitudes{
    private final List<Solicitud> solicitudes = new ArrayList<>();

    public void guardar(Solicitud solicitud) {
        solicitudes.add(solicitud);
    }

    public Solicitud obtenerPorId(int id) {
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getId() == id) {
                return solicitud;
            }
        }
        return null;
    }

    public List<Solicitud> obtenerTodas() {
        return new ArrayList<>(solicitudes);
    }
}
