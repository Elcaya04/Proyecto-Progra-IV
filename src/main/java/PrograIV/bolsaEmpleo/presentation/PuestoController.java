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


    @GetMapping("/buscar-por-caracteristicas")
    public String buscarPuestos(@RequestParam(required = false) Long categoriaId, Model model, Principal principal) {


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


        List<Puesto> puestosEncontrados;

        if (categoriaId != null) {

            List<PuestoCaracteristica> pcList = puestoCaracteristicaRepository.findByCaracteristicaId(categoriaId);
            puestosEncontrados = pcList.stream()
                    .map(PuestoCaracteristica::getPuesto)
                    .filter(Puesto::getActivo)
                    .distinct()
                    .collect(Collectors.toList());
        } else {

            puestosEncontrados = puestoRepository.findAll().stream()
                    .filter(Puesto::getActivo)
                    .collect(Collectors.toList());
        }


        if (principal == null) {
            puestosEncontrados = puestosEncontrados.stream()
                    .filter(p -> "PUBLICO".equalsIgnoreCase(p.getTipo()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("puestos", puestosEncontrados);

        return "puestos/buscar";
    }
}