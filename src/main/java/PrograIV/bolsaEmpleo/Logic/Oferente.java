package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.*;

@Entity
// Le decimos a Hibernate que el ID de esta tabla es el "email" de la tabla Usuario
@PrimaryKeyJoinColumn(name = "usuario_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Oferente extends Usuario {

    // 1. ELIMINAMOS el @Id y el Long id. (Hereda el ID del padre)
    // 2. ELIMINAMOS correo, clave y estado. (Ya los tiene el padre y si los ponemos aquí, Hibernate creará columnas duplicadas)

    private String identificacion;
    private String nombre;
    private String primerApellido;
    private String nacionalidad;
    private String telefono;
    private String lugarResidencia;

    // Ruta del archivo PDF del currículo guardado en el servidor
    private String rutaCurriculo;
}