
package ar.edu.utn.ms2.servicio;

import ar.edu.utn.ms2.modelo.Responsable;
import ar.edu.utn.ms2.modelo.Solicitud;
import java.util.List;

public class GeneradorReporteSolicitudes {

    public String generar(List<Solicitud> solicitudes) {
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE SOLICITUDES\n");
        int pendientes = 0;
        int enProceso = 0;
        int resueltas = 0;
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getEstado().equals("PENDIENTE")) pendientes++;
            if (solicitud.getEstado().equals("EN_PROCESO")) enProceso++;
            if (solicitud.getEstado().equals("RESUELTA")) resueltas++;
            sb.append("#").append(solicitud.getId()).append(" | ")
                    .append(solicitud.getTitulo()).append(" | ")
                    .append(solicitud.getTipo()).append(" | ")
                    .append(solicitud.getPrioridad()).append(" | ")
                    .append(solicitud.getEstado()).append(" | ")
                    .append(solicitud.getEstudiante().getNombre()).append(" | ");
            Responsable responsable = solicitud.getResponsable();
            if (responsable == null) {
                sb.append("Sin asignar");
            } else {
                sb.append(responsable.formatearParaReporte());
            }
            sb.append("\n");
        }
        sb.append("TOTAL: ").append(solicitudes.size())
                .append(" | PENDIENTES: ").append(pendientes)
                .append(" | EN PROCESO: ").append(enProceso)
                .append(" | RESUELTAS: ").append(resueltas);
        return sb.toString();
    }
}
