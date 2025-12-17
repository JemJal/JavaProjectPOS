package pos;
import java.awt.EventQueue;

import java.sql.*;
import java.sql.DriverManager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SatisEkran extends JFrame {

	private JPanel contentPane;
	private JTextField usernameText;
	private JTextField passText;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Giris frame = new Giris();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Connection conn =  null;
	
	JLabel lblUsername, lblAdiSoyadi, lblGorev;
	
	/**
	 * Create the frame.
	 */
	public SatisEkran() {
		setTitle("Giri\u015F Ekran");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 250, 500, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblUsername = new JLabel("");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		lblUsername.setBounds(164, 54, 101, 50);
		contentPane.add(lblUsername);
		
		lblAdiSoyadi = new JLabel("");
		lblAdiSoyadi.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdiSoyadi.setForeground(Color.WHITE);
		lblAdiSoyadi.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		lblAdiSoyadi.setBounds(275, 54, 126, 50);
		contentPane.add(lblAdiSoyadi);
		
		lblGorev = new JLabel("");
		lblGorev.setHorizontalAlignment(SwingConstants.CENTER);
		lblGorev.setForeground(Color.WHITE);
		lblGorev.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		lblGorev.setBounds(164, 11, 155, 50);
		contentPane.add(lblGorev);
		
		
	}
}
