package vista;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import modelo.producto;

@SuppressWarnings("serial")
public class inventarioView extends JDialog {

    private JPanel contentPane;
    private JTabbedPane tabInventario;

    // Pestaña Registro
    private JPanel pnlRegistro;
    private JLabel lblCodigo;
    private JSpinner spnCodigo;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JLabel lblStock;
    private JSpinner spnStock;
    private JLabel lblPrecio;
    private JSpinner spnPrecio;
    private JButton btnRegistrar;

    // Pestaña Edición
    private JPanel pnlEdicion;
    private JLabel lblProducto;
    private JComboBox<producto> cmbProductos;
    private JLabel lblStockActualTitulo;
    private JLabel lblStockActual;
    private JLabel lblNuevoStock;
    private JSpinner spnNuevoStock;
    private JButton btnActualizar;

    public inventarioView(Window owner) {
        super(owner, "Inventario", Dialog.ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 430, 330);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        tabInventario = new JTabbedPane(JTabbedPane.TOP);
        tabInventario.setBounds(10, 10, 395, 270);
        contentPane.add(tabInventario);

        // ---------- Pestaña Registro ----------
        pnlRegistro = new JPanel();
        pnlRegistro.setLayout(null);
        tabInventario.addTab("Registro", null, pnlRegistro, null);

        lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(20, 25, 135, 20);
        pnlRegistro.add(lblCodigo);

        // SpinnerNumberModel(valor inicial, mínimo, máximo, paso): aquí se limita el código (1 a 500).
        spnCodigo = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        spnCodigo.setBounds(160, 22, 100, 26);
        pnlRegistro.add(spnCodigo);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 65, 135, 20);
        pnlRegistro.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(160, 62, 200, 26);
        pnlRegistro.add(txtNombre);

        lblStock = new JLabel("Stock inicial:");
        lblStock.setBounds(20, 105, 135, 20);
        pnlRegistro.add(lblStock);

        // Stock al registrar: 1 a 200.
        spnStock = new JSpinner(new SpinnerNumberModel(1, 1, 200, 1));
        spnStock.setBounds(160, 102, 100, 26);
        pnlRegistro.add(spnStock);

        lblPrecio = new JLabel("Precio (dólares):");
        lblPrecio.setBounds(20, 145, 135, 20);
        pnlRegistro.add(lblPrecio);

        // Precio: mayor que 0 y hasta 20.00, de centavo en centavo.
        spnPrecio = new JSpinner(new SpinnerNumberModel(0.01, 0.01, 20.0, 0.01));
        JSpinner.NumberEditor editorPrecio = new JSpinner.NumberEditor(spnPrecio, "0.00");
        editorPrecio.getFormat().setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        spnPrecio.setEditor(editorPrecio);
        editorPrecio.getTextField().setValue(spnPrecio.getValue());
        spnPrecio.setBounds(160, 142, 100, 26);
        pnlRegistro.add(spnPrecio);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(160, 190, 110, 32);
        pnlRegistro.add(btnRegistrar);

        // ---------- Pestaña Edición ----------
        pnlEdicion = new JPanel();
        pnlEdicion.setLayout(null);
        tabInventario.addTab("Edición", null, pnlEdicion, null);

        lblProducto = new JLabel("Producto:");
        lblProducto.setBounds(20, 25, 135, 20);
        pnlEdicion.add(lblProducto);

        cmbProductos = new JComboBox<producto>();
        cmbProductos.setBounds(160, 22, 200, 26);
        pnlEdicion.add(cmbProductos);

        lblStockActualTitulo = new JLabel("Stock actual:");
        lblStockActualTitulo.setBounds(20, 65, 135, 20);
        pnlEdicion.add(lblStockActualTitulo);

        lblStockActual = new JLabel("-");
        lblStockActual.setBounds(160, 65, 100, 20);
        pnlEdicion.add(lblStockActual);

        lblNuevoStock = new JLabel("Nuevo stock:");
        lblNuevoStock.setBounds(20, 105, 135, 20);
        pnlEdicion.add(lblNuevoStock);

        // Stock al editar: 0 a 200.
        spnNuevoStock = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
        spnNuevoStock.setBounds(160, 102, 100, 26);
        pnlEdicion.add(spnNuevoStock);

        btnActualizar = new JButton("Actualizar stock");
        btnActualizar.setBounds(160, 150, 140, 32);
        pnlEdicion.add(btnActualizar);

        setLocationRelativeTo(owner);
    }

    // ---------- Métodos para el controlador (van después del diseño) ----------

    public void addRegistrarListener(ActionListener l) {
        btnRegistrar.addActionListener(l);
    }

    public void addProductoListener(ActionListener l) {
        cmbProductos.addActionListener(l);
    }

    public void addActualizarListener(ActionListener l) {
        btnActualizar.addActionListener(l);
    }

    // Registro

    public int getCodigo() {
        return ((Number) spnCodigo.getValue()).intValue();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public int getStock() {
        return ((Number) spnStock.getValue()).intValue();
    }

    public double getPrecio() {
        return Math.round(((Number) spnPrecio.getValue()).doubleValue() * 100.0) / 100.0;
    }

    // Deja el formulario de registro en sus valores iniciales (el mínimo de cada spinner).
    public void limpiarRegistro() {
        txtNombre.setText("");
        spnCodigo.setValue(((SpinnerNumberModel) spnCodigo.getModel()).getMinimum());
        spnStock.setValue(((SpinnerNumberModel) spnStock.getModel()).getMinimum());
        spnPrecio.setValue(((SpinnerNumberModel) spnPrecio.getModel()).getMinimum());
    }

    // Edición

    public void setModeloProductos(ComboBoxModel<producto> modelo) {
        cmbProductos.setModel(modelo);
    }

    public producto getProductoSeleccionado() {
        return (producto) cmbProductos.getSelectedItem();
    }

    public void setStockActual(String texto) {
        lblStockActual.setText(texto);
    }

    public int getNuevoStock() {
        return ((Number) spnNuevoStock.getValue()).intValue();
    }

    public void setNuevoStock(int valor) {
        spnNuevoStock.setValue(valor);
    }

    // Sin productos registrados no hay nada que editar.
    public void setEdicionHabilitada(boolean habilitada) {
        spnNuevoStock.setEnabled(habilitada);
        btnActualizar.setEnabled(habilitada);
    }

    // Mensajes

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Dato incorrecto", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
