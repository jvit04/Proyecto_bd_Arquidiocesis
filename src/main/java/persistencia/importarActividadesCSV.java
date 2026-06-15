package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

// Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarActividadesCSV implements ExcepcionAmigable {

    public static void importarActividades(File archivo) throws Exception {
        String sql = "SELECT insert_actividades(?, ?, ?::DATE, ?::TIME, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;


            DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .toFormatter();

            DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    .appendOptional(DateTimeFormatter.ofPattern("H:mm:ss"))
                    .appendOptional(DateTimeFormatter.ofPattern("HH:mm"))
                    .appendOptional(DateTimeFormatter.ofPattern("H:mm"))
                    .toFormatter();

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                // Verificamos que tenga las 5 columnas necesarias
                if (datos.length >= 5) {

                    // -- 1. Nombre --
                    String nombre = datos[0].trim().replace("\uFEFF", "");
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. ID Lugar Culto --
                    String idLugarStr = datos[1].trim();
                    if (idLugarStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Lugar de Culto vacío. Registro omitido.");
                        continue;
                    }

                    // -- 3. Fecha --
                    String fechaStr = datos[2].trim();
                    if (fechaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Fecha vacía. Registro omitido.");
                        continue;
                    }

                    // -- 4. Hora --
                    String horaStr = datos[3].trim();
                    if (horaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Hora vacía. Registro omitido.");
                        continue;
                    }

                    // -- 5. Responsable --
                    String responsable = datos[4].trim();
                    if (responsable.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Responsable vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros

                        // 1. p_nombre
                        pstmt.setString(1, nombre);

                        // 2. p_id_lugares_culto
                        pstmt.setInt(2, Integer.parseInt(idLugarStr));

                        // 3. p_fecha
                        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);
                        pstmt.setDate(3, Date.valueOf(fecha));

                        // 4. p_hora
                        LocalTime hora = LocalTime.parse(horaStr, timeFormatter);
                        pstmt.setTime(4, Time.valueOf(hora));

                        // 5. p_responsable
                        pstmt.setString(5, responsable);

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Error de formato Fecha/Hora en línea " + numeroLinea + ": " + e.getParsedString());
                    } catch (Exception e) {
                        System.err.println("Error inesperado en línea " + numeroLinea + ": " + e.getMessage());
                    }

                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 5).");
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Actividades finalizado.");
        }
    }
}
