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

public class AdminEkran extends JFrame {

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
	public AdminEkran() {
		setTitle("Admin Paneli");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 250, 600, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnYeniUrun = new JButton("Yeni Urun Ekle");
		btnYeniUrun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UrunEkle uee = new UrunEkle();
				uee.setVisible(true);
			}
		});
		
		btnYeniUrun.setBackground(new Color(255, 255, 255));
		btnYeniUrun.setForeground(Color.BLACK);
		btnYeniUrun.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnYeniUrun.setBounds(145, 117, 310, 50);
		contentPane.add(btnYeniUrun);
		
		JButton btnYeniKasiyer = new JButton("Yeni Kasiyer Ekle");
		btnYeniKasiyer.setForeground(Color.BLACK);
		btnYeniKasiyer.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnYeniKasiyer.setBackground(Color.WHITE);
		btnYeniKasiyer.setBounds(145, 306, 310, 50);
		contentPane.add(btnYeniKasiyer);
		
		JButton btnUrunSil = new JButton("Urun Sil");
		btnUrunSil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UrunSil use = new UrunSil();
				use.setVisible(true);
			}
		});
		btnUrunSil.setBackground(new Color(255, 255, 255));
		btnUrunSil.setForeground(Color.BLACK);
		btnUrunSil.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnUrunSil.setBounds(145, 220, 310, 50);
		contentPane.add(btnUrunSil);
		
		JButton btnKasiyerSil = new JButton("Kasiyer Sil");
		btnKasiyerSil.setForeground(Color.BLACK);
		btnKasiyerSil.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnKasiyerSil.setBackground(Color.WHITE);
		btnKasiyerSil.setBounds(145, 354, 310, 50);
		contentPane.add(btnKasiyerSil);
		
		JLabel lblAdminPaneli = new JLabel("Admin Paneli");
		lblAdminPaneli.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminPaneli.setForeground(Color.WHITE);
		lblAdminPaneli.setFont(new Font("Arial Narrow", Font.PLAIN, 32));
		lblAdminPaneli.setBounds(145, 6, 310, 50);
		contentPane.add(lblAdminPaneli);
		
		JLabel lblUrunIslemleri = new JLabel("Urun Islemleri:");
		lblUrunIslemleri.setVerticalAlignment(SwingConstants.BOTTOM);
		lblUrunIslemleri.setHorizontalAlignment(SwingConstants.LEFT);
		lblUrunIslemleri.setForeground(Color.WHITE);
		lblUrunIslemleri.setFont(new Font("Arial Narrow", Font.PLAIN, 21));
		lblUrunIslemleri.setBounds(145, 68, 155, 50);
		contentPane.add(lblUrunIslemleri);
		
		JLabel lblPersonelIslemleri = new JLabel("Personel Islemleri:");
		lblPersonelIslemleri.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPersonelIslemleri.setHorizontalAlignment(SwingConstants.LEFT);
		lblPersonelIslemleri.setForeground(Color.WHITE);
		lblPersonelIslemleri.setFont(new Font("Arial Narrow", Font.PLAIN, 21));
		lblPersonelIslemleri.setBounds(145, 257, 155, 50);
		contentPane.add(lblPersonelIslemleri);
		
		JButton btnCikis = new JButton("Çıkış");
		btnCikis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Giris ge = new Giris();
				ge.setVisible(true);
			}
		});
		btnCikis.setForeground(Color.BLACK);
		btnCikis.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnCikis.setBackground(Color.WHITE);
		btnCikis.setBounds(368, 416, 173, 50);
		contentPane.add(btnCikis);
		
		JButton btnStokEkle = new JButton("Stok Ekle");
		btnStokEkle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				StokEkle see = new StokEkle();
				see.setVisible(true);
			}
		});
		btnStokEkle.setForeground(Color.BLACK);
		btnStokEkle.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnStokEkle.setBackground(Color.WHITE);
		btnStokEkle.setBounds(145, 167, 310, 50);
		contentPane.add(btnStokEkle);
	}
}
