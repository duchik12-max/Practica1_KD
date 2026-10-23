package modelo;
import java.util.ArrayList;
import java.util.Locale;

public class pedido {
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

}
