package controlador;

import java.util.ArrayList;

import modelo.producto;
import modelo.productoRepositorio;
import vista.consolaView;

public class stockController {
	private static int STOCK_MAX = 200;
    private static double PRECIO_MAX = 20.0;
 
    private consolaView vista;
    private productoRepositorio repo;
 
    public stockController(consolaView vista, productoRepositorio repo) {
        this.vista = vista;
        this.repo = repo;
    }
 
    public void mostrarMenu() {
        boolean volver = false;
        while (!volver) {
            vista.mostrarMenuStock();
            int opcion = vista.leerEntero("Seleccione una opción: ", 1, 3);
            switch (opcion) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    modificarStock();
                    break;
                case 3:
                    volver = true;
                    break;
            }
        }
    }
 
    private void registrarProducto() {
        vista.mostrarMensaje("\n-- Registrar Producto --");
        int codigo = vista.leerEntero("Código (solo numeros): ", 1, Integer.MAX_VALUE);
        if (repo.existeCodigo(codigo)) {
            vista.mostrarMensaje("Ya existe un producto con ese código.");
            return;
        }
        String nombre = vista.leerNombre("Nombre: ");
        int stock = vista.leerEntero("Stock inicial (1-" + STOCK_MAX + "): ", 1, STOCK_MAX);
        double precio = vista.leerPrecio("Precio ($): ", PRECIO_MAX);
 
        if (repo.agregar(new producto(nombre, codigo, stock, precio))) {
            vista.mostrarMensaje("Producto registrado correctamente.");
        } else {
            vista.mostrarMensaje("Error: no se pudo guardar el producto.");
        }
    }
 
    private void modificarStock() {
        vista.mostrarMensaje("\n-- Modificar Stock --");
        int codigo = vista.leerEntero("Código del producto: ", 1, Integer.MAX_VALUE);
        ArrayList<producto> productos = repo.leerTodos();
        producto p = repo.buscarEnLista(productos, codigo);
        if (p == null) {
            vista.mostrarMensaje("No existe un producto con ese código.");
            return;
        }
        vista.mostrarMensaje("Producto: " + p.getNombre() + " (stock actual: " + p.getStock() + ")");
        int nuevoStock = vista.leerEntero("Nuevo stock (0-" + STOCK_MAX + "): ", 0, STOCK_MAX);
        p.setStock(nuevoStock);
 
        if (repo.guardarTodos(productos)) {
            vista.mostrarMensaje("Stock actualizado correctamente.");
        } else {
            vista.mostrarMensaje("Error: no se pudo guardar el cambio.");
        }
    }
}
