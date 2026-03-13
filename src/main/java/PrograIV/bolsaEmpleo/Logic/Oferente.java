package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oferente")
// ¡CLAVE! Enlazamos el ID de esta tabla con el "email" de la tabla padre Usuario
@PrimaryKeyJoinColumn(name = "usuario_email")
@Getter @Setter @NoArgsConstructor
public class Oferente extends Usuario { // La palabra extends aplica la herencia

    //NO hay @Id aquí. Lo hereda de Usuario.

    @Column(nullable = false, unique = true, length = 50)
    private String identificacion;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 50)
    private String nacionalidad;

    @Column(columnDefinition = "TEXT")
    private String residencia;

    @Column(name = "pdf_curriculo", length = 255)
    private String pdfCurriculo; // Solo guardamos el nombre del archivo PDF
}