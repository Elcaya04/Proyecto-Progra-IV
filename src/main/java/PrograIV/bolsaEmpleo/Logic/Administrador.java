package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "usuario_email")
@Getter
@Setter
@NoArgsConstructor
public class Administrador extends Usuario {

    @Column(length = 100)
    private String nombre;
}