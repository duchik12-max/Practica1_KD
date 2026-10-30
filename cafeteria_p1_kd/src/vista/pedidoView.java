package vista;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import modelo.pedido;

@SuppressWarnings("serial")
public class pedidoView extends JDialog {

    private JPanel contentPane;
    private JLabel lblCatalogo;
    private JScrollPane scrCatalogo;
    private JTable tblCatalogo;
    private JLabel lblCantidad;
    private JSpinner spnCantidad;
    private JButton btnAgregar;
    private JLabel lblPedidoActual;
    private JScrollPane scrPedido;
    private JTable tblPedido;
    private JLabel lblTotal;
    private JButton btnFinalizar;
    private JButton btnCancelar;

    public pedidoView(Window owner) {
        super(owner, "Realizar pedido", Dialog.ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 620, 450);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        lblCatalogo = new JLabel("Catálogo (unidades disponibles)");
        lblCatalogo.setBounds(15, 10, 250, 20);
        contentPane.add(lblCatalogo);

        scrCatalogo = new JScrollPane();
        scrCatalogo.setBounds(15, 32, 380, 170);
        contentPane.add(scrCatalogo);

        tblCatalogo = new JTable();
        tblCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCatalogo.getTableHeader().setReorderingAllowed(false);
        scrCatalogo.setViewportView(tblCatalogo);

        lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(415, 45, 100, 20);
        contentPane.add(lblCantidad);

        // La cantidad mínima es 1; el máximo lo ajusta el controlador según el producto elegido.
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
        spnCantidad.setBounds(415, 67, 100, 26);
        contentPane.add(spnCantidad);

        btnAgregar = new JButton("Agregar al pedido");
        btnAgregar.setBounds(415, 110, 170, 32);
        contentPane.add(btnAgregar);

        lblPedidoActual = new JLabel("Pedido actual");
        lblPedidoActual.setBounds(15, 212, 200, 20);
        contentPane.add(lblPedidoActual);

        scrPedido = new JScrollPane();
        scrPedido.setBounds(15, 234, 380, 150);
        contentPane.add(scrPedido);

        tblPedido = new JTable();
        tblPedido.getTableHeader().setReorderingAllowed(false);
        scrPedido.setViewportView(tblPedido);

        lblTotal = new JLabel("TOTAL: $0.00");
        lblTotal.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTotal.setBounds(415, 236, 170, 30);
        contentPane.add(lblTotal);

        btnFinalizar = new JButton("Finalizar pedido");
        btnFinalizar.setBounds(415, 285, 170, 32);
        contentPane.add(btnFinalizar);

        btnCancelar = new JButton("Cancelar pedido");
        btnCancelar.setBounds(415, 330, 170, 32);
        contentPane.add(btnCancelar);

        setLocationRelativeTo(owner);
    }

    // ---------- Métodos para el controlador (van después del diseño) ----------

    public void addAgregarListener(ActionListener l) {
        btnAgregar.addActionListener(l);
    }

    public void addFinalizarListener(ActionListener l) {
        btnFinalizar.addActionListener(l);
    }

    public void addCancelarListener(ActionListener l) {
        btnCancelar.addActionListener(l);
    }

    public void addCatalogoSeleccionListener(ListSelectionListener l) {
        tblCatalogo.getSelectionModel().addListSelectionListener(l);
    }

    // Al pulsar la X de la ventana se ejecuta la acción indicada (cancelar el pedido).
    public void alCerrar(Runnable accion) {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                accion.run();
            }
        });
    }

    public void cerrar() {
        dispose();
    }

    public void setModeloCatalogo(TableModel modelo) {
        tblCatalogo.setModel(modelo);
        aplicarFormatoMoneda(tblCatalogo);
    }

    public void setModeloPedido(TableModel modelo) {
        tblPedido.setModel(modelo);
        aplicarFormatoMoneda(tblPedido);
    }

    public int getFilaCatalogo() {
        return tblCatalogo.getSelectedRow();
    }

    public int getCantidad() {
        return ((Number) spnCantidad.getValue()).intValue();
    }

    // Ajusta el máximo del spinner (unidades disponibles del producto elegido).
    public void setCantidadMaxima(int maximo) {
        SpinnerNumberModel modelo = (SpinnerNumberModel) spnCantidad.getModel();
        int tope = Math.max(maximo, 1);
        modelo.setMaximum(tope);
        if (((Number) modelo.getValue()).intValue() > tope) {
            modelo.setValue(tope);
        }
    }

    public void setTotal(double total) {
        lblTotal.setText("TOTAL: " + pedido.formatoDinero(total));
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Dato incorrecto", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // ---------- Delegado de presentación ----------
    // Dibuja las columnas de tipo Double como dinero ($0.00). También lo usa reporteView.

    public static void aplicarFormatoMoneda(JTable tabla) {
        tabla.setDefaultRenderer(Double.class, new monedaRenderer());
    }

    @SuppressWarnings("serial")
    public static class monedaRenderer extends DefaultTableCellRenderer {
        public monedaRenderer() {
            setHorizontalAlignment(RIGHT);
        }

        @Override
        protected void setValue(Object valor) {
            if (valor instanceof Number) {
                setText(pedido.formatoDinero(((Number) valor).doubleValue()));
            } else {
                super.setValue(valor);
            }
        }
    }
}
