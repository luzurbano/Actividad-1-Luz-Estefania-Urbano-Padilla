package com.mycompany.agenda;

import dao.PersonaDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Persona;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Agenda extends Application {

    private TextArea area;
    private PersonaDAO dao = new PersonaDAO();

    @Override
    public void start(Stage stage) {
        area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(480);

        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");
        Button btnModificar = new Button("Modificar");

        btnAgregar.setOnAction(e -> agregar());
        btnEliminar.setOnAction(e -> eliminar());
        btnModificar.setOnAction(e -> modificar());

        VBox root = new VBox(12, area, btnAgregar, btnEliminar, btnModificar);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 760, 560);
        stage.setScene(scene);
        stage.setTitle("Agenda");
        stage.show();

        cargar();
    }

    private void agregar() {
        String nombre = prompt("Nombre:");
        if (nombre == null) {
            return;
        }
        String direccion = prompt("Dirección:");
        if (direccion == null) {
            direccion = "";
        }
        String tels = prompt("Teléfonos (separados por coma):");
        List<String> telefonos = parseTelefonos(tels);
        dao.agregar(nombre, direccion, telefonos);
        cargar();
    }

    private void eliminar() {
        String idStr = prompt("ID a eliminar:");
        if (idStr == null) {
            return;
        }
        try {
            int id = Integer.parseInt(idStr.trim());
            boolean ok = dao.eliminar(id);
            if (!ok) {
                showAlert("No existe una persona con ese ID.");
            }
            cargar();
        } catch (NumberFormatException ex) {
            showAlert("ID inválido.");
        }
    }

    private void modificar() {
        String idStr = prompt("ID a modificar:");
        if (idStr == null) {
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException ex) {
            showAlert("ID inválido.");
            return;
        }
        String nombre = prompt("Nuevo nombre:");
        if (nombre == null) {
            return;
        }
        String direccion = prompt("Nueva dirección:");
        if (direccion == null) {
            direccion = "";
        }
        String tels = prompt("Nuevos teléfonos (separados por coma):");
        List<String> telefonos = parseTelefonos(tels);

        boolean ok = dao.modificar(id, nombre, direccion, telefonos);
        if (!ok) {
            showAlert("No existe una persona con ese ID.");
        }
        cargar();
    }

    private void cargar() {
        area.clear();
        for (Persona p : dao.listar()) {
            area.appendText(p.toString() + "\n\n");
        }
    }

    private String prompt(String header) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agenda");
        dialog.setHeaderText(header);
        dialog.setContentText(null);
        return dialog.showAndWait().orElse(null);
    }

    private List<String> parseTelefonos(String tels) {
        if (tels == null || tels.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(tels.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public String simularPrompt(String input) {
        if (input == null) {
            return null;
        }
        return input.trim();
    }

}
