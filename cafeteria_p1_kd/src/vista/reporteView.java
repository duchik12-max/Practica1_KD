package vista;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import modelo.pedido;

@SuppressWarnings("serial")
public class reporteView extends JDialog {

    private JPanel contentPane;
    private JLabel lblTituloPedidos;
    private JScrollPane scrPedidos;
    private JTable tblPedidos;
    private JLabel lblTotalVentas;
    private JLabel lblTituloMasVendidos;
    private JScrollPane scrMasVendidos;
    private JTextArea txaMasVendidos;

    public reporteView(Window owner) {
        super(owner, "Reporte de ventas", Dialog.ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 420, 430);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        lblTituloPedidos = new JLabel("Pedidos registrados");
        lblTituloPedidos.setBounds(15, 10, 200, 20);
        contentPane.add(lblTituloPedidos);

        scrPedidos = new JScrollPane();
        scrPedidos.setBounds(15, 32, 375, 200);
        contentPane.add(scrPedidos);

        tblPedidos = new JTable();
        tblPedidos.getTableHeader().setReorderingAllowed(false);
        scrPedidos.setViewportView(tblPedidos);

        lblTotalVentas = new JLabel("Total de ventas: $0.00");
        lblTotalVentas.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTotalVentas.setBounds(15, 245, 375, 24);
        contentPane.add(lblTotalVentas);

        lblTituloMasVendidos = new JLabel("Producto(s) más vendido(s):");
        lblTituloMasVendidos.setBounds(15, 280, 300, 20);
        contentPane.add(lblTituloMasVendidos);

        scrMasVendidos = new JScrollPane();
        scrMasVendidos.setBounds(15, 302, 375, 70);
        contentPane.add(scrMasVendidos);

        txaMasVendidos = new JTextArea();
        txaMasVendidos.setEditable(false);
        scrMasVendidos.setViewportView(txaMasVendidos);

        setLocationRelativeTo(owner);
    }

    // ---------- Métodos para el controlador (van después del diseño) ----------

    public void setModeloPedidos(TableModel modelo) {
        tblPedidos.setModel(modelo);
        pedidoView.aplicarFormatoMoneda(tblPedidos);
    }

    public void setTotalVentas(double total) {
        lblTotalVentas.setText("Total de ventas: " + pedido.formatoDinero(total));
    }

    public void setMasVendidos(String texto) {
        txaMasVendidos.setText(texto);
        txaMasVendidos.setCaretPosition(0);
    }
}
