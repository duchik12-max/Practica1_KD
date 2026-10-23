package vista;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
 
import modelo.itemPedido;
import modelo.pedido;
import modelo.producto;

public class consolaView {
	private Scanner scanner = new Scanner(System.in);
	 
    private String dinero(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }
 
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
 
    // ---------- Menús ----------
 
    public void mostrarMenuPrincipal() {
        System.out.println("\n** Cafetería El Buen Sabor **");
        System.out.println("1. Modificar stock");
        System.out.println("2. Registrar pedido");
        System.out.println("3. Resumen");
        System.out.println("4. Limpiar datos");
        System.out.println("5. Salir");
    }
 
    public void mostrarMenuStock() {
        System.out.println("\n--- Modificar Stock ---");
        System.out.println("1. Registrar producto");
        System.out.println("2. Modificar stock");
        System.out.println("3. Volver");
    }
 
    public void mostrarMenuAgregarOtro() {
        System.out.println("\n1. Agregar otro producto");
        System.out.println("2. Finalizar pedido");
    }
 
    // ---------- Lectura validada ----------
 
    public int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Error: ingrese un número entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número válido.");
            }
        }
    }
 
    // Precio en dólares: mayor que 0 y hasta "max" (2 decimales).
    public double leerPrecio(String mensaje, double max) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim().replace(',', '.');
            try {
                double valor = Math.round(Double.parseDouble(texto) * 100.0) / 100.0;
                if (valor > 0 && valor <= max) {
                    return valor;
                }
                System.out.println("Error: el precio debe ser mayor que $0.00 y no superar " + dinero(max) + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un precio válido (ejemplo: 1.50).");
            }
        }
    }
 
    // El nombre no puede estar vacío ni contener , ; | (separadores de los archivos).
    public String leerNombre(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Error: el nombre no puede estar vacío.");
            } else if (texto.contains(",") || texto.contains(";") || texto.contains("|")) {
                System.out.println("Error: el nombre no puede contener , ; ni |");
            } else {
                return texto;
            }
        }
    }
 
    public boolean confirmar(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim().toLowerCase();
            if (texto.equals("s")) {
                return true;
            }
            if (texto.equals("n")) {
                return false;
            }
            System.out.println("Error: responda con s o n.");
        }
    }
 
    // Catalogo y resumen
 
    public void mostrarCatalogo(ArrayList<producto> disponibles) {
        System.out.println("\n* Catálogo (nombre - $) *");
        for (int i = 0; i < disponibles.size(); i++) {
            producto p = disponibles.get(i);
            System.out.println((i + 1) + ". " + p.getNombre() + " - " + dinero(p.getPrecio()));
        }
        System.out.println("0. Cancelar pedido");
    }
 
    public void mostrarResumenPedido(pedido p) {
        System.out.println("\n** Resumen del Pedido **");
        for (itemPedido item : p.getItems()) {
            System.out.println(item.getNombre() + " | Cantidad: " + item.getCantidad()
                    + " | Subtotal: " + dinero(item.getSubtotal()));
        }
        System.out.println("Total: " + dinero(p.getTotal()));
    }
 
    public void mostrarResumenVentas(ArrayList<pedido> pedidos, double totalVentas,
                                     ArrayList<String> masVendidos, int unidades) {
        System.out.println("\n** Resumen de ventas **");
        for (int i = 0; i < pedidos.size(); i++) {
            System.out.println("Pedido " + (i + 1) + " - Total: " + dinero(pedidos.get(i).getTotal()));
        }
        System.out.println("\nTotal de ventas: " + dinero(totalVentas));
        System.out.println("Producto(s) más vendido(s) (" + unidades + " unidades):");
        for (String nombre : masVendidos) {
            System.out.println(" - " + nombre);
        }
    }
}
