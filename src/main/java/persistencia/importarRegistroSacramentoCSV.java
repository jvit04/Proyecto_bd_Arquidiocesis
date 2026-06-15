package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

// Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarRegistroSacramentoCSV implements ExcepcionAmigable {

    public static void importarRegistroSacramento(File archivo) throws Exception {
        String sql = "SELECT insert_registro_sacramento(?, ?::DATE, ?, ?)";

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

                if (datos.length >= 4) {

                    // -- 1. Tipo de Sacramento (Obligatorio) --
                    String tipo = datos[0].trim().replace("\uFEFF", "");
                    if (tipo.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Tipo de sacramento vacío. Registro omitido.");
                        continue;
                    }

                    // -- 2. Fecha (Obligatorio) --
                    String fechaStr = datos[1].trim();
                    if (fechaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Fecha vacía. Registro omitido.");
                        continue;
                    }

                    // -- 3. ID Clérigo (Obligatorio - Integer) --
                    String idClerigoStr = datos[2].trim();
                    if (idClerigoStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Clérigo vacío. Registro omitido.");
                        continue;
                    }

                    // -- 4. ID Lugar de Culto (Obligatorio - Integer) --
                    String idLugarStr = datos[3].trim();
                    if (idLugarStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Lugar de Culto vacío. Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros
                        // 1. p_tipo_sacramento
                        pstmt.setString(1, tipo);

                        // 2. p_fecha (Usando el formateador flexible)
                        LocalDate fecha = LocalDate.parse(fechaStr, fmt);
                        pstmt.setDate(2, Date.valueOf(fecha));

                        // 3. p_id_clerigo
                        pstmt.setInt(3, Integer.parseInt(idClerigoStr));

                        // 4. p_id_lugares_culto
                        pstmt.setInt(4, Integer.parseInt(idLugarStr));

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
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 4).");
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Registro de Sacramentos finalizado.");
        }
    }
}