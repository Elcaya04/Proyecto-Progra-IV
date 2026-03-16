package PrograIV.bolsaEmpleo.Logic;
import PrograIV.bolsaEmpleo.data.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    // Listar todas las empresas pendientes de aprobación (estado = 0)
    public List<Empresa> listarPendientes() {
        return empresaRepository.findByEstado(0);
    }

    // Aprobar una empresa: cambia estado a 1
    public void aprobar(String email) {
        Empresa empresa = empresaRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        empresa.setEstado(1);
        empresaRepository.save(empresa);
    }

    // Registrar una nueva empresa (llega con estado 0 por defecto)
    public void registrar(Empresa empresa) {
        if (empresaRepository.existsById(empresa.getEmail())) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese correo");
        }
        empresa.setEstado(0);
        empresa.setTipoUsuario("EMPRESA");
        empresaRepository.save(empresa);
    }

    public Empresa buscarPorEmail(String email) {
        return empresaRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
    }
}
