package utilities.interfaces;

import clases.VistaReporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.Paths;

import java.sql.*;

//Este metodo es el que permite cargar la vista desde la base de datos a la aplicación. Tiene su propia clase
// VistaReporte con todas las columnas de la vista como atributos.
public interface cargarVistaReporte {
    static ObservableList<VistaReporte> cargarReporte(){
        ObservableList<VistaReporte> vistas = FXCollections.observableArrayList();


        String sql = "select * from rpt_presupuesto_vicaria";
        try (Connection conn = DriverManager.getConnection(Paths.UrlBaseDatos, Paths.USER, Paths.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String vicariaDesdeBD = rs.getString("Vicaría Responsable");
                int proyectosDesdeBD = rs.getInt("Proyectos totales");
                int enEjecucionDesdeBD = rs.getInt("En Ejecución");
                int planificadosDesdeBD = rs.getInt("Planificados");
                int canceladosSuspendidosDesdeBD = rs.getInt("Cancelados/Suspendidos");
                String presupuestoDesdeBD = rs.getString("Presupuesto Total");
                String inversionDesdeBD = rs.getString("Inversión Promedio");

                VistaReporte vistaReporte = new VistaReporte(vicariaDesdeBD,proyectosDesdeBD,enEjecucionDesdeBD,planificadosDesdeBD,canceladosSuspendidosDesdeBD,presupuestoDesdeBD,inversionDesdeBD);

                vistas.add(vistaReporte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vistas;
    }
}

