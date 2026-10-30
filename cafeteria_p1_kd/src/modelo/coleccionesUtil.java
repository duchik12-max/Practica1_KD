package modelo;
import java.util.ArrayList;
import java.util.Collection;

public class coleccionesUtil {

	@SafeVarargs
    public static <T> ArrayList<T> crearLista(T... elementos) {
        ArrayList<T> lista = new ArrayList<>();
        for (T elemento : elementos) {
            lista.add(elemento);
        }
        return lista;
    }
 
    // 
    @SafeVarargs
    public static <T> void agregarTodos(Collection<T> destino, T... elementos) {
        for (T elemento : elementos) {
            destino.add(elemento);
        }
    }
}
