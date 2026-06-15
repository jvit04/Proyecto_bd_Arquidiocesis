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

public class importarClerigoCSV implements ExcepcionAmigable {

    public static void importarClerigo(File archivo) throws Exception {
        String sql = "SELECT insert_clerigo(?, ?, ?, ?, ?::DATE, ?::DATE, ?, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            // --- 1. CONFIGURACIÓN DE FECHAS FLEXIBLE ---
            DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .toFormatter();

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                if (datos.length >= 8) {

                    // -- VALIDACIONES BÁSICAS --
                    String nombres = datos[0].trim().replace("\uFEFF", "");
                    if (nombres.isEmpty()) continue;

                    String apellidos = datos[1].trim();
                    if (apellidos.isEmpty()) continue;

                    // --- 2. CORRECCIÓN CÉDULA ---
                    String cedula = datos[2].trim();
                    if (cedula.isEmpty()) continue;
                    // Si tiene 9 dígitos y le falta el 0, se lo ponemos
                    if (cedula.length() == 9 && !cedula.startsWith("0")) {
                        cedula = "0" + cedula;
                    }

                    String rol = datos[3].trim();
                    if (rol.isEmpty()) continue;

                    String fechaNacStr = datos[4].trim();
                    if (fechaNacStr.isEmpty()) continue;

                    String fechaOrdStr = datos[5].trim();
                    if (fechaOrdStr.isEmpty()) continue;

                    try {
                        pstmt.setString(1, nombres);
                        pstmt.setString(2, apellidos);
                        pstmt.setString(3, cedula);
                        pstmt.setString(4, rol);

                        // --- 3. PARSEO DE FECHAS ---
                        LocalDate fechaNac = LocalDate.parse(fechaNacStr, fmt);
                        pstmt.setDate(5, Date.valueOf(fechaNac));

                        LocalDate fechaOrd = LocalDate.parse(fechaOrdStr, fmt);
                        pstmt.setDate(6, Date.valueOf(fechaOrd));

                        // -- Email --
                        String email = datos[6].trim();
                        if (email.isEmpty() || email.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(7, Types.VARCHAR);
                        } else {
                            pstmt.setString(7, email);
                        }

                        // --- 4. CORRECCIÓN TELÉFONO (Agregado) ---
                        String telefono = datos[7].trim();

                        // Lógica de rescate del cero para teléfonos:
                        // Si tiene 9 dígitos (celular sin 0) u 8 (fijo sin 0) y no empieza con 0, lo agregamos.
                        if (!telefono.isEmpty() && !telefono.equalsIgnoreCase("NULL")) {
                            if ((telefono.length() == 9 || telefono.length() == 8) && !telefono.startsWith("0")) {
                                telefono = "0" + telefono;
                            }
                        }

                        if (telefono.isEmpty() || telefono.equalsIgnoreCase("NULL")) {
                            pstmt.setNull(8, Types.VARCHAR);
                        } else {
                            pstmt.setString(8, telefono);
                        }

                        pstmt.addBatch();

                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Error de formato de FECHA en línea " + numeroLinea + ": " + e.getParsedString());
                    } catch (Exception e) {
                        System.err.println("Error en línea " + numeroLinea + ": " + e.getMessage());
                    }
                }
            }

            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Clérigos finalizado.");
        }
    }
}