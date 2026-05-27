package org.example;
import java.sql.*;
import java.util.HashMap;

public class DAO {

    private String url;
    private String usuario;
    private String password;

    
    public DAO(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    public void guardarEstadisticas(String winnerName, HashMap<String, Integer> liesCaught) {

        // Recorremos el HashMap jugador por jugador usando sus nombres
        for (String playerName : liesCaught.keySet()) {

            // Si el jugador actual es el ganador se lleva 1 victoria, si no, 0
            int won = playerName.equals(winnerName) ? 1 : 0;
            // Sacamos el número de veces que ha sido pillado mintiendo
            int lies = liesCaught.get(playerName);

            // Intentamos hacer un UPDATE por si el jugador ya existía en la tabla
            String sqlUpdate = "UPDATE stats_players SET games_won = games_won + ?, total_lies_caught = total_lies_caught + ? WHERE name = ?";
            int filasModificadas = 0;

            try (Connection conexion = DriverManager.getConnection(url, usuario, password);
                 PreparedStatement sentenciaUpdate = conexion.prepareStatement(sqlUpdate)) {

                // Colocamos las variables en los interrogantes del UPDATE
                sentenciaUpdate.setInt(1, won);
                sentenciaUpdate.setInt(2, lies);
                sentenciaUpdate.setString(3, playerName);

                // Guardamos cuántas filas han sido afectadas en la base de datos
                filasModificadas = sentenciaUpdate.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Error al intentar actualizar: " + e.getMessage());
            }

            // Si filasModificadas es 0 significa que el jugador es nuevo
            if (filasModificadas == 0) {
                // Sentencia clásica para insertar un nuevo registro desde cero
                String sqlInsert = "INSERT INTO stats_players (name, games_won, total_lies_caught) VALUES (?, ?, ?)";

                try (Connection conexion = DriverManager.getConnection(url, usuario, password);
                     PreparedStatement sentenciaInsert = conexion.prepareStatement(sqlInsert)) {

                    // Colocamos los datos del nuevo jugador en los interrogantes del INSERT
                    sentenciaInsert.setString(1, playerName);
                    sentenciaInsert.setInt(2, won);
                    sentenciaInsert.setInt(3, lies);

                    // Ejecutamos la inserción en la tabla
                    sentenciaInsert.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("Error al intentar insertar: " + e.getMessage());
                }
            }
        }
        System.out.println("[DAO] Estadísticas de la partida guardadas correctamente.");
    }

    public void mostrarLeaderboard() {
        // Selecciona todos los jugadores ordenando por más victorias y menos mentiras
        String sql = "SELECT name, games_won, total_lies_caught FROM stats_players ORDER BY games_won DESC, total_lies_caught ASC";

        System.out.println("\n========= GLOBAL LEADERBOARD (POSTGRESQL) =========");
        System.out.printf("%-15s | %-12s | %-18s\n", "PLAYER", "GAMES WON", "TOTAL LIES CAUGHT");
        System.out.println("----------------------------------------------------");

        try (Connection conexion = DriverManager.getConnection(url, usuario, password);
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) { // executeQuery se usa para los SELECT

            // Variable para controlar si la tabla no tiene ninguna fila guardada
            boolean tieneDatos = false;

            // Recorremos las filas que nos ha devuelto la consulta una por una
            while (resultado.next()) {
                tieneDatos = true; // Si entra aquí al menos una vez es que sí hay datos

                // Extraemos los datos de las columnas usando sus nombres exactos de la BD
                String name = resultado.getString("name");
                int won = resultado.getInt("games_won");
                int lies = resultado.getInt("total_lies_caught");

                // Pintamos la fila con un formato de espacios fijos ordenados
                System.out.printf("%-15s | %-12d | %-18d\n", name, won, lies);
            }

            // Si la tabla estaba vacía y el bucle while no se ejecutó, avisamos
            if (!tieneDatos) {
                System.out.println("No hay estadísticas registradas todavía. ¡Juega una partida!");
            }
            System.out.println("====================================================\n");

        } catch (SQLException e) {
            System.out.println("Error al intentar leer el ranking: " + e.getMessage());
        }
    }
}
