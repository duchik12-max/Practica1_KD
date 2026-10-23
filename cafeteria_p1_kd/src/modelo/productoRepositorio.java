package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class productoRepositorio {
	private static String ARCHIVO = "stock.txt";
	 
    public ArrayList<producto> leerTodos() {
        ArrayList<producto> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return lista;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lista.add(producto.desdeLinea(linea));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
        }
        return lista;
    }
 
    public boolean guardarTodos(ArrayList<producto> lista) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (producto p : lista) {
                escritor.write(p.aLinea());
                escritor.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
 
    public boolean agregar(producto p) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            escritor.write(p.aLinea());
            escritor.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
 
    public producto buscarEnLista(ArrayList<producto> lista, int codigo) {
        for (producto p : lista) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }
 
    public boolean existeCodigo(int codigo) {
        return buscarEnLista(leerTodos(), codigo) != null;
    }
 
    public boolean limpiar() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            return true; // abrir sin "append" deja el archivo vacío
        } catch (IOException e) {
            return false;
        }
    }
}
