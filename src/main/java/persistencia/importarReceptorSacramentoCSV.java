package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarReceptorSacramentoCSV implements ExcepcionAmigable {

    public static void importarReceptorSacramento(File archivo) throws Exception {
        String sql = "SELECT insert_receptor_sacramento(?, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                if (datos.length >= 2) {

                    // -- 1. Cédula (Obligatorio - Varchar) --
                    String cedula = datos[0].trim().replace("\uFEFF", "");
                    if (cedula.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": La cédula está vacía. Registro omitido.");
                        continue;
                    }

                    if (cedula.length() == 9 && !cedula.startsWith("0")) {
                        cedula = "0" + cedula;
                    }

                    // -- 2. ID Registro Sacramento (Obligatorio - Integer) --
                    String idRegistroStr = datos[1].trim();
                    if (idRegistroStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": El ID Registro Sacramento está vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros
                        // 1. p_cedula
                        pstmt.setString(1, cedula);

                        // 2. p_id_registro_sacramento
                        pstmt.setInt(2, Integer.parseInt(idRegistroStr));

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico (ID) en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error inesperado en línea " + numeroLinea + ": " + e.getMessage());
                    }

                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 2).");
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Receptor Sacramento finalizado.");
        }
    }
}