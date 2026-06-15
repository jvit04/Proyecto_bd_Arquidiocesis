package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

// Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarProyectosPastoralesCSV implements ExcepcionAmigable {

    public static void importarProyectosPastorales(File archivo) throws Exception {
        // La función recibe 7 parámetros
        String sql = "SELECT insert_proyectos_pastorales(?, ?, ?, ?::DATE, ?::DATE, ?, ?)";

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


                if (datos.length >= 7) {

                    // -- 1. Nombre --
                    String nombre = datos[0].trim().replace("\uFEFF", "");
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. Descripción --
                    String descripcion = datos[1].trim();
                    if (descripcion.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Descripción vacía. Registro omitido.");
                        continue;
                    }

                    // -- 3. ID Pastorales --
                    String idPastoralesStr = datos[2].trim();
                    if (idPastoralesStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Pastorales vacío. Registro omitido.");
                        continue;
                    }

                    // -- 4. Fecha Inicio --
                    String fechaInicioStr = datos[3].trim();
                    if (fechaInicioStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Fecha de inicio vacía. Registro omitido.");
                        continue;
                    }

                    // -- 5. Fecha Fin (Puede ser NULL) --
                    String fechaFinStr = datos[4].trim();

                    // -- 6. Presupuesto --
                    String presupuestoStr = datos[5].trim();
                    if (presupuestoStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Presupuesto vacío. Registro omitido.");
                        continue;
                    }

                    // -- 7. Estado --
                    String estado = datos[6].trim();
                    if (estado.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Estado vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros
                        // 1. p_nombre
                        pstmt.setString(1, nombre);

                        // 2. p_descripcion
                        pstmt.setString(2, descripcion);

                        // 3. p_id_pastorales
                        pstmt.setInt(3, Integer.parseInt(idPastoralesStr));

                        // 4. p_fecha_inicio
                        LocalDate fechaIni = LocalDate.parse(fechaInicioStr, fmt);
                        pstmt.setDate(4, Date.valueOf(fechaIni));

                        // 5. p_fecha_fin
                        if (fechaFinStr.isEmpty() || fechaFinStr.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(5, Types.DATE);
                        } else {
                            LocalDate fechaFin = LocalDate.parse(fechaFinStr, fmt);
                            pstmt.setDate(5, Date.valueOf(fechaFin));
                        }

                        // 6. p_presupuesto (Numeric)
                        String presupuestoLimpio = presupuestoStr.replace(",", ".");
                        pstmt.setBigDecimal(6, new BigDecimal(presupuestoLimpio));

                        // 7. p_estado
                        pstmt.setString(7, estado);

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error numérico (ID o Presupuesto) en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Error de formato de fecha en línea " + numeroLinea + ": " + e.getParsedString());
                    } catch (Exception e) {
                        System.err.println("Error inesperado en línea " + numeroLinea + ": " + e.getMessage());
                    }

                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 7).");
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Proyectos Pastorales finalizado.");
        }
    }
}