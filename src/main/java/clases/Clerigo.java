package clases;
//Clase Clerigo, se combina con la interfaz cargarClerigos para obtener los párrocos, los cuales son necesarios
// para el registro en parroquia.
public class Clerigo {
    private int id_clerigo;
    private String nombres;
    private String apellidos;

    public Clerigo(int id_clerigo, String nombres, String apellidos) {
        this.id_clerigo = id_clerigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public int getId_clerigo() {
        return id_clerigo;
    }

    @Override
    public String toString() {
        return this.nombres + " " +this.apellidos;
    }
}

