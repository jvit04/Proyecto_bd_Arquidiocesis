package utilities.interfaces;

import clases.Vicaria;
import utilities.Paths;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//Metodo cargar, su proposito es que gracias a la función de la base obtener_vicarias devuelva una lista de
//las vicarias que abarca la arquidiócesis, como es un dato relevante para el registro de la parroquia
// se creó su propia clase Vicaria con atributos como nombre e id.
public interface cargarVicarias {
    static List<Vicaria> cargarVicarias(){
        List<Vicaria> vicarias = new ArrayList<>();


        String sql = "SELECT * FROM obtener_vicarias()";
        try (Connection conn = DriverManager.getConnection(Paths.UrlBaseDatos, Paths.USER, Paths.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                int idDesdeBD = rs.getInt("p_id_vicaria");
                String nombresDesdeBD = rs.getString("p_nombre_vicaria");



                Vicaria vicaria = new Vicaria(idDesdeBD, nombresDesdeBD);


                vicarias.add(vicaria);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vicarias;
    }
}

