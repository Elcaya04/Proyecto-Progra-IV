package PrograIV.Logic;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String localizacion;

    @Column(unique = true, nullable = false)
    private String correo;

    private String telefono;

    @Column(length = 1000)
    private String descripcion;

    private String clave;

    // "PENDIENTE" o "APROBADO"
    private String estado = "PENDIENTE";
}
