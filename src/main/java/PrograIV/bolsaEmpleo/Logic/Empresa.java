package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "usuario_correo")
public class Empresa extends Usuario {

    private String nombre;
    private String localizacion;
    private String telefono;

    @Column(length = 1000)
    private String descripcion;

    // ==========================================================
    // CONSTRUCTOR VACÍO (Obligatorio para Hibernate)
    // ==========================================================
    public Empresa() {
    }

    // ==========================================================
    // GETTERS Y SETTERS MANUALES (Sin Lombok)
    // ==========================================================
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}