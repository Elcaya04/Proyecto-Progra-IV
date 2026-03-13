package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "usuario_email")
@Getter @Setter @NoArgsConstructor
public class Administrador extends Usuario {

    // Por ahora le pondremos solo un nombre para distinguirlo,
    // pero hereda su email y clave para hacer Login.
    @Column(length = 100)
    private String nombre;
}