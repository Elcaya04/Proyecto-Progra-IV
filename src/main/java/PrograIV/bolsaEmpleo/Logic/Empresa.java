package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empresa")
@PrimaryKeyJoinColumn(name = "usuario_correo")
@Getter
@Setter
@NoArgsConstructor
public class Empresa extends Usuario {

    private String nombre;
    private String localizacion;
    private String telefono;

    @Column(length = 1000)
    private String descripcion;
}