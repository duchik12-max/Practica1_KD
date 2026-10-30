package controlador;

import modelo.pedido;
import modelo.producto;
import vista.menuView;

public class menuController {
	private menuView vista;
    private inventarioController inventarioCtrl;
    private pedidoController pedidoCtrl;
 
    public menuController() {
        vista = new menuView();
        inventarioCtrl = new inventarioController();
        pedidoCtrl = new pedidoController();
 
        vista.addInventarioListener(e -> inventarioCtrl.abrir(vista));
        vista.addPedidoListener(e -> abrirPedido());
        vista.addReporteListener(e -> abrirReporte());
        vista.addLimpiarListener(e -> limpiarDatos());
    }
 
    public void iniciar() {
        vista.setVisible(true);
    }
 
    private void abrirPedido() {
        if (!producto.hayDisponibles()) {
            vista.mostrarInfo("No existen productos en el inventario");
            return;
        }
        pedidoCtrl.abrirPedido(vista);
    }
 
    private void abrirReporte() {
        if (pedido.getHistorial().isEmpty()) {
            vista.mostrarInfo("No hay pedidos registrados");
            return;
        }
        pedidoCtrl.abrirReporte(vista);
    }
 
    private void limpiarDatos() {
        if (!vista.confirmar("Se borrarán TODOS los productos y pedidos. ¿Está seguro?")) {
            return;
        }
        boolean productosLimpios = producto.limpiar();
        boolean pedidosLimpios = pedido.limpiar();
        if (productosLimpios && pedidosLimpios) {
            vista.mostrarInfo("Datos eliminados correctamente.");
        } else {
            vista.mostrarError("No se pudieron borrar los datos.");
        }
    }
}
