/**
 * 
 */
package persistencia;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz que modela el metodo que nos establecera una conexion a la base de datos.
 * 
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */

public interface IConexionBD {
    
    public Connection crearConexion() throws SQLException;
    
}
