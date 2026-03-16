package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.Caracteristica;
import PrograIV.bolsaEmpleo.Logic.Puesto;
import PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica;
import PrograIV.bolsaEmpleo.data.CaracteristicaRepository;
import PrograIV.bolsaEmpleo.data.PuestoCaracteristicaRepository;
import PrograIV.bolsaEmpleo.data.PuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/puestos")
public class PuestoController {

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    // ---- BUSCADOR DE PUESTOS (Público y Privado) ----
    @GetMapping("/buscar-por-caracteristicas")
    public String buscarPuestos(@RequestParam(required = false) Long categoriaId, Model model, Principal principal) {

        // 1. Navegación del árbol de categorías (Igual que en habilidades)
        List<Caracteristica> subcategorias;
        Caracteristica categoriaActual = null;

        if (categoriaId == null) {
            subcategorias = caracteristicaRepository.findByPadreIsNull();
        } else {
            categoriaActual = caracteristicaRepository.findById(categoriaId).orElse(null);
            subcategorias = caracteristicaRepository.findByPadreId(categoriaId);
        }

        model.addAttribute("subcategorias", subcategorias);
        model.addAttribute("categoriaActual", categoriaActual);

        // 2. Buscar los puestos según la categoría seleccionada
        List<Puesto> puestosEncontrados;

        if (categoriaId != null) {
            // Filtrar puestos que piden específicamente esta característica
            List<PuestoCaracteristica> pcList = puestoCaracteristicaRepository.findByCaracteristicaId(categoriaId);
            puestosEncontrados = pcList.stream()
                    .map(PuestoCaracteristica::getPuesto)
                    .filter(Puesto::getActivo) // Solo mostrar puestos activos
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            // Si no seleccionó nada, mostrar todos los activos
            puestosEncontrados = puestoRepository.findAll().stream()
                    .filter(Puesto::getActivo)
                    .collect(Collectors.toList());
        }

        // 3. REGLA DE NEGOCIO: Filtrar Privados vs Públicos
        // Si 'principal' es null, significa que es un visitante sin cuenta
        if (principal == null) {
            puestosEncontrados = puestosEncontrados.stream()
                    .filter(p -> "PUBLICO".equalsIgnoreCase(p.getTipo()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("puestos", puestosEncontrados);

        return "puestos/buscar";
    }
}