package PrograIV.bolsaEmpleo.Logic;
import PrograIV.bolsaEmpleo.data.CaracteristicaRepository;
import PrograIV.bolsaEmpleo.data.PuestoCaracteristicaRepository;
import PrograIV.bolsaEmpleo.data.PuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PuestoService {

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;
    public List<Puesto> listarUltimosPublicos() {
        List<Puesto> todos = puestoRepository.findByActivoTrueAndTipo("PUBLICO");
        int total = todos.size();
        return todos.subList(Math.max(total - 5, 0), total);
    }
    public List<Puesto> listarPublicos() {
        return puestoRepository.findByActivoTrueAndTipo("PUBLICO");
    }
    public List<Puesto> listarPorEmpresa(String emailEmpresa) {
        return puestoRepository.findByEmpresaEmail(emailEmpresa);
    }

    public void publicar(Puesto puesto) {
        puesto.setActivo(true);
        puestoRepository.save(puesto);
    }

    public void desactivar(Long id) {
        Puesto puesto = puestoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));
        puesto.setActivo(false);
        puestoRepository.save(puesto);
    }

    public Puesto buscarPorId(Long id) {
        return puestoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));
    }

    public List<PuestoCaracteristica> listarRequisitos(Long puestoId) {
        return puestoCaracteristicaRepository.findByPuestoId(puestoId);
    }

    public List<Puesto> listarTodos() {
        return puestoRepository.findAll();
    }

    public List<Puesto> listarTodosActivos() {
        return puestoRepository.findAll().stream()
                .filter(Puesto::getActivo)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Puesto> listarActivosPorCaracteristica(Long caracId) {
        return puestoCaracteristicaRepository.findByCaracteristicaId(caracId)
                .stream()
                .map(PuestoCaracteristica::getPuesto)
                .filter(Puesto::getActivo)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }
    public List<Puesto> buscarPorCaracteristicas(List<Long> caracteristicaIds) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
            return listarPublicos();
        }
        return puestoRepository.findByCaracteristicas(caracteristicaIds);
    }
    public void guardarConCaracteristicas(Puesto puesto,
                                          List<Long> caracteristicasIds,
                                          Map<Long, Integer> niveles) {
        Puesto puestoGuardado = puestoRepository.save(puesto);

        if (caracteristicasIds != null) {
            for (Long caracId : caracteristicasIds) {
                Caracteristica carac = caracteristicaRepository.findById(caracId).orElse(null);
                if (carac != null) {
                    PuestoCaracteristica pc = new PuestoCaracteristica();
                    pc.setPuesto(puestoGuardado);
                    pc.setCaracteristica(carac);

                    int nivelRequerido = niveles.getOrDefault(caracId, 1);
                    pc.setNivelRequerido(nivelRequerido);

                    puestoCaracteristicaRepository.save(pc);
                }
            }
        }
    }
}