package vista;

import java.awt.EventQueue;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


public class menuView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnInventario;
	private JButton btnReporte;
	private JButton btnPedido;
	private JButton btnLimpiar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menuView frame = new menuView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public menuView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 488, 337);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitulo = new JLabel("Cafeteria El Buen Sabor");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblTitulo.setBounds(119, 26, 262, 47);
		contentPane.add(lblTitulo);
		
		btnInventario = new JButton("Inventario");
		btnInventario.setBounds(41, 126, 135, 47);
		contentPane.add(btnInventario);
		
		btnReporte = new JButton("Ver Reporte");
		btnReporte.setBounds(41, 216, 135, 47);
		contentPane.add(btnReporte);
		
		btnPedido = new JButton("Realizar Pedido");
		btnPedido.setBounds(298, 126, 135, 47);
		contentPane.add(btnPedido);
		
		btnLimpiar = new JButton("Limpiar Datos");
		btnLimpiar.setBounds(298, 216, 135, 47);
		contentPane.add(btnLimpiar);
		
		setLocationRelativeTo(null);
	}
	public void addInventarioListener(ActionListener l) {
        btnInventario.addActionListener(l);
    }
 
    public void addPedidoListener(ActionListener l) {
        btnPedido.addActionListener(l);
    }
 
    public void addReporteListener(ActionListener l) {
        btnReporte.addActionListener(l);
    }
 
    public void addLimpiarListener(ActionListener l) {
        btnLimpiar.addActionListener(l);
    }
 
    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
 
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
 
    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
