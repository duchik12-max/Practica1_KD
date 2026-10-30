package modelo;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
 
import javax.swing.table.AbstractTableModel;
 
public class pedido {
 
    // ---------- Datos compartidos y persistencia (pedidos.txt) ----------
 
    private static String archivo = "pedidos.txt";
    private static ArrayList<pedido> historial = null;
 
    // ---------- Contenido del pedido ----------
 
    private ArrayList<itemPedido> items = new ArrayList<>();
 
    public ArrayList<itemPedido> getItems() {
        return items;
    }
 
    public boolean estaVacio() {
        return items.isEmpty();
    }
 
    // Si el producto ya está en el pedido, se suma la cantidad al mismo ítem.
    public void agregarProducto(producto p, int cantidad) {
        itemPedido existente = buscarItem(p.getCodigo());
        if (existente != null) {
            existente.sumarCantidad(cantidad);
        } else {
            items.add(new itemPedido(p.getCodigo(), p.getNombre(), cantidad, p.getPrecio()));
        }
    }
 
    public int getCantidadDe(int codigo) {
        itemPedido item = buscarItem(codigo);
        return item == null ? 0 : item.getCantidad();
    }
 
    public double getTotal() {
        double total = 0;
        for (itemPedido item : items) {
            total += item.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }
 
    private itemPedido buscarItem(int codigo) {
        for (itemPedido item : items) {
            if (item.getCodigo() == codigo) {
                return item;
            }
        }
        return null;
    }
 
    // Dinero en dólares con punto decimal, por ejemplo $3.50
    public static String formatoDinero(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }
 
    // Una línea por pedido: item;item;item|total
    public String aLinea() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(items.get(i).aTexto());
        }
        sb.append("|").append(String.format(Locale.US, "%.2f", getTotal()));
        return sb.toString();
    }
 
    public static pedido desdeLinea(String linea) {
        pedido p = new pedido();
        String[] partes = linea.split("\\|");
        for (String texto : partes[0].split(";")) {
            p.items.add(itemPedido.desdeTexto(texto));
        }
        return p;
    }
 
    // ---------- Operaciones sobre los pedidos guardados (antes pedidoRepositorio) ----------
 
    // Carga pedidos.txt la primera vez. El número de pedido es la posición en la lista.
    public static ArrayList<pedido> getHistorial() {
        if (historial == null) {
            historial = new ArrayList<>();
            File f = new File(archivo);
            if (f.exists()) {
                try (BufferedReader lector = new BufferedReader(new FileReader(f))) {
                    String linea;
                    while ((linea = lector.readLine()) != null) {
                        if (!linea.trim().isEmpty()) {
                            historial.add(desdeLinea(linea));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer " + archivo + ": " + e.getMessage());
                }
            }
        }
        return historial;
    }
 
    // Agrega este pedido al final del archivo y a la lista en memoria.
    public boolean guardar() {
        getHistorial();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true))) {
            escritor.write(aLinea());
            escritor.newLine();
        } catch (IOException e) {
            return false;
        }
        historial.add(this);
        return true;
    }
 
    // Vacía el archivo (abrirlo sin "append" lo deja en blanco) y la lista.
    public static boolean limpiar() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            getHistorial().clear();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
 
    // ---------- Modelo de la tabla del pedido en curso (tblPedido) ----------
 
    @SuppressWarnings("serial")
    public static class itemsTableModel extends AbstractTableModel {
        private ArrayList<String> columnas = coleccionesUtil.crearLista("Producto", "Cantidad", "Subtotal");
        private pedido origen;
 
        public itemsTableModel(pedido origen) {
            this.origen = origen;
        }
 
        public void refrescar() {
            fireTableDataChanged();
        }
 
        @Override
        public int getRowCount() {
            return origen.getItems().size();
        }
 
        @Override
        public int getColumnCount() {
            return columnas.size();
        }
 
        @Override
        public String getColumnName(int columna) {
            return columnas.get(columna);
        }
 
        @Override
        public Class<?> getColumnClass(int columna) {
            switch (columna) {
                case 1:
                    return Integer.class;
                case 2:
                    return Double.class;
                default:
                    return String.class;
            }
        }
 
        @Override
        public Object getValueAt(int fila, int columna) {
            itemPedido item = origen.getItems().get(fila);
            switch (columna) {
                case 0:
                    return item.getNombre();
                case 1:
                    return item.getCantidad();
                default:
                    return item.getSubtotal();
            }
        }
    }
 
    // ---------- Modelo de la tabla del reporte (tblPedidos) ----------
 
    @SuppressWarnings("serial")
    public static class resumenTableModel extends AbstractTableModel {
        private ArrayList<String> columnas = coleccionesUtil.crearLista("Pedido", "Total");
 
        public void refrescar() {
            fireTableDataChanged();
        }
 
        @Override
        public int getRowCount() {
            return getHistorial().size();
        }
 
        @Override
        public int getColumnCount() {
            return columnas.size();
        }
 
        @Override
        public String getColumnName(int columna) {
            return columnas.get(columna);
        }
 
        @Override
        public Class<?> getColumnClass(int columna) {
            return columna == 0 ? Integer.class : Double.class;
        }
 
        @Override
        public Object getValueAt(int fila, int columna) {
            if (columna == 0) {
                return fila + 1;
            }
            return getHistorial().get(fila).getTotal();
        }
    }
}
