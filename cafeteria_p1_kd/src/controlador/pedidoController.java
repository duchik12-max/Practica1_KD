package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
 
import modelo.itemPedido;
import modelo.pedido;
import modelo.pedidoRepositorio;
import modelo.producto;
import modelo.productoRepositorio;
import vista.consolaView;

public class pedidoController {
	private consolaView vista;
    private productoRepositorio productoRepo;
    private pedidoRepositorio pedidoRepo;
 
    public pedidoController(consolaView vista, productoRepositorio productoRepo, pedidoRepositorio pedidoRepo) {
        this.vista = vista;
        this.productoRepo = productoRepo;
        this.pedidoRepo = pedidoRepo;
    }
 
    // ---------- Registrar pedido ----------
 
    public void registrarPedido() {
        ArrayList<producto> productos = productoRepo.leerTodos();
        pedido nuevo = new pedido();
 
        if (obtenerDisponibles(productos, nuevo).isEmpty()) {
            vista.mostrarMensaje("No existen productos en el inventario");
            return;
        }
 
        boolean finalizar = false;
        boolean cancelado = false;
        while (!finalizar) {
            // Se recalcula cada vuelta: un producto sin stock restante deja de mostrarse.
            ArrayList<producto> disponibles = obtenerDisponibles(productos, nuevo);
            if (disponibles.isEmpty()) {
                vista.mostrarMensaje("No hay más productos disponibles.");
                break;
            }
 
            vista.mostrarCatalogo(disponibles);
            int opcion = vista.leerEntero("Seleccione un producto: ", 0, disponibles.size());
 
            if (opcion == 0) {
                if (nuevo.estaVacio() || vista.confirmar("¿Descartar el pedido actual? (s/n): ")) {
                    cancelado = true;
                    break;
                }
                continue;
            }
 
            producto elegido = disponibles.get(opcion - 1);
            int restante = elegido.getStock() - nuevo.getCantidadDe(elegido.getCodigo());
            int cantidad = vista.leerEntero("Cantidad (1-" + restante + "): ", 1, restante);
            nuevo.agregarProducto(elegido, cantidad);
 
            vista.mostrarMenuAgregarOtro();
            int siguiente = vista.leerEntero("Seleccione una opción: ", 1, 2);
            switch (siguiente) {
                case 1:
                    break;
                case 2:
                    finalizar = true;
                    break;
            }
        }
 
        if (cancelado || nuevo.estaVacio()) {
            vista.mostrarMensaje("Pedido cancelado.");
            return;
        }
        finalizarPedido(productos, nuevo);
    }
 
    private void finalizarPedido(ArrayList<producto> productos, pedido nuevo) {
        vista.mostrarResumenPedido(nuevo);
 
        for (itemPedido item : nuevo.getItems()) {
            producto p = productoRepo.buscarEnLista(productos, item.getCodigo());
            p.setStock(p.getStock() - item.getCantidad());
        }
 
        if (productoRepo.guardarTodos(productos) && pedidoRepo.guardar(nuevo)) {
            vista.mostrarMensaje("\nPedido registrado correctamente.");
        } else {
            vista.mostrarMensaje("\nError: no se pudo guardar el pedido.");
        }
    }
 
    // Productos con unidades todavía disponibles (descontando lo ya agregado al pedido).
    private ArrayList<producto> obtenerDisponibles(ArrayList<producto> productos, pedido actual) {
        ArrayList<producto> disponibles = new ArrayList<>();
        for (producto p : productos) {
            if (p.getStock() - actual.getCantidadDe(p.getCodigo()) > 0) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }
 
    // ---------- Resumen ----------
 
    public void mostrarResumen() {
        ArrayList<pedido> pedidos = pedidoRepo.leerTodos();
        if (pedidos.isEmpty()) {
            vista.mostrarMensaje("No hay pedidos registrados.");
            return;
        }
 
        double totalVentas = 0;
        HashMap<Integer, Integer> unidadesPorCodigo = new HashMap<>();
        LinkedHashMap<Integer, String> nombres = new LinkedHashMap<>();
 
        for (pedido p : pedidos) {
            totalVentas += p.getTotal();
            for (itemPedido item : p.getItems()) {
                unidadesPorCodigo.merge(item.getCodigo(), item.getCantidad(), Integer::sum);
                nombres.put(item.getCodigo(), item.getNombre());
            }
        }
        totalVentas = Math.round(totalVentas * 100.0) / 100.0;
 
        int maximo = 0;
        for (int unidades : unidadesPorCodigo.values()) {
            maximo = Math.max(maximo, unidades);
        }
 
        ArrayList<String> masVendidos = new ArrayList<>();
        for (int codigo : nombres.keySet()) {
            if (unidadesPorCodigo.get(codigo) == maximo) {
                masVendidos.add(nombres.get(codigo));
            }
        }
 
        vista.mostrarResumenVentas(pedidos, totalVentas, masVendidos, maximo);
    }
}
