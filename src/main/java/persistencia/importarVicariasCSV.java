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

// Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarVicariasCSV implements ExcepcionAmigable {

    public static void importarVicarias(File archivo) throws Exception {
        String sql = "SELECT insert_vicaria(?, ?, ?, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                if (datos.length >= 4) {

                    // -- 1. Nombre (Obligatorio) --
                    String nombre = datos[0].trim().replace("\uFEFF", "");
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre de Vicaria vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. Descripción (OPCIONAL/NULLABLE) --
                    String descripcion = datos[1].trim();

                    // -- 3. Email (Obligatorio) --
                    String email = datos[2].trim();
                    if (email.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Email vacío. Registro omitido.");
                        continue;
                    }

                    // -- 4. Teléfono (Obligatorio con corrección) --
                    String telefono = datos[3].trim();

                    // CORRECCIÓN DE TELÉFONO:
                    // Si Excel quitó el cero (9 dígitos), lo reponemos.
                    if (!telefono.isEmpty()) {
                        if (telefono.length() == 9 && !telefono.startsWith("0")) {
                            telefono = "0" + telefono;
                        }
                    }

                    // Validación final de vacío
                    if (telefono.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Teléfono vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros

                        // 1. p_nombre
                        pstmt.setString(1, nombre);

                        // 2. p_descripcion (Único NULLABLE)
                        if (descripcion.isEmpty() || descripcion.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(2, Types.VARCHAR);
                        } else {
                            pstmt.setString(2, descripcion);
                        }

                        // 3. p_email
                        pstmt.setString(3, email);

                        // 4. p_telefono (Corregido)
                        pstmt.setString(4, telefono);

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (Exception e) {
                        System.err.println("Error inesperado en línea " + numeroLinea + ": " + e.getMessage());
                    }

                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 4).");
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Vicarías finalizado.");
        }
    }
}