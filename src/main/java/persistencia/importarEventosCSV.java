package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

// Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarEventosCSV implements ExcepcionAmigable {

    public static void importarEventos(File archivo) throws Exception {
        // insert_evento tiene 6 parámetros
        String sql = "SELECT insert_evento(?, ?, ?::TIMESTAMP, ?::TIMESTAMP, ?, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            // --- FORMATEADOR FLEXIBLE PARA FECHA Y HORA ---
            DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss"))
                    .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                    .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"))
                    .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                // Verificamos que tenga las 6 columnas necesarias
                if (datos.length >= 6) {

                    // -- 1. Nombre (Obligatorio) --
                    String nombre = datos[0].trim().replace("\uFEFF", "");
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. Categoría (Obligatorio) --
                    String categoria = datos[1].trim();
                    if (categoria.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Categoría vacía. Registro omitido.");
                        continue;
                    }

                    // -- 3. Fecha Inicio (Obligatorio) --
                    String fechaIniStr = datos[2].trim();
                    if (fechaIniStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Fecha Inicio vacía. Registro omitido.");
                        continue;
                    }

                    // -- 4. Fecha Fin (Obligatorio) --
                    String fechaFinStr = datos[3].trim();
                    if (fechaFinStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Fecha Fin vacía. Registro omitido.");
                        continue;
                    }

                    // -- 5. ID Lugar Culto (Obligatorio - Integer) --
                    String idLugarStr = datos[4].trim();
                    if (idLugarStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Lugar Culto vacío. Registro omitido.");
                        continue;
                    }

                    // -- 6. Presupuesto (Obligatorio - Numeric) --
                    String presupuestoStr = datos[5].trim();
                    if (presupuestoStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Presupuesto vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros

                        // 1. p_nombre
                        pstmt.setString(1, nombre);

                        // 2. p_categoria
                        pstmt.setString(2, categoria);

                        // 3. p_fecha_hora_inicio (Usando el formateador flexible)
                        pstmt.setObject(3, LocalDateTime.parse(fechaIniStr, fmt));

                        // 4. p_fecha_hora_fin (Usando el formateador flexible)
                        pstmt.setObject(4, LocalDateTime.parse(fechaFinStr, fmt));

                        // 5. p_id_lugar_culto
                        pstmt.setInt(5, Integer.parseInt(idLugarStr));

                        // 6. p_presupuesto
                        pstmt.setBigDecimal(6, new BigDecimal(presupuestoStr.replace(",", ".")));

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error numérico (ID o Presupuesto) en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (DateTimeParseException e) {
                        System.err.println("Error de formato de Fecha/Hora en línea " + numeroLinea + ": '" + e.getParsedString() + "'. Verifique el formato (ej: 28/03/2024 10:00:00).");
                    } catch (Exception e) {
                        System.err.println("Error inesperado en línea " + numeroLinea + ": " + e.getMessage());
                    }

                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 6).");
                }
            }


            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Eventos finalizado.");
        }
    }
}
