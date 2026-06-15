package clases;

import java.time.LocalDate;
//Clase que sirve para almacenar los datos que se registraran de la parroquia.
public class Parroquia {
    public String nombre;
    public int vicaria;
    public String ciudad;
    public String direccion;
    public LocalDate fechaFundacion;
    public int clerigo;
    public String telefono;
    public String email;
    public String sitioWeb;

    public Parroquia(String nombre, int vicaria, String ciudad, String direccion, LocalDate fechaFundacion, int clerigo, String telefono, String email, String sitioWeb) {
        this.nombre = nombre;
        this.vicaria = vicaria;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.fechaFundacion = fechaFundacion;
        this.clerigo = clerigo;
        this.telefono = telefono;
        this.email = email;
        this.sitioWeb = sitioWeb;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVicaria() {
        return vicaria;
    }

    public void setVicaria(int vicaria) {
        this.vicaria = vicaria;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getClerigo() {
        return clerigo;
    }

    public void setClerigo(int clerigo) {
        this.clerigo = clerigo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }
}
