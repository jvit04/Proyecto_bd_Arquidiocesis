package clases;
//Clase que sirve para mostrar en un comboBox las vicarias disponibles.
public class Vicaria {
    private int idVicaria;
    private String nombreVicaria;

    public Vicaria(int idVicaria, String nombreVicaria) {
        this.idVicaria = idVicaria;
        this.nombreVicaria = nombreVicaria;
    }

    public int getIdVicaria() {
        return idVicaria;
    }

    @Override
    public String toString() {
        return nombreVicaria;
    }
}
