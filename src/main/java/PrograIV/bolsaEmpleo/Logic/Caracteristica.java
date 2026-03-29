package PrograIV.bolsaEmpleo.Logic;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "padre_id")
    private Caracteristica padre;
    @OneToMany(mappedBy = "padre", fetch = FetchType.EAGER)
    private List<Caracteristica> hijos = new ArrayList<>();
}
