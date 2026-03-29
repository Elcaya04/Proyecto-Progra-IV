package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List; // 👈 IMPORTANTE: Importar List

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
    private String tipo;

    private Boolean activo = true;
    private LocalDate fechaCreacion = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
    @OneToMany(mappedBy = "puesto")
    private List<PuestoCaracteristica> puestoCaracteristicas;
}