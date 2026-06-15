package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarResponsableCSV implements ExcepcionAmigable {

    public static void importarResponsable(File archivo) throws Exception {
        String sql = "SELECT insert_responsable(?, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                // Verificamos que tenga las 2 columnas necesarias
                if (datos.length >= 2) {

                    // -- 1. ID Parroquia (Obligatorio - Integer) --
                    String idParroquiaStr = datos[0].trim().replace("\uFEFF", "");
                    if (idParroquiaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Parroquia vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. ID Proyecto (Obligatorio - Integer) --
                    String idProyectoStr = datos[1].trim();
                    if (idProyectoStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Proyecto vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // 1. p_id_parroquia
                        pstmt.setInt(1, Integer.parseInt(idParroquiaStr));

                        // 2. p_id_proyectos
                        pstmt.setInt(2, Integer.parseInt(idProyectoStr));

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error numérico (ID) en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error de formato de fecha en línea " + numeroLinea + ": " + e.getMessage());
                    }


                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 2).");
                }
            }
            try{
                pstmt.executeBatch();
            }
            catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Responsables finalizado.");
        }
    }
}