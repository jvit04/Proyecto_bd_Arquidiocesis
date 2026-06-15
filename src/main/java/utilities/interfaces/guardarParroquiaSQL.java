package utilities.interfaces;

import clases.Parroquia;
import utilities.ConexionBD;

import java.sql.*;

//Esta interfaz permite, guardar la parroquia que registra el usuario en la base.
public interface guardarParroquiaSQL {

    static void guardarEnSQL(Parroquia p) throws SQLException {

        String sql = "select insert_parroquia(?,?,?,?,?,?,?,?::DATE,?)";


        try (Connection connection = ConexionBD.conectar();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getVicaria());
            ps.setString(3, p.getDireccion());
            ps.setString(4, p.getCiudad());
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getSitioWeb());
            ps.setDate(8, Date.valueOf(p.getFechaFundacion()));
            ps.setInt(9, p.getClerigo());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String mensajeDeLaBase = rs.getString(1);

                if (mensajeDeLaBase.startsWith("Error") || mensajeDeLaBase.contains("ya existe")) {
                    throw new SQLException(mensajeDeLaBase);
                }


            }
        }
    }
}