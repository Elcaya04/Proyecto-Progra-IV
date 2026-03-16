package PrograIV.bolsaEmpleo.Logic;
import PrograIV.bolsaEmpleo.data.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaracteristicaService {

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    // Raíces del árbol (para mostrar el árbol al admin)
    public List<Caracteristica> listarRaices() {
        return caracteristicaRepository.findByPadreIsNull();
    }

    // Hijos de un nodo para navegar el árbol
    public List<Caracteristica> listarHijos(Integer padreId) {
        return caracteristicaRepository.findByPadreId(padreId);
    }

    public List<Caracteristica> listarTodas() {
        return caracteristicaRepository.findAll();
    }

    public void crear(Caracteristica caracteristica) {
        caracteristicaRepository.save(caracteristica);
    }

    public Caracteristica buscarPorId(Integer id) {
        return caracteristicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Característica no encontrada"));
    }
}
