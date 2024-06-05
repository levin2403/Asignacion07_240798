/**
 * 
 */
package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que implementa IConexionBD para establecer conexion con una base de datos.
 * 
 * @author @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public class ConexionBD implements IConexionBD {
    
    //Definimos las variables que componen la conexion
    final String SERVER = "localhost";
    final String BASE_DATOS = "crud";
    private final String CADENA_CONEXION = "jdbc:mysql://" + SERVER + "/" + BASE_DATOS;
    final String USUARIO = "root";
    final String CONTRASEÑA = "Saymyname15";
    
    /**
     * Metodo que establece una conexion a la base de datos crud.
     * 
     * @return Objeto de tipo conexion.
     * @throws SQLException Arroja una excepcion en caso de error al hacer la conexion.
     */
    @Override
    public Connection crearConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASEÑA);
        return conexion;   
    }
}