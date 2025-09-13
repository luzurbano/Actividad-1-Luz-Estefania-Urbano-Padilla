package appTest;

import dao.PersonaDAO;
import modelo.Persona;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AgendaAppTest {

    @Test
    public void testAgregarYModificarPersona() {
        PersonaDAO dao = new PersonaDAO();

        dao.agregar("Carlos", "Dir1", List.of("111"));

        List<Persona> todos = dao.listar();
        assertFalse(todos.isEmpty(), "No se insertó la persona. Revisa la conexión a BD.");

        Persona guardado = todos.stream()
                .filter(per -> "Carlos".equals(per.getNombre()))
                .findFirst()
                .orElse(null);

        assertNotNull(guardado, "No se encontró la persona 'Carlos' después de insertar.");

        dao.modificar(guardado.getId(), "Carlos Modificado", "Dir3", List.of("333"));

        Persona modificado = dao.listar().stream()
                .filter(per -> per.getId() == guardado.getId())
                .findFirst()
                .orElse(null);

        assertNotNull(modificado, "No se encontró la persona modificada.");
        assertEquals("Carlos Modificado", modificado.getNombre());
        assertEquals("Dir3", modificado.getDireccion());
        assertEquals(List.of("333"), modificado.getTelefonos());

        dao.eliminar(modificado.getId());
    }
}
