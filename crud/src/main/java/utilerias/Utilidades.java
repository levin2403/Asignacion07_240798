/**
 *
 */
package utilerias;

/**
 *
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public class Utilidades {

    public int RegresarOFFSETMySQL(int limite, int pagina) {
        if (pagina <= 1) {
            return 0;
        }

        if (pagina == 2) {
            return limite;
        }

        return ((int) (limite * (pagina - 1)));
    }

}
