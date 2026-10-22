
package ar.edu.utn.ms2.app;

import ar.edu.utn.ms2.modelo.Solicitud;
import ar.edu.utn.ms2.servicio.GestorSolicitudes;
import ar.edu.utn.ms2.servicio.DatosRegistroSolicitud;

public class Main {
    public static void main(String[] args) {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud primera = gestor.registrar(new DatosRegistroSolicitud(101, "Ana Pérez", "ana@example.com",
                "Certificado de alumno regular", "Solicito certificado para beca",
                "CERTIFICADO", "", "Alumnos"));
        Solicitud segunda = gestor.registrar(new DatosRegistroSolicitud(102, "Martín Ruiz", "martin@example.com",
                "Error en inscripción", "No aparece mi inscripción a Programación IV",
                "RECLAMO", "", "Alumnos"));
        Solicitud tercera = gestor.registrar(new DatosRegistroSolicitud(103, "Lucía Gómez", "lucia@example.com",
                "Consulta de equivalencias", "Necesito información de equivalencias",
                "CONSULTA", "MEDIA", "Académica"));

        gestor.asignarResponsable(segunda.getId(), 10, "Carla López",
                "Secretaría Académica", "carla@example.com");
        gestor.cambiarEstado(segunda.getId(), "EN_PROCESO");
        gestor.cambiarEstado(primera.getId(), "RESUELTA");

        System.out.println(gestor.generarReporte());
        System.out.println("BÚSQUEDA EN_PROCESO: " + gestor.buscar("EN_PROCESO", null).size());
        System.out.println("BÚSQUEDA MEDIA: " + gestor.buscar(null, "MEDIA").size());
    }
}

