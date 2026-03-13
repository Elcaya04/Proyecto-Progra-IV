package PrograIV.bolsaEmpleo.data;

import PrograIV.bolsaEmpleo.Logic.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    // Usamos String porque el ID (Llave primaria) de la Empresa es el correo
}