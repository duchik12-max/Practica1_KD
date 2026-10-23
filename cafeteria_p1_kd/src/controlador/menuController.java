package controlador;

import modelo.pedidoRepositorio;
import modelo.productoRepositorio;
import vista.consolaView;

public class menuController {
	private consolaView vista = new consolaView();
    private productoRepositorio productoRepo = new productoRepositorio();
    private pedidoRepositorio pedidoRepo = new pedidoRepositorio();
    private stockController stockCtrl;
    private pedidoController pedidoCtrl;
 
    public menuController() {
        stockCtrl = new stockController(vista, productoRepo);
        pedidoCtrl = new pedidoController(vista, productoRepo, pedidoRepo);
    }
 
    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            vista.mostrarMenuPrincipal();
            int opcion = vista.leerEntero("Seleccione una opción: ", 1, 5);
            switch (opcion) {
                case 1:
                    stockCtrl.mostrarMenu();
                    break;
                case 2:
                    pedidoCtrl.registrarPedido();
                    break;
                case 3:
                    pedidoCtrl.mostrarResumen();
                    break;
                case 4:
                    limpiarDatos();
                    break;
                case 5:
                    vista.mostrarMensaje("Fin del programa");
                    salir = true;
                    break;
            }
        }
    }
 
    private void limpiarDatos() {
        if (!vista.confirmar("Se borrarán Todos los productos y pedidos. ¿Está seguro? (s/n): ")) {
            vista.mostrarMensaje("Operación cancelada");
            return;
        }
        if (productoRepo.limpiar() && pedidoRepo.limpiar()) {
            vista.mostrarMensaje("Datos eliminados correctamente");
        } else {
            vista.mostrarMensaje("Error: no se pudieron borrar los datos.");
        }
    }
}
