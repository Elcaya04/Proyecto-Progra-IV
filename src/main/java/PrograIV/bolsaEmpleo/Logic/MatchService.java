package PrograIV.bolsaEmpleo.Logic;

import PrograIV.bolsaEmpleo.data.OferenteCaracteristicaRepository;
import PrograIV.bolsaEmpleo.data.OferenteRepository;
import PrograIV.bolsaEmpleo.data.PuestoCaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;


    public List<CandidatoMatch> buscarCandidatos(Long puestoId) {


        List<PuestoCaracteristica> requisitosPuesto =
                puestoCaracteristicaRepository.findByPuestoId(puestoId);
        int totalRequisitos = requisitosPuesto.size();

        List<CandidatoMatch> resultado = new ArrayList<>();

        if (totalRequisitos == 0) {
            return resultado;
        }


        List<Oferente> todosOferentes = oferenteRepository.findByEstado(1);


        for (Oferente oferente : todosOferentes) {

            List<OferenteCaracteristica> habsOferente =
                    oferenteCaracteristicaRepository.findByOferenteEmail(oferente.getEmail());

            int coincidencias = 0;

            for (PuestoCaracteristica req : requisitosPuesto) {
                for (OferenteCaracteristica hab : habsOferente) {

                    boolean mismaCaracteristica = req.getCaracteristica().getId()
                            .equals(hab.getCaracteristica().getId());

                    boolean nivelSuficiente = hab.getNivel() >= req.getNivelRequerido();

                    if (mismaCaracteristica && nivelSuficiente) {
                        coincidencias++;
                        break;
                    }
                }
            }

            if (coincidencias > 0) {
                double porcentaje = ((double) coincidencias / totalRequisitos) * 100.0;
                resultado.add(new CandidatoMatch(oferente, coincidencias, totalRequisitos, porcentaje));
            }
        }


        resultado.sort((a, b) -> Double.compare(b.getPorcentajeCoincidencia(), a.getPorcentajeCoincidencia()));

        return resultado;
    }
}
