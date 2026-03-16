package PrograIV.bolsaEmpleo.Logic;
import PrograIV.bolsaEmpleo.data.OferenteCaracteristicaRepository;
import PrograIV.bolsaEmpleo.data.OferenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OferenteService {

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private OferenteCaracteristicaRepository habilidadesRepository;

    public void registrar(Oferente oferente) {
        if (oferenteRepository.existsById(oferente.getEmail())) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese correo");
        }
        oferente.setEstado(0);
        oferente.setTipoUsuario("OFERENTE");
        oferenteRepository.save(oferente);
    }

    public List<Oferente> listarPendientes() {
        return oferenteRepository.findByEstado(0);
    }

    public void aprobar(String email) {
        Oferente oferente = oferenteRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
        oferente.setEstado(1);
        oferenteRepository.save(oferente);
    }

    public Oferente buscarPorEmail(String email) {
        return oferenteRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
    }

    public List<OferenteCaracteristica> listarHabilidades(String email) {
        return habilidadesRepository.findByOferenteEmail(email);
    }

    public void agregarHabilidad(OferenteCaracteristica habilidad) {
        habilidadesRepository.save(habilidad);
    }
    public void guardarRutaCurriculo(String email, String nombreArchivo) {
        Oferente oferente = oferenteRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
        oferente.setRutaCurriculo(nombreArchivo);
        oferenteRepository.save(oferente);
    }

    public List<Oferente> buscarCandidatos(Long puestoId) {
        // Por ahora retorna todos los aprobados, la lógica de coincidencia va después
        return oferenteRepository.findByEstado(1);
    }
}
