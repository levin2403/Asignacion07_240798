/**
 * 
 */
package negocio;

import dtos.AlumnoTablaDTO;
import java.util.List;

/**
 * Interfaz que modela los metodos que implementara la clase negocio de alumnos.
 * 
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public interface IAlumnoNegocio {
    public List<AlumnoTablaDTO> alumnosTabla() throws NegocioException;

    public void registrar(String nombres, String apellidoPaterno, String apellidoMaterno) throws NegocioException;

    public void editar(int idAlumno, String nombres, String apellidoPaterno, String apellidoMaterno) throws NegocioException;
    
    public void eliminar(int idAlumno) throws NegocioException;
}
