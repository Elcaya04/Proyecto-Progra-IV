package PrograIV.Logic;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Oferente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identificacion;
    private String nombre;
    private String primerApellido;
    private String nacionalidad;
    private String telefono;

    @Column(unique = true, nullable = false)
    private String correo;

    private String lugarResidencia;
    private String clave;

    // Ruta del archivo PDF del currículo guardado en el servidor
    private String rutaCurriculo;

    // "PENDIENTE" o "APROBADO"
    private String estado = "PENDIENTE";
}
