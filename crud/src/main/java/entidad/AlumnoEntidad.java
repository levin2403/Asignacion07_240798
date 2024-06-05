/**
 * 
 */
package entidad;

/**
 * Clase POJO que modela la entidad de alumnos.
 * 
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public class AlumnoEntidad {
   
    //inicializamos las variables de clase.
    private int idAlumno;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private boolean eliminado;
    private boolean activo;
    
    //inicializamos el constructior completo.
    public AlumnoEntidad(
    int idAlumno,
    String nombres,
    String apellidoPaterno,
    String apellidoMaterno,
    boolean eliminado,
    boolean activo) {
        
    this.idAlumno = idAlumno;
    this.nombres = nombres;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
    this.eliminado = eliminado;
    this.activo = activo;
    }

    //Getter y Setters
    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
