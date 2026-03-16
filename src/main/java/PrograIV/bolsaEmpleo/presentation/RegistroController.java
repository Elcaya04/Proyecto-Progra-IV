package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.Empresa;
import PrograIV.bolsaEmpleo.Logic.EmpresaService;
import PrograIV.bolsaEmpleo.Logic.Oferente;
import PrograIV.bolsaEmpleo.Logic.OferenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OferenteService oferenteService;

    @GetMapping("/registro")
    public String formRegistroGeneral() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistroGeneral(
            @RequestParam String tipoUsuario,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String clave,
            // 👇 NUEVOS CAMPOS EXCLUSIVOS DE OFERENTE 👇
            @RequestParam(required = false) String identificacion,
            @RequestParam(required = false) String nacionalidad,
            @RequestParam(required = false) String telefonoOferente,
            @RequestParam(required = false) String lugarResidencia,
            // 👇 NUEVOS CAMPOS EXCLUSIVOS DE EMPRESA 👇
            @RequestParam(required = false) String telefonoEmpresa,
            @RequestParam(required = false) String localizacion,
            @RequestParam(required = false) String descripcion,
            Model model) {

        try {
            if ("EMPRESA".equals(tipoUsuario)) {
                Empresa empresa = new Empresa();
                empresa.setEmail(email);
                empresa.setClave(passwordEncoder.encode(clave));
                empresa.setTipoUsuario("EMPRESA");
                empresa.setNombre(nombre);
                empresa.setEstado(0); // 0 = Pendiente de aprobación

                // Guardamos los datos extra
                empresa.setTelefono(telefonoEmpresa);
                empresa.setLocalizacion(localizacion);
                empresa.setDescripcion(descripcion);

                empresaService.registrar(empresa);
            } else {
                Oferente oferente = new Oferente();
                oferente.setEmail(email);
                oferente.setClave(passwordEncoder.encode(clave));
                oferente.setTipoUsuario("OFERENTE");
                oferente.setNombre(nombre);
                oferente.setEstado(0); // 0 = Pendiente de aprobación

                // Guardamos los datos extra
                oferente.setIdentificacion(identificacion);
                oferente.setNacionalidad(nacionalidad);
                oferente.setTelefono(telefonoOferente);
                oferente.setLugarResidencia(lugarResidencia);

                oferenteService.registrar(oferente);
            }

            // Redirigimos al login con un parámetro de éxito para que el usuario sepa qué pasó
            return "redirect:/login?exito=true";

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: El correo ya está en uso o faltan datos.");
            return "registro";
        }
    }
}