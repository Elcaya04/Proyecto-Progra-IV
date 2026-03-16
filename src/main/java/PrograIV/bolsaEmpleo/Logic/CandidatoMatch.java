package PrograIV.bolsaEmpleo.Logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CandidatoMatch {
    private Oferente oferente;
    private int requisitosCumplidos;
    private int requisitosTotales;
    private double porcentajeCoincidencia;
}