package pos;
import java.awt.EventQueue;

import java.sql.*;

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
	
	JLabel lblUsername;
	private JLabel lblAdminPaneli;
	
	/**
	 * Create the frame.
	 */
	public SatisEkran() {
		setTitle("Giri\u015F Ekran");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 250, 600, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblUsername = new JLabel("");
		lblUsername.setVerticalAlignment(SwingConstants.TOP);
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Arial Narrow", Font.PLAIN, 22));
		lblUsername.setBounds(147, 6, 218, 50);
		contentPane.add(lblUsername);
		
		lblAdminPaneli = new JLabel("Güncel kullanıcı:");
		lblAdminPaneli.setVerticalAlignment(SwingConstants.TOP);
		lblAdminPaneli.setHorizontalAlignment(SwingConstants.LEFT);
		lblAdminPaneli.setForeground(Color.WHITE);
		lblAdminPaneli.setFont(new Font("Arial Narrow", Font.PLAIN, 22));
		lblAdminPaneli.setBounds(6, 6, 145, 50);
		contentPane.add(lblAdminPaneli);
		
		JButton btnGeri = new JButton("Geri");
        btnGeri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdminEkran ae = new AdminEkran();
                ae.setVisible(true);
            }
        });
        btnGeri.setForeground(Color.BLACK);
        btnGeri.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnGeri.setBackground(Color.WHITE);
        btnGeri.setBounds(6, 416, 173, 50);
        contentPane.add(btnGeri);

        JButton btnKaydet = new JButton("Kaydet");
		btnKaydet.setForeground(Color.BLACK);
		
		btnKaydet.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    }
		});
		
		btnKaydet.setForeground(Color.BLACK);
		btnKaydet.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnKaydet.setBackground(Color.WHITE);
		btnKaydet.setBounds(421, 416, 173, 50);
		contentPane.add(btnKaydet);
		
	}
}
