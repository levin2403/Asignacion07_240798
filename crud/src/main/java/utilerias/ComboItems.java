/**
 * 
 */
package utilerias;

/**
 *
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public class ComboItems {
    
    private String nombre;
    private String id;
    
    public ComboItems(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
     public ComboItems(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String aNombre) {
        nombre = aNombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String aId) {
        id = aId;
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }
    
    @Override
    public boolean equals(Object item) {
        return  this.id.equals(((ComboItems) item).id);
    }
}