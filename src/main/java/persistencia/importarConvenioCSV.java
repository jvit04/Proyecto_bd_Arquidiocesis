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
public class importarConvenioCSV implements ExcepcionAmigable {

    public static void importarConvenio(File archivo) throws Exception {
        // insert_convenio recibe 8 parámetros
        String sql = "SELECT insert_convenio(?, ?, ?, ?::DATE, ?::DATE, ?, ?, ?)";

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

                // Verificamos que tenga las 8 columnas necesarias
                if (datos.length >= 8) {

                    // -- 1. Nombre --
                    String nombre = datos[0].trim().replace("\uFEFF", "");
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. Institución --
                    String institucion = datos[1].trim();
                    if (institucion.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Institución vacía. Registro omitido.");
                        continue;
                    }

                    // -- 3. Objetivo --
                    String objetivo = datos[2].trim();
                    if (objetivo.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Objetivo vacío. Registro omitido.");
                        continue;
                    }

                    // -- 4. Fecha Firmante (Obligatorio) --
                    String fechaFirmaStr = datos[3].trim();
                    if (fechaFirmaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Fecha de firma vacía. Registro omitido.");
                        continue;
                    }

                    // -- 5. Fecha Vencimiento (OPCIONAL/NULLABLE) --
                    String fechaVencimientoStr = datos[4].trim();

                    // -- 6. ID Vicaria --
                    String idVicariaStr = datos[5].trim();
                    if (idVicariaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Vicaria vacío. Registro omitido.");
                        continue;
                    }

                    // -- 7. ID Clérigo --
                    String idClerigoStr = datos[6].trim();
                    if (idClerigoStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Clérigo vacío. Registro omitido.");
                        continue;
                    }

                    // -- 8. Estado --
                    String estado = datos[7].trim();
                    if (estado.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Estado vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros

                        pstmt.setString(1, nombre);
                        pstmt.setString(2, institucion);
                        pstmt.setString(3, objetivo);

                        // -- 4. Fecha Firmante
                        LocalDate fechaFirma = LocalDate.parse(fechaFirmaStr, fmt);
                        pstmt.setDate(4, Date.valueOf(fechaFirma));

                        // -- 5. Fecha Vencimiento
                        if (fechaVencimientoStr.isEmpty() || fechaVencimientoStr.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(5, Types.DATE);
                        } else {
                            LocalDate fechaVenc = LocalDate.parse(fechaVencimientoStr, fmt);
                            pstmt.setDate(5, Date.valueOf(fechaVenc));
                        }

                        // Parseo de enteros
                        pstmt.setInt(6, Integer.parseInt(idVicariaStr));
                        pstmt.setInt(7, Integer.parseInt(idClerigoStr));

                        pstmt.setString(8, estado);

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
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes.");
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Convenios finalizado.");
        }
    }
}
