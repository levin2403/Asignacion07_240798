/**
 *
 */
package persistencia;

import entidad.AlumnoEntidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798.
 */
public class AlumnoDAO implements IAlumnoDAO {

    private IConexionBD conexionBD;

    public AlumnoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    /**
     * Metodo que nos devuelve una lista de todos los alumnos.
     * 
     * @return Lista de los alumnos.
     * @throws PersistenciaException Arroja una excepecion en caso de error en la base de datos
     */
    @Override
    public List<AlumnoEntidad> buscarAlumnosTabla() throws PersistenciaException {
        try {
            List<AlumnoEntidad> alumnosLista = null;
            Connection conexion = this.conexionBD.crearConexion();
            String codigoSQL = "SELECT idAlumno, nombres, apellidoPaterno, apellidoMaterno, eliminado, activo FROM alumnos";
            Statement comandoSQL = conexion.createStatement();
            ResultSet resultado = comandoSQL.executeQuery(codigoSQL);
            while (resultado.next()) {
                if (alumnosLista == null) {
                    alumnosLista = new ArrayList<>();
                }
                AlumnoEntidad alumno = this.convertirAEntidad(resultado);
                alumnosLista.add(alumno);
            }
            conexion.close();
            return alumnosLista;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new PersistenciaException("Ocurrió un error al leer la base de datos, inténtelo de nuevo.");
        }

    }


    /**
     * Metodo que registra a un alumno dado en el parametro.
     * 
     * @param alumno Objeto de tipo alumno que deseemos eliminar.
     * @throws PersistenciaException Arroja una excepecion en caso de error en la base de datos.
     */
    @Override
    public void registrar(AlumnoEntidad alumno) throws PersistenciaException {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;

        try {
            conexion = conexionBD.crearConexion();
            conexion.setAutoCommit(false);

            String sentenciaSql = "INSERT INTO alumnos (nombres, apellidoPaterno, apellidoMaterno, eliminado, activo) VALUES (?, ?, ?, ?, ?);";
            preparedStatement = conexion.prepareStatement(sentenciaSql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, alumno.getNombres());
            preparedStatement.setString(2, alumno.getApellidoPaterno());
            preparedStatement.setString(3, alumno.getApellidoMaterno());
            preparedStatement.setBoolean(4, alumno.isEliminado());
            preparedStatement.setBoolean(5, alumno.isActivo());

            preparedStatement.executeUpdate();

            resultado = preparedStatement.getGeneratedKeys();
            if (resultado.next()) {
                alumno.setIdAlumno(resultado.getInt(1));
            }

            conexion.commit();//enviamos los datos a la base de datos.
            
        } catch (SQLException ex) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException e) {
                    throw new PersistenciaException("Error al revertir la transacción");
                }
            }
            throw new PersistenciaException("Error al registrar el alumno");
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                    preparedStatement.close();
                    conexion.close();
                } 
            } catch (SQLException e) {
                throw new PersistenciaException("Error al cerrar los recursos");
            }
        }
    }

    /**
     * edita a un alumno dado en el parametro dado.
     * 
     * @param alumno Objeto de tipo alumno que deseemos eliminar.
     * @throws PersistenciaException Arroja una excepecion en caso de error en la base de datos.
     */
    @Override
    public void editar(AlumnoEntidad alumno) throws PersistenciaException {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;

        try {
            conexion = conexionBD.crearConexion();
            String sentenciaSql = "UPDATE alumnos SET nombres = ?, apellidoPaterno = ?, apellidoMaterno = ? WHERE idAlumno = ?";
            preparedStatement = conexion.prepareStatement(sentenciaSql);
            preparedStatement.setString(1, alumno.getNombres());
            preparedStatement.setString(2, alumno.getApellidoPaterno());
            preparedStatement.setString(3, alumno.getApellidoMaterno());
            preparedStatement.setInt(4, alumno.getIdAlumno());

            preparedStatement.executeUpdate(); //ejecutamos el comanda
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al editar el alumno: " + ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                throw new PersistenciaException("Error al cerrar los recursos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Metodo que elimina a un alumno dado en el parametro de la base de datos
     * 
     * @param id id del alumno que deseamos eliminar.
     * @throws PersistenciaException Arroja una excepecion en caso de error en la base de datos.
     */
    @Override
    public void eliminar(int id) throws PersistenciaException {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;

        try {
            conexion = conexionBD.crearConexion();
            String sentenciaSql = "DELETE FROM alumnos WHERE idAlumno = ?";
            preparedStatement = conexion.prepareStatement(sentenciaSql);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al eliminar el alumno: " + ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                throw new PersistenciaException("Error al cerrar los recursos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Metodo que recibe un objeto de tipo ResultSet y lo convierte a un
     * Objeto de tipo AlumnoEntidad.
     * 
     * @param resultado Objeto de tipo ResultSet
     * @return Objeto de tipo AlumnoEntidad ya convertido.
     * @throws PersistenciaException Arroja una excepecion en caso de error en la base de datos.
     */
    private AlumnoEntidad convertirAEntidad(ResultSet resultado) throws SQLException {
        int id = resultado.getInt("idAlumno");
        String nombre = resultado.getString("nombres");
        String paterno = resultado.getString("apellidoPaterno");
        String materno = resultado.getString("apellidoMaterno");
        boolean eliminado = resultado.getBoolean("eliminado");
        boolean activo = resultado.getBoolean("activo");
        return new AlumnoEntidad(id, nombre, paterno, materno, eliminado, activo);
    }


}
