package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
// ¡AQUÍ ESTÁ LA MAGIA! Le decimos a JPA que use la estrategia de tablas unidas
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @Column(name = "email", length = 100)
    private String email; // Usamos el correo como ID para el login

    @Column(nullable = false, length = 100)
    private String clave;

    @Column(name = "tipo_usuario", nullable = false, length = 20)
    private String tipoUsuario; // 'ADMIN', 'EMPRESA', 'OFERENTE'

    @Column(columnDefinition = "int default 0")
    private int estado; // 0 = Pendiente, 1 = Aprobado

    // ==========================================================
    // CONSTRUCTOR VACÍO (Obligatorio para Hibernate)
    // ==========================================================
    public Usuario() {
    }

    // ==========================================================
    // GETTERS Y SETTERS MANUALES (Sin usar Lombok)
    // ==========================================================
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}