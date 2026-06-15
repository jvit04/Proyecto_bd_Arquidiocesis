package utilities.interfaces;

import clases.Clerigo;
import utilities.Paths;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//interfaz que se encarga de cargar los clerigos almacenados en la base a través de la función fn_parroco
// la cual recoleta los clerigos que son párrocos y no han sido asignados a una parroquia.

public interface cargarClerigos {
    static List<Clerigo> cargar(){
        List<Clerigo> clerigos = new ArrayList<>();


        String sql = "SELECT * FROM fn_parroco()";
        try (Connection conn = DriverManager.getConnection(Paths.UrlBaseDatos, Paths.USER, Paths.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                int idDesdeBD = rs.getInt("p_id_clerigo");
                String nombresDesdeBD = rs.getString("p_nombres");
                String apellidosBD = rs.getString("p_apellidos");


                Clerigo c = new Clerigo(idDesdeBD, nombresDesdeBD,apellidosBD);


                clerigos.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clerigos;
    }
}

