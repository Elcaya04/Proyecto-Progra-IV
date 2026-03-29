package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.*;

@Entity
@PrimaryKeyJoinColumn(name = "usuario_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Oferente extends Usuario {

    private String identificacion;
    private String nombre;
    private String primerApellido;
    private String nacionalidad;
    private String telefono;
    private String lugarResidencia;
    private String rutaCurriculo;
}