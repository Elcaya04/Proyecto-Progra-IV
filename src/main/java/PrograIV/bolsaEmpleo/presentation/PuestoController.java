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
import java.util.ArrayList;
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
    public String buscarPuestos(
            @RequestParam(required = false) List<Long> ids,
            Model model,
            Principal principal) {


        List<Caracteristica> raices = caracteristicaRepository.findByPadreIsNull();
        model.addAttribute("raices", raices);
        model.addAttribute("idsSeleccionados", ids != null ? ids : new ArrayList<>());


        List<Puesto> puestosEncontrados;

        if (ids == null || ids.isEmpty()) {

            puestosEncontrados = puestoRepository.findAll().stream()
                    .filter(Puesto::getActivo)
                    .collect(Collectors.toList());
        } else {

            puestosEncontrados = new ArrayList<>();
            for (Long id : ids) {
                List<PuestoCaracteristica> pcList =
                        puestoCaracteristicaRepository.findByCaracteristicaId(id);
                for (PuestoCaracteristica pc : pcList) {
                    Puesto p = pc.getPuesto();
                    if (p.getActivo() && !puestosEncontrados.contains(p)) {
                        puestosEncontrados.add(p);
                    }
                }
            }
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
