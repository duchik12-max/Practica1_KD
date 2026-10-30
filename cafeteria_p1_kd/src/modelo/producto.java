package modelo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
 
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.table.AbstractTableModel;


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
	
 // ---------- Datos compartidos y persistencia (stock.txt) ----------
    
    private static String archivo = "stock.txt";
    private static ArrayList<producto> lista = null;
    private static ArrayList<Character> prohibidos = new ArrayList<>();
 
    static {
        // Caracteres que se usan como separadores en los archivos de texto.
        coleccionesUtil.agregarTodos(prohibidos, ',', ';', '|');
    }
    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
 
    // Formato en stock.txt: nombre,codigo,stock,precio
    public String aLinea() {
        return nombre + "," + codigo + "," + stock + "," + String.format(Locale.US, "%.2f", precio);
    }
 
    public static producto desdeLinea(String linea) {
        String[] partes = linea.split(",");
        return new producto(partes[0], Integer.parseInt(partes[1]),
                Integer.parseInt(partes[2]), Double.parseDouble(partes[3]));
    }
 
    // ---------- Validación ----------
 
    // El nombre no puede estar vacío ni contener caracteres especiales
    public static boolean nombreEsValido(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        for (char c : nombre.toCharArray()) {
            if (prohibidos.contains(c)) {
                return false;
            }
        }
        return true;
    }
 
    // Operaciones sobre el inventario
 
    // Carga stock.txt la primera vez y después devuelve siempre la misma lista
    public static ArrayList<producto> getLista() {
        if (lista == null) {
            lista = new ArrayList<>();
            File f = new File(archivo);
            if (f.exists()) {
                try (BufferedReader lector = new BufferedReader(new FileReader(f))) {
                    String linea;
                    while ((linea = lector.readLine()) != null) {
                        if (!linea.trim().isEmpty()) {
                            lista.add(desdeLinea(linea));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer " + archivo + ": " + e.getMessage());
                }
            }
        }
        return lista;
    }
 
    public static producto buscar(int codigo) {
        for (producto p : getLista()) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }
 
    public static boolean existeCodigo(int codigo) {
        return buscar(codigo) != null;
    }
 
    // Hay al menos un producto con unidades en stock.
    public static boolean hayDisponibles() {
        for (producto p : getLista()) {
            if (p.getStock() > 0) {
                return true;
            }
        }
        return false;
    }
 
    // Agrega el producto al final del archivo y a la lista en memoria.
    public static boolean agregar(producto p) {
        getLista();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true))) {
            escritor.write(p.aLinea());
            escritor.newLine();
        } catch (IOException e) {
            return false;
        }
        lista.add(p);
        return true;
    }
 
    // Reescribe stock.txt completo con el contenido actual de la lista.
    public static boolean guardar() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            for (producto p : getLista()) {
                escritor.write(p.aLinea());
                escritor.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
 
    // Vacía el archivo y la lista.
    public static boolean limpiar() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            getLista().clear();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    //elementos para la GUI
    // ---------- Modelo del JComboBox de edición (cmbProductos) ----------
 
    @SuppressWarnings("serial")
    public static class comboModel extends AbstractListModel<producto> implements ComboBoxModel<producto> {
        private Object seleccionado;
 
        public comboModel() {
            if (!getLista().isEmpty()) {
                seleccionado = getLista().get(0);
            }
        }
 
        @Override
        public int getSize() {
            return getLista().size();
        }
 
        @Override
        public producto getElementAt(int indice) {
            return getLista().get(indice);
        }
 
        @Override
        public void setSelectedItem(Object item) {
            seleccionado = item;
            fireContentsChanged(this, -1, -1);
        }
 
        @Override
        public Object getSelectedItem() {
            return seleccionado;
        }
 
        // Se llama cuando cambia la lista 
        public void refrescar() {
            if (seleccionado == null && !getLista().isEmpty()) {
                seleccionado = getLista().get(0);
            }
            fireContentsChanged(this, 0, Math.max(getSize() - 1, 0));
        }
    }
 
    // Modelo de la tabla del catálogo (tblCatalogo) actualiza el stock 

    @SuppressWarnings("serial")
    public static class catalogoTableModel extends AbstractTableModel {
        private ArrayList<String> columnas = coleccionesUtil.crearLista("Nombre", "Precio", "Stock");
        private ArrayList<producto> visibles = new ArrayList<>();
        private pedido actual;
 
        public catalogoTableModel(pedido actual) {
            this.actual = actual;
            refrescar();
        }
 
        public void refrescar() {
            visibles.clear();
            for (producto p : getLista()) {
                if (p.getStock() - actual.getCantidadDe(p.getCodigo()) > 0) {
                    visibles.add(p);
                }
            }
            fireTableDataChanged();
        }
 
        public producto getProducto(int fila) {
            return visibles.get(fila);
        }
 
        public int getDisponible(int fila) {
            producto p = visibles.get(fila);
            return p.getStock() - actual.getCantidadDe(p.getCodigo());
        }
 
        @Override
        public int getRowCount() {
            return visibles.size();
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
                    return Double.class;
                case 2:
                    return Integer.class;
                default:
                    return String.class;
            }
        }
 
        @Override
        public Object getValueAt(int fila, int columna) {
            producto p = visibles.get(fila);
            switch (columna) {
                case 0:
                    return p.getNombre();
                case 1:
                    return p.getPrecio();
                default:
                    return getDisponible(fila);
            }
        }
    }
}
