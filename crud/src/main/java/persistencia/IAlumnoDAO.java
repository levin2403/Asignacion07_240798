/**
 * 
 */
package persistencia;

import entidad.AlumnoEntidad;
import java.util.List;

/**
 * Interfaz que modela los metodos que implementaran Los DAO de alumno
 * 
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public interface IAlumnoDAO {
    
    public List<AlumnoEntidad> buscarAlumnosTabla() throws PersistenciaException;

    public void registrar(AlumnoEntidad nuevoAlumno) throws PersistenciaException;

    public void editar(AlumnoEntidad alumno) throws PersistenciaException;
    
    public void eliminar(int idAlumno) throws PersistenciaException;
    
}
