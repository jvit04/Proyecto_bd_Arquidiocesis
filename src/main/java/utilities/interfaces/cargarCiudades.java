package utilities.interfaces;

import utilities.Paths;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//Metodo cargar, su proposito es que gracias a la función de la base obtener_ciudades, devuelva una lista de
//las ciudades que abarca la arquidiócesis
public interface cargarCiudades {
    static List<String> cargar() {
        List<String> ciudades = new ArrayList<>();


        String sql = "SELECT * FROM obtener_ciudades()";
        try (Connection conn = DriverManager.getConnection(Paths.UrlBaseDatos, Paths.USER, Paths.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String nombresDesdeBD = rs.getString("ciudades");

                ciudades.add(nombresDesdeBD);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ciudades;
    }
}

