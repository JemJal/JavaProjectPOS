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

	private String currentUsername;

	/**
	 * Create the frame.
	 */
	public AdminEkran() {
		this("");
	}

	/**
	 * Create the frame with username.
	 */
	public AdminEkran(String username) {
		this.currentUsername = username;
		setTitle("Admin Paneli");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 250, 600, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnYeniUrun = new JButton("Yeni Ürün Ekle");
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
		btnYeniUrun.setBounds(60, 120, 221, 50);
		contentPane.add(btnYeniUrun);
		
		JButton btnYeniKasiyer = new JButton("Yeni Kullanıcı Ekle");
		btnYeniKasiyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				KullaniciEkle ke = new KullaniciEkle();
				ke.setVisible(true);
			}
		});
		btnYeniKasiyer.setForeground(Color.BLACK);
		btnYeniKasiyer.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnYeniKasiyer.setBackground(Color.WHITE);
		btnYeniKasiyer.setBounds(145, 306, 310, 50);
		contentPane.add(btnYeniKasiyer);
		
		JButton btnUrunDuzelt = new JButton("Ürün Düzelt");
		btnUrunDuzelt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UrunDuzelt ude = new UrunDuzelt();
				ude.setVisible(true);
			}
		});
		btnUrunDuzelt.setBackground(new Color(255, 255, 255));
		btnUrunDuzelt.setForeground(Color.BLACK);
		btnUrunDuzelt.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnUrunDuzelt.setBounds(279, 120, 221, 50);
		contentPane.add(btnUrunDuzelt);
		
		JButton btnKasiyerSil = new JButton("Kullanıcı Sil");
		btnKasiyerSil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				KullaniciSil ks = new KullaniciSil(currentUsername);
				ks.setVisible(true);
			}
		});
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
		
		JLabel lblUrunIslemleri = new JLabel("Ürün İşlemleri:");
		lblUrunIslemleri.setVerticalAlignment(SwingConstants.BOTTOM);
		lblUrunIslemleri.setHorizontalAlignment(SwingConstants.LEFT);
		lblUrunIslemleri.setForeground(Color.WHITE);
		lblUrunIslemleri.setFont(new Font("Arial Narrow", Font.PLAIN, 21));
		lblUrunIslemleri.setBounds(145, 68, 155, 50);
		contentPane.add(lblUrunIslemleri);
		
		JLabel lblPersonelIslemleri = new JLabel("Personel İşlemleri:");
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
		btnCikis.setBounds(421, 516, 173, 50);
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
		btnStokEkle.setBounds(60, 166, 221, 50);
		contentPane.add(btnStokEkle);
		
		JLabel lblSatisIslemleri = new JLabel("Satış İşlemleri:");
		lblSatisIslemleri.setVerticalAlignment(SwingConstants.BOTTOM);
		lblSatisIslemleri.setHorizontalAlignment(SwingConstants.LEFT);
		lblSatisIslemleri.setForeground(Color.WHITE);
		lblSatisIslemleri.setFont(new Font("Arial Narrow", Font.PLAIN, 21));
		lblSatisIslemleri.setBounds(145, 395, 155, 50);
		contentPane.add(lblSatisIslemleri);
		
		JButton btnSatislariIncele = new JButton("Satışları İncele");
		btnSatislariIncele.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Satislar sie = new Satislar();
				sie.setVisible(true);
			}
		});
		btnSatislariIncele.setForeground(Color.BLACK);
		btnSatislariIncele.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnSatislariIncele.setBackground(Color.WHITE);
		btnSatislariIncele.setBounds(145, 444, 310, 50);
		contentPane.add(btnSatislariIncele);
		
		JButton btnUrunSil_1 = new JButton("Ürün Sil");
		btnUrunSil_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnUrunSil_1.setForeground(Color.BLACK);
		btnUrunSil_1.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnUrunSil_1.setBackground(Color.WHITE);
		btnUrunSil_1.setBounds(279, 166, 221, 50);
		contentPane.add(btnUrunSil_1);
	}
}
