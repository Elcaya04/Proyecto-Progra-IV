package PrograIV.bolsaEmpleo.Logic;

import PrograIV.bolsaEmpleo.Logic.Empresa;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String descripcion;

    private Double salario;

    // "PUBLICO" o "PRIVADO"
    private String tipo;

    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
}