package controlador;
import java.awt.Window;

import modelo.producto;
import vista.inventarioView;

public class inventarioController {

	private inventarioView vista;
    private producto.comboModel modelo;
 
    public void abrir(Window dueno) {
        vista = new inventarioView(dueno);
        modelo = new producto.comboModel();
        vista.setModeloProductos(modelo);
 
        vista.addRegistrarListener(e -> registrar());
        vista.addProductoListener(e -> mostrarProductoSeleccionado());
        vista.addActualizarListener(e -> actualizar());
 
        mostrarProductoSeleccionado();
        vista.setVisible(true);
    }
 
    private void registrar() {
        int codigo = vista.getCodigo();
        String nombre = vista.getNombre();
        int stock = vista.getStock();
        double precio = vista.getPrecio();
 
        if (producto.existeCodigo(codigo)) {
            vista.mostrarAdvertencia("Ya existe un producto con ese código.");
            return;
        }
        if (!producto.nombreEsValido(nombre)) {
            vista.mostrarAdvertencia("El nombre no puede estar vacío ni contener , ; |");
            return;
        }
 
        if (producto.agregar(new producto(nombre, codigo, stock, precio))) {
            modelo.refrescar();
            vista.limpiarRegistro();
            mostrarProductoSeleccionado();
            vista.mostrarInfo("Producto registrado correctamente.");
        } else {
            vista.mostrarError("No se pudo guardar el producto.");
        }
    }
 
    // Muestra el stock actual del producto elegido en el combo y lo propone como nuevo stock.
    private void mostrarProductoSeleccionado() {
        producto p = vista.getProductoSeleccionado();
        if (p == null) {
            vista.setStockActual("-");
            vista.setEdicionHabilitada(false);
        } else {
            vista.setStockActual(String.valueOf(p.getStock()));
            vista.setNuevoStock(p.getStock());
            vista.setEdicionHabilitada(true);
        }
    }
 
    private void actualizar() {
        producto p = vista.getProductoSeleccionado();
        if (p == null) {
            vista.mostrarAdvertencia("Seleccione un producto.");
            return;
        }
 
        int anterior = p.getStock();
        p.setStock(vista.getNuevoStock());
 
        if (producto.guardar()) {
            vista.setStockActual(String.valueOf(p.getStock()));
            vista.mostrarInfo("Stock actualizado correctamente.");
        } else {
            p.setStock(anterior);
            vista.mostrarError("No se pudo guardar el cambio.");
        }
    }
}
