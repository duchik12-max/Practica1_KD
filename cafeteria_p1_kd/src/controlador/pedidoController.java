package controlador;

import java.awt.Window;
import java.util.HashMap;
import java.util.LinkedHashMap;
 
import modelo.itemPedido;
import modelo.pedido;
import modelo.producto;
import vista.pedidoView;
import vista.reporteView;

public class pedidoController {
	private pedidoView vistaPedido;
    private pedido actual;
    private producto.catalogoTableModel catalogo;
    private pedido.itemsTableModel items;
 
    // ---------- Registrar pedido ----------
 
    public void abrirPedido(Window dueno) {
        actual = new pedido();
        catalogo = new producto.catalogoTableModel(actual);
        items = new pedido.itemsTableModel(actual);
 
        vistaPedido = new pedidoView(dueno);
        vistaPedido.setModeloCatalogo(catalogo);
        vistaPedido.setModeloPedido(items);
        vistaPedido.setTotal(0);
 
        vistaPedido.addCatalogoSeleccionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ajustarCantidadMaxima();
            }
        });
        vistaPedido.addAgregarListener(e -> agregar());
        vistaPedido.addFinalizarListener(e -> finalizar());
        vistaPedido.addCancelarListener(e -> cancelar());
        vistaPedido.alCerrar(() -> cancelar());
 
        vistaPedido.setVisible(true);
    }
 
    // El máximo de la cantidad es lo que queda disponible del producto elegido.
    private void ajustarCantidadMaxima() {
        int fila = vistaPedido.getFilaCatalogo();
        if (fila < 0) {
            vistaPedido.setCantidadMaxima(1);
        } else {
            vistaPedido.setCantidadMaxima(catalogo.getDisponible(fila));
        }
    }
 
    private void agregar() {
        int fila = vistaPedido.getFilaCatalogo();
        if (fila < 0) {
            vistaPedido.mostrarAdvertencia("Seleccione un producto del catálogo.");
            return;
        }
 
        producto elegido = catalogo.getProducto(fila);
        int cantidad = vistaPedido.getCantidad();
        if (cantidad < 1 || cantidad > catalogo.getDisponible(fila)) {
            vistaPedido.mostrarAdvertencia("La cantidad debe estar entre 1 y " + catalogo.getDisponible(fila) + ".");
            return;
        }
 
        actual.agregarProducto(elegido, cantidad);
        catalogo.refrescar();
        items.refrescar();
        vistaPedido.setTotal(actual.getTotal());
 
        if (catalogo.getRowCount() == 0) {
            vistaPedido.mostrarInfo("No hay más productos disponibles. Puede finalizar el pedido.");
        }
    }
 
    private void finalizar() {
        if (actual.estaVacio()) {
            vistaPedido.mostrarAdvertencia("El pedido está vacío. Agregue productos o cancele el pedido.");
            return;
        }
 
        String resumen = construirResumen();
 
        for (itemPedido item : actual.getItems()) {
            producto p = producto.buscar(item.getCodigo());
            p.setStock(p.getStock() - item.getCantidad());
        }
 
        if (producto.guardar() && actual.guardar()) {
            vistaPedido.mostrarInfo("Pedido registrado correctamente.\n\n" + resumen);
            vistaPedido.cerrar();
        } else {
            for (itemPedido item : actual.getItems()) {
                producto p = producto.buscar(item.getCodigo());
                p.setStock(p.getStock() + item.getCantidad());
            }
            producto.guardar();
            vistaPedido.mostrarError("No se pudo guardar el pedido.");
        }
    }
 
    private void cancelar() {
        if (actual.estaVacio()) {
            vistaPedido.cerrar();
        } else if (vistaPedido.confirmar("¿Descartar el pedido actual?")) {
            vistaPedido.mostrarInfo("Pedido cancelado.");
            vistaPedido.cerrar();
        }
    }
 
    // Resumen del pedido: nombre, cantidad, subtotal y total.
    private String construirResumen() {
        StringBuilder sb = new StringBuilder();
        for (itemPedido item : actual.getItems()) {
            sb.append(item.getNombre())
                    .append(" | Cantidad: ").append(item.getCantidad())
                    .append(" | Subtotal: ").append(pedido.formatoDinero(item.getSubtotal()))
                    .append("\n");
        }
        sb.append("\nTOTAL: ").append(pedido.formatoDinero(actual.getTotal()));
        return sb.toString();
    }
 
    // ---------- Reporte ----------
 
    public void abrirReporte(Window dueno) {
        double totalVentas = 0;
        HashMap<Integer, Integer> unidadesPorCodigo = new HashMap<>();
        LinkedHashMap<Integer, String> nombres = new LinkedHashMap<>();
 
        for (pedido p : pedido.getHistorial()) {
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
 
        // Todos los productos empatados con el máximo de unidades vendidas.
        StringBuilder masVendidos = new StringBuilder();
        for (int codigo : nombres.keySet()) {
            if (unidadesPorCodigo.get(codigo) == maximo) {
                masVendidos.append(nombres.get(codigo))
                        .append(" - ").append(maximo).append(" unidades\n");
            }
        }
 
        reporteView vistaReporte = new reporteView(dueno);
        vistaReporte.setModeloPedidos(new pedido.resumenTableModel());
        vistaReporte.setTotalVentas(totalVentas);
        vistaReporte.setMasVendidos(masVendidos.toString());
        vistaReporte.setVisible(true);
    }
}
