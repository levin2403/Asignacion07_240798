/**
 *
 */
package negocio;

import dtos.AlumnoTablaDTO;
import entidad.AlumnoEntidad;
import java.util.ArrayList;
import java.util.List;
import persistencia.IAlumnoDAO;
import persistencia.PersistenciaException;

/**
 * Clase que impenta la interfaz IAlumnoNegocio y envuelve los metodos de 
 * capas inferiores en DTOs para su uso en capas superiores
 *
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public class AlumnoNegocio implements IAlumnoNegocio {

    //inicializamos la variable de clase 
    private IAlumnoDAO alumnoDAO;

    //inicializamos el constructor de la clase
    public AlumnoNegocio(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }

    /**
     * Metodo que envuelve al metodo de buscarAlumnosTabla de persistencia
     * en un DTO para su uso en capas superiores.
     * 
     * @return Lista de alumnos registrados.
     * @throws NegocioException Excepcion en caso de error el metodo.
     */
    @Override
    public List<AlumnoTablaDTO> alumnosTabla() throws NegocioException {
        try {
            List<AlumnoEntidad> alumnos = this.alumnoDAO.buscarAlumnosTabla();
            return this.convertirAlumnoTablaDTO(alumnos);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    
    /**
     * Metodo que envuelve al metodo de registro de alumnos de persistencia
     * en un DTO para su uso en capas superiores.
     * 
     * @param nombres nombre del alumno.
     * @param apellidoPaterno apellido paterno del alumno.
     * @param apellidoMaterno apellido materno del alumno.
     * @throws NegocioException Excepcion en caso de error el metodo.
     */
    @Override
    public void registrar(String nombres, String apellidoPaterno, String apellidoMaterno) throws NegocioException {
        if (nombres == null || nombres.trim().isEmpty() || apellidoPaterno == null || apellidoPaterno.trim().isEmpty()) {
            throw new NegocioException(" ingrese la informacion completa.");
        }

        AlumnoEntidad nuevoAlumno = new AlumnoEntidad(0, nombres, apellidoPaterno, apellidoMaterno, true, true);
        nuevoAlumno.setNombres(nombres.trim());
        nuevoAlumno.setApellidoPaterno(apellidoPaterno.trim());
        nuevoAlumno.setApellidoMaterno(apellidoMaterno != null ? apellidoMaterno.trim() : null);
        nuevoAlumno.setEliminado(false);
        nuevoAlumno.setActivo(true);

        try {
            alumnoDAO.registrar(nuevoAlumno);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al registrar el alumno: " + ex.getMessage());
        }
    }
    
    /**
     * Metodo que envuelve al metodo de edicion de alumnos de persistencia
     * en un DTO para su uso en capas superiores.
     * 
     * @param id id del alumno.
     * @param nombres nombre o nombres del alumno.
     * @param apellidoPaterno apellido paterno del alumno.
     * @param apellidoMaterno apellido materno del alumno.
     * @throws NegocioException Excepcion en caso de error el metodo.
     */
    @Override
    public void editar(int id, String nombres, String apellidoPaterno, String apellidoMaterno) throws NegocioException {
        if (id <= 0 || nombres == null || nombres.trim().isEmpty() || apellidoPaterno == null 
                || apellidoPaterno.trim().isEmpty()) {
            throw new NegocioException("ingrese la informacion completa");
        }
        
        AlumnoEntidad alumno = new AlumnoEntidad(id, nombres, apellidoPaterno, apellidoMaterno, true, true);
        alumno.setIdAlumno(id);
        alumno.setNombres(nombres.trim());
        alumno.setApellidoPaterno(apellidoPaterno.trim());
        alumno.setApellidoMaterno(apellidoMaterno != null ? apellidoMaterno.trim() : null);

        try {
            alumnoDAO.editar(alumno);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al editar el alumno: " + ex.getMessage());
        }
    }
    
    /**
     * Metodo que envuelve al metodo de elimnar alumnos de persistencia
     * en un DTO para su uso en capas superiores.
     * 
     * @param id id del alumno a eliminar.
     * @throws NegocioException Excepcion en caso de error el metodo.
     */
    @Override
    public void eliminar(int id) throws NegocioException {
        if (id <= 0) {
            throw new NegocioException("ID no válido.");
        }

        try {
            alumnoDAO.eliminar(id);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al eliminar el alumno");
        }
    }
    
    /**
     * Metodo que convierte una entidad de la capa de persistencia a un DTO
     * para usos en capas posteriores.
     * 
     * @param alumnos Lista de alumnos que se desea convertir.
     * @return Lista de alumnos convertidos a dto.
     * @throws NegocioException Excepcion en caso de error el metodo.
     */
    private List<AlumnoTablaDTO> convertirAlumnoTablaDTO(List<AlumnoEntidad> alumnos) throws NegocioException {
        if (alumnos == null) {
            throw new NegocioException("No hay alumnos registrados");
        }

        List<AlumnoTablaDTO> alumnosDTO = new ArrayList<>();
        for (AlumnoEntidad alumno : alumnos) {
            AlumnoTablaDTO dto = new AlumnoTablaDTO();
            dto.setIdAlumno(alumno.getIdAlumno());
            dto.setNombres(alumno.getNombres());
            dto.setApellidoPaterno(alumno.getApellidoPaterno());
            dto.setApellidoMaterno(alumno.getApellidoMaterno());
            dto.setEstatus(alumno.isActivo() == true ? "Activo" : "Inactivo");
            alumnosDTO.add(dto);
        }
        return alumnosDTO;
    }
}
