package modelo;
import java.util.Locale;

public class producto {
	private String nombre;
    private int codigo;
    private int stock;
    private double precio;
	public producto(String nombre, int codigo, int stock, double precio) {
		super();
		this.nombre = nombre;
		this.codigo = codigo;
		this.stock = stock;
		this.precio = precio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	//metodos
	
	// Formato de stock.txt: nombre,codigo,stock,precio
    public String aLinea() {
        return nombre + "," + codigo + "," + stock + "," + String.format(Locale.US, "%.2f", precio);
    }
 
    public static producto desdeLinea(String linea) {
        String[] partes = linea.split(",");
        return new producto(partes[0], Integer.parseInt(partes[1]),
                Integer.parseInt(partes[2]), Double.parseDouble(partes[3]));
    }
    
}
