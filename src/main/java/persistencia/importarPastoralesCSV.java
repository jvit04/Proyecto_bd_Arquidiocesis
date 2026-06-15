package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
//Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarPastoralesCSV implements ExcepcionAmigable {

   public static void importarPastorales(File archivo) throws Exception {
        // La consulta llama a la función insert_pastorales con 3 parámetros
        String sql = "SELECT insert_pastorales(?, ?, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                // Verificamos que tenga las 3 columnas necesarias
                if (datos.length >= 3) {

                    // -- 1. Nombre (Obligatorio) --
                    // Limpieza de BOM para el primer campo
                    String nombre = datos[0].trim().replace("\uFEFF", "");
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. Ámbito (Obligatorio) --
                    String ambito = datos[1].trim();
                    if (ambito.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Ámbito vacío. Registro omitido.");
                        continue;
                    }

                    // -- 3. Descripción (OPCIONAL/NULLABLE) --
                    String descripcion = datos[2].trim();

                    try {
                        // Asignación de parámetros al PreparedStatement

                        // 1. p_nombre
                        pstmt.setString(1, nombre);

                        // 2. p_ambito
                        pstmt.setString(2, ambito);

                        // 3. p_descripcion
                        // Lógica para permitir nulos solo en este campo
                        if (descripcion.isEmpty() || descripcion.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(3, Types.VARCHAR);
                        } else {
                            pstmt.setString(3, descripcion);
                        }

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error numérico (ID) en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error de formato de fecha en línea " + numeroLinea + ": " + e.getMessage());
                    }


                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 3).");
                }
            }

            try{
                pstmt.executeBatch();
            }
            catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Pastorales finalizado.");
        }
    }
}