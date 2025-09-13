package daoTest;

import dao.PersonaDAO;
import modelo.Persona;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonaDAOTest {

    @Test
    public void testAgregarYListar() {
        PersonaDAO dao = new PersonaDAO();
        dao.agregar("Ana", "Dir1", List.of("111"));

        List<Persona> personas = dao.listar();
        assertFalse(personas.isEmpty());
        Persona p = personas.get(personas.size() - 1); // último insertado
        assertEquals("Ana", p.getNombre());
        assertEquals("Dir1", p.getDireccion());
        assertTrue(p.getTelefonos().contains("111"));
    }

    @Test
    public void testModificar() {
        PersonaDAO dao = new PersonaDAO();
        dao.agregar("Luis", "DirX", List.of("999"));

        Persona guardado = dao.listar().get(dao.listar().size() - 1);
        boolean ok = dao.modificar(guardado.getId(), "Luis Mod", "DirY", List.of("888"));

        assertTrue(ok, "No se pudo modificar (ID inexistente).");
        Persona actualizado = dao.listar().stream()
                .filter(p -> p.getId() == guardado.getId())
                .findFirst().orElse(null);

        assertNotNull(actualizado);
        assertEquals("Luis Mod", actualizado.getNombre());
        assertEquals("DirY", actualizado.getDireccion());
        assertTrue(actualizado.getTelefonos().contains("888"));
    }

    @Test
    public void testEliminar() {
        PersonaDAO dao = new PersonaDAO();
        dao.agregar("Pedro", "DirZ", List.of("123"));

        Persona guardado = dao.listar().get(dao.listar().size() - 1);
        boolean ok = dao.eliminar(guardado.getId());

        assertTrue(ok, "No se eliminó la persona.");
        assertTrue(dao.listar().stream().noneMatch(p -> p.getId() == guardado.getId()));
    }

    @Test
    public void testEliminarIdInexistente() {
        PersonaDAO dao = new PersonaDAO();
        boolean ok = dao.eliminar(-999);
        assertFalse(ok, "Eliminó un ID inexistente.");
    }

    @Test
    public void testModificarIdInexistente() {
        PersonaDAO dao = new PersonaDAO();
        boolean ok = dao.modificar(-999, "X", "Y", List.of("000"));
        assertFalse(ok, "Modificó un ID inexistente.");
    }
}
