/**
 *
 */
package negocio;

/**
 * Clase que modela la excepcion personalizada para los objetos de tipo Negocio.
 * 
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public class NegocioException extends Exception {

    public NegocioException() {
    }

    public NegocioException(String message) {
        super(message);
    }

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegocioException(Throwable cause) {
        super(cause);
    }

    public NegocioException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
