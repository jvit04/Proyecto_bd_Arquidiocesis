package persistencia;

import utilities.ConexionBD;
import utilities.interfaces.ExcepcionAmigable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//Permite importar desde un CSV los datos de la tabla correspondiente a la base de datos.
public class importarLugaresCultoCSV implements ExcepcionAmigable{

    public static void importarLugaresCulto(File archivo) throws Exception {
        String sql = "SELECT insert_lugares_culto(?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] datos = linea.split(";", -1);

                // Verificamos que tenga las 6 columnas necesarias
                if (datos.length >= 6) {

                    // -- 1. ID Parroquia (Integer) --
                    // Limpiamos BOM aquí por si es el primer dato
                    String idParroquiaStr = datos[0].trim().replace("\uFEFF", "");
                    if (idParroquiaStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": ID Parroquia vacío (Obligatorio). Registro omitido.");
                        continue;
                    }

                    // -- 2. Nombre (Varchar) --
                    String nombre = datos[1].trim();
                    if (nombre.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Nombre vacío (Obligatorio). Registro omitido.");
                        continue;
                    }

                    // -- 3. Tipo (Varchar) --
                    String tipo = datos[2].trim();
                    if (tipo.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Tipo vacío (Obligatorio). Registro omitido.");
                        continue;
                    }

                    // -- 4. Dirección (Text) --
                    String direccion = datos[3].trim();
                    if (direccion.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Dirección vacía (Obligatorio). Registro omitido.");
                        continue;
                    }

                    // -- 5. Capacidad (Integer) --
                    String capacidadStr = datos[4].trim();
                    if (capacidadStr.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Capacidad vacía (Obligatorio). Registro omitido.");
                        continue;
                    }

                    // -- 6. Estado (Varchar) --
                    String estado = datos[5].trim();
                    if (estado.isEmpty()) {
                        System.err.println("Línea " + numeroLinea + ": Estado vacío (Obligatorio). Registro omitido.");
                        continue;
                    }

                    try {
                        // Asignación de parámetros en el orden exacto de la función:

                        // 1. p_id_parroquia (int)
                        pstmt.setInt(1, Integer.parseInt(idParroquiaStr));

                        // 2. p_nombre (varchar)
                        pstmt.setString(2, nombre);

                        // 3. p_tipo (varchar)
                        pstmt.setString(3, tipo);

                        // 4. p_direccion (text)
                        pstmt.setString(4, direccion);

                        // 5. p_capacidad (int)
                        pstmt.setInt(5, Integer.parseInt(capacidadStr));

                        // 6. p_estado (varchar)
                        pstmt.setString(6, estado);

                        // Añadir al lote
                        pstmt.addBatch();

                    } catch (NumberFormatException e) {
                        System.err.println("Error numérico (ID) en línea " + numeroLinea + ": " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error de formato de fecha en línea " + numeroLinea + ": " + e.getMessage());
                    }

                } else {
                    System.err.println("Línea " + numeroLinea + " omitida: Columnas insuficientes (se esperan 6).");
                }
            }

            try{
                pstmt.executeBatch();
            }
            catch (SQLException e) {
                ExcepcionAmigable.verificarErrorAmigable(e);
            }
            System.out.println("Proceso de importación de Lugares de Culto finalizado.");
        }
    }
}