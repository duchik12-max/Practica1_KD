package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class pedidoRepositorio {
	private static String ARCHIVO = "pedidos.txt";
	 
    // El número de pedido es su posición en la lista (línea 1 = pedido 1).
    public ArrayList<pedido> leerTodos() {
        ArrayList<pedido> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return lista;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lista.add(pedido.desdeLinea(linea));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
        }
        return lista;
    }
 
    public boolean guardar(pedido p) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            escritor.write(p.aLinea());
            escritor.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
 
    public boolean limpiar() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            return true; // abrir sin "append" deja el archivo vacío
        } catch (IOException e) {
            return false;
        }
    }

}
