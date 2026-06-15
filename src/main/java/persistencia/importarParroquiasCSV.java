package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

// Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarParroquiasCSV implements ExcepcionAmigable {

    public static void importarParroquias(File archivo) throws Exception {
        String sql = "SELECT insert_parroquia(?, ?, ?, ?, ?, ?, ?, ?::DATE, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .toFormatter();

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                // Verificamos que tenga las 9 columnas
                if (datos.length >= 9) {

                    // -- 1. Nombre (Obligatorio) --
                    String nombre = datos[0].trim().replace("\uFEFF", "");
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. ID Vicaria (Obligatorio) --
                    String idVicariaStr = datos[1].trim();
                    if (idVicariaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Vicaria vacío. Registro omitido.");
                        continue;
                    }

                    // -- 3. Dirección (Obligatorio) --
                    String direccion = datos[2].trim();
                    if (direccion.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Dirección vacía. Registro omitido.");
                        continue;
                    }

                    // -- 4. Ciudad (Obligatorio) --
                    String ciudad = datos[3].trim();
                    if (ciudad.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Ciudad vacía. Registro omitido.");
                        continue;
                    }

                    // -- 8. Fecha Erección (Obligatorio - Validar antes de procesar) --
                    String fechaStr = datos[7].trim();
                    if (fechaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Fecha de Erección vacía. Registro omitido.");
                        continue;
                    }

                    // -- 9. ID Párroco (Obligatorio) --
                    String idParrocoStr = datos[8].trim();
                    if (idParrocoStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Párroco vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros
                        pstmt.setString(1, nombre);
                        pstmt.setInt(2, Integer.parseInt(idVicariaStr));
                        pstmt.setString(3, direccion);
                        pstmt.setString(4, ciudad);

                        // -- 5. Teléfono (OPCIONAL con CORRECCIÓN) --
                        String telefono = datos[4].trim();
                        if (!telefono.isEmpty() && !telefono.equalsIgnoreCase("NULL")) {
                            if ((telefono.length() == 9 || telefono.length() == 8) && !telefono.startsWith("0")) {
                                telefono = "0" + telefono;
                            }
                            pstmt.setString(5, telefono);
                        } else {
                            pstmt.setNull(5, Types.VARCHAR);
                        }

                        // -- 6. Email (OPCIONAL) --
                        String email = datos[5].trim();
                        if (email.isEmpty() || email.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(6, Types.VARCHAR);
                        } else {
                            pstmt.setString(6, email);
                        }

                        // -- 7. Sitio Web (OPCIONAL) --
                        String sitioWeb = datos[6].trim();
                        if (sitioWeb.isEmpty() || sitioWeb.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(7, Types.VARCHAR);
                        } else {
                            pstmt.setString(7, sitioWeb);
                        }

                        // -- 8. Fecha Erección (Con Parseo Flexible) --
                        LocalDate fecha = LocalDate.parse(fechaStr, fmt);
                        pstmt.setDate(8, Date.valueOf(fecha));

                        // -- 9. ID Párroco --
                        pstmt.setInt(9, Integer.parseInt(idParrocoStr));

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error numérico (ID) en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Error de formato de fecha en línea " + numeroLinea + ": " + e.getParsedString());
                    } catch (Exception e) {
                        System.err.println("Error inesperado en línea " + numeroLinea + ": " + e.getMessage());
                    }

                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 9).");
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Parroquias finalizado.");
        }
    }
}