package persistencia; // O el paquete donde la tengas (ej: persistencia)
import utilities.ConexionBD;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class importarSQL {

    public static void restaurarBaseDatos(File archivoSQL) throws SQLException, IOException {
        String s;
        StringBuilder sb = new StringBuilder();

        try (Connection conn = ConexionBD.conectar();
             FileReader fr = new FileReader(archivoSQL);
             BufferedReader br = new BufferedReader(fr)) {

            conn.setAutoCommit(false);
            Statement st = conn.createStatement();

            // Verificamos si podemos usar las funciones avanzadas de Postgres
            CopyManager copyManager = null;
            if (conn.isWrapperFor(BaseConnection.class)) {
                copyManager = new CopyManager(conn.unwrap(BaseConnection.class));
            }

            while ((s = br.readLine()) != null) {
                String lineaTrim = s.trim();

                // Ignorar líneas vacías o comentarios
                if (lineaTrim.isEmpty() || lineaTrim.startsWith("--") || lineaTrim.startsWith("//") || lineaTrim.startsWith("#")) {
                    continue;
                }

                // Acumulamos la línea en el buffer (agregamos espacio o salto de línea para evitar que se peguen palabras)
                sb.append(s).append("\n");

                // Si la línea termina en punto y coma, significa que el comando terminó
                if (lineaTrim.endsWith(";")) {
                    String sqlCommand = sb.toString().trim();

                    // AQUI ESTA LA CORRECCIÓN:
                    // Verificamos el comando COMPLETO, sin importar mayúsculas o saltos de línea
                    if (copyManager != null &&
                            sqlCommand.toUpperCase().startsWith("COPY") &&
                            sqlCommand.toUpperCase().contains("FROM STDIN")) {

                        // Si es un COPY FROM STDIN, usamos el copyManager
                        // El copyManager leerá las siguientes líneas del BufferedReader (br) automáticamente
                        copyManager.copyIn(sqlCommand, br);

                    } else {
                        // Si es cualquier otro comando (INSERT, CREATE, etc.), usamos execute normal
                        st.execute(sqlCommand);
                    }

                    // Limpiamos el buffer para el siguiente comando
                    sb = new StringBuilder();
                }
            }

            // Ejecutar si quedó algo pendiente en el buffer (raro, pero posible)
            if (sb.length() > 0 && !sb.toString().trim().isEmpty()) {
                st.execute(sb.toString());
            }

            conn.commit();
            System.out.println("Restauración completada con éxito.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Relanzamos para que el Controller muestre la alerta
        }
    }
}