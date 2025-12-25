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
import javax.swing.JTextArea;

public class Giris extends JFrame {

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
	/**
	 * Create the frame.
	 */
	public Giris() {
		setTitle("Giri\u015F Ekran");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 250, 600, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		usernameText = new JTextField();
		usernameText.setBounds(195, 148, 225, 30);
		contentPane.add(usernameText);
		usernameText.setColumns(10);
		
		passText = new JTextField();
		passText.setBounds(195, 198, 225, 30);
		contentPane.add(passText);
		passText.setColumns(10);
		
		JButton btnGiris = new JButton("Giri\u015F");
		btnGiris.setForeground(Color.BLACK);
		
		
		btnGiris.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnGiris.setBackground(new Color(128, 128, 0));
		btnGiris.setBounds(251, 241, 100, 30);
		contentPane.add(btnGiris);
		
		JLabel lblKullaniciAdi = new JLabel("Kullanıcı Adı:");
		lblKullaniciAdi.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKullaniciAdi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblKullaniciAdi.setForeground(new Color(255, 255, 255));
		lblKullaniciAdi.setBounds(105, 156, 80, 14);
		contentPane.add(lblKullaniciAdi);
		
		JLabel lblPinKodu = new JLabel("Şifre:");
		lblPinKodu.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPinKodu.setForeground(Color.WHITE);
		lblPinKodu.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPinKodu.setBounds(105, 206, 80, 14);
		contentPane.add(lblPinKodu);
		
		JTextArea txtrUserAdmin = new JTextArea();
		txtrUserAdmin.setText("super-admin -> admin  -> 9999\nadmin -> asli -> 8888\nkasiyer -> ayse -> 1234\nkasiyer -> mehmet -> 5678");
		txtrUserAdmin.setBounds(175, 374, 259, 70);
		contentPane.add(txtrUserAdmin);
		
		btnGiris.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				conn = (Connection) Baglanti.Bagla();
				int cnt = 0;
				String username = null, AdiSoyadi = null, Gorev = null;
				try
				{
					String sqlKomut = "SELECT * FROM Kullanici Where KullaniciAdi = ? AND PinKodu = ?";
					PreparedStatement prst = conn.prepareStatement(sqlKomut);
					prst.setString(1, usernameText.getText());
					prst.setString(2, passText.getText());
					ResultSet rslt = prst.executeQuery();
					while (rslt.next())
					{
						username = rslt.getString("KullaniciAdi");
						AdiSoyadi = rslt.getString("AdiSoyadi");
						Gorev = rslt.getString("Gorev");
						if(Gorev.compareTo("admin") == 0 || Gorev.compareTo("super-admin") == 0) {
							cnt = 1;
						} else if (Gorev.compareTo("kasiyer") == 0) {
							cnt = 2;
						}
					}
				
					if (cnt == 1)
					{
						dispose();
						AdminEkran ae = new AdminEkran(username);
						ae.setVisible(true);
					} else if(cnt == 2) {
						dispose();
						SatisEkran se = new SatisEkran(username);
						se.setVisible(true);
					}else
					{
						JOptionPane.showMessageDialog(null, "Başarısız..");
					}
					prst.close();
					rslt.close();
				} catch(Exception hata)
				{
					JOptionPane.showMessageDialog(null, hata);
				}
			}
		});
		
		
		
	}
}
