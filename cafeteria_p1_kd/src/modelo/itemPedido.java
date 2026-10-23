package modelo;
import java.util.Locale;

public class itemPedido {
	private int codigo;
    private String nombre;
    private int cantidad;
    private double precioUnitario;
	public itemPedido(int codigo, String nombre, int cantidad, double precioUnitario) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
    //Metodos
	public void sumarCantidad(int extra) {
        this.cantidad += extra;
    }
 
    public double getSubtotal() {
        return Math.round(cantidad * precioUnitario * 100.0) / 100.0;
    }
 
    // Formato dentro de pedidos.txt: codigo,nombre,cantidad,precioUnitario,subtotal
    public String aTexto() {
        return codigo + "," + nombre + "," + cantidad + ","
                + String.format(Locale.US, "%.2f", precioUnitario) + ","
                + String.format(Locale.US, "%.2f", getSubtotal());
    }
 
    public static itemPedido desdeTexto(String texto) {
        String[] partes = texto.split(",");
        return new itemPedido(Integer.parseInt(partes[0]), partes[1],
                Integer.parseInt(partes[2]), Double.parseDouble(partes[3]));
    }
}
