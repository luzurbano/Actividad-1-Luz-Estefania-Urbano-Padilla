package modelo;

import java.util.List;

public class Persona {
    private int id;
    private String nombre;
    private String direccion;
    private List<String> telefonos;

    public Persona(int id, String nombre, String direccion, List<String> telefonos) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefonos = telefonos;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public List<String> getTelefonos() { return telefonos; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefonos(List<String> telefonos) { this.telefonos = telefonos; }

    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + ", Dirección: " + direccion +
                "\n   Teléfonos: " + telefonos;
    }
}
