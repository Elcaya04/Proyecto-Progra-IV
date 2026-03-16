package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.Usuario;
import PrograIV.bolsaEmpleo.Logic.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String formLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String clave,
            HttpSession session,
            Model model) {

        try {
            Usuario usuario = usuarioService.login(email, clave);
            session.setAttribute("usuarioEmail", usuario.getEmail());
            session.setAttribute("usuarioTipo", usuario.getTipoUsuario());

            switch (usuario.getTipoUsuario()) {
                case "EMPRESA":   return "redirect:/empresa/dashboard";
                case "OFERENTE":  return "redirect:/oferente/dashboard";
                case "ADMIN":     return "redirect:/admin/dashboard";
                default:          return "redirect:/";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
