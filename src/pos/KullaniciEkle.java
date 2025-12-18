package pos;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class KullaniciEkle extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JTextField kullaniciAdiText;
	private JTextField adiSoyadiText;
	private JTextField pinKoduText;
	private JComboBox<String> gorevCombo;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KullaniciEkle frame = new KullaniciEkle();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection conn = null;
	/**
	 * Create the frame.
	 */
	public KullaniciEkle() {
		conn = Baglanti.Bagla();
		setTitle("Kullanici Ekleme Paneli");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 250, 600, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblKullaniciAdi = new JLabel("Kullanici Adi:");
		lblKullaniciAdi.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKullaniciAdi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblKullaniciAdi.setForeground(new Color(255, 255, 255));
		lblKullaniciAdi.setBounds(116, 163, 80, 14);
		contentPane.add(lblKullaniciAdi);
		
		JLabel lblAdiSoyadi = new JLabel("Adi Soyadi:");
		lblAdiSoyadi.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAdiSoyadi.setForeground(Color.WHITE);
		lblAdiSoyadi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAdiSoyadi.setBounds(116, 205, 80, 14);
		contentPane.add(lblAdiSoyadi);

		JLabel lblPinKodu = new JLabel("Pin Kodu:");
		lblPinKodu.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPinKodu.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPinKodu.setForeground(new Color(255, 255, 255));
		lblPinKodu.setBounds(116, 247, 80, 14);
		contentPane.add(lblPinKodu);
		
		JLabel lblGorev = new JLabel("Gorev:");
		lblGorev.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGorev.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGorev.setForeground(new Color(255, 255, 255));
		lblGorev.setBounds(116, 289, 80, 14);
		contentPane.add(lblGorev);
		
		kullaniciAdiText = new JTextField();
		kullaniciAdiText.setBounds(218, 156, 225, 30);
		contentPane.add(kullaniciAdiText);
		kullaniciAdiText.setColumns(10);
		
		adiSoyadiText = new JTextField();
		adiSoyadiText.setBounds(218, 198, 225, 30);
		contentPane.add(adiSoyadiText);
		adiSoyadiText.setColumns(10);
		
		pinKoduText = new JTextField();
		pinKoduText.setBounds(218, 240, 225, 30);
		contentPane.add(pinKoduText);
		pinKoduText.setColumns(10);
		
		gorevCombo = new JComboBox<String>();
		gorevCombo.addItem("kasiyer");
		gorevCombo.addItem("admin");
		gorevCombo.setBounds(218, 282, 225, 30);
		contentPane.add(gorevCombo);
		
		JButton btnKaydet = new JButton("Kaydet");
		btnKaydet.setForeground(Color.BLACK);
		
		btnKaydet.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        if (kullaniciAdiText.getText().isEmpty() || adiSoyadiText.getText().isEmpty() || pinKoduText.getText().isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Tüm alanların doldurulması zorunludur.", "Uyarı", JOptionPane.WARNING_MESSAGE);
		            return; 
		        }

		        PreparedStatement prst = null;
		        try {
		            String kullaniciAdi = kullaniciAdiText.getText();
		            String adiSoyadi = adiSoyadiText.getText();
		            
		            String pinStr = pinKoduText.getText();
		            int pinKodu = Integer.parseInt(pinStr);
		            
		            String gorev = (String) gorevCombo.getSelectedItem();

		            String komut = "INSERT INTO Kullanici (KullaniciAdi, AdiSoyadi, PinKodu, Gorev) VALUES (?, ?, ?, ?)";
		            prst = conn.prepareStatement(komut);

		            prst.setString(1, kullaniciAdi);   
		            prst.setString(2, adiSoyadi);  
		            prst.setInt(3, pinKodu);
		            prst.setString(4, gorev);    

		            prst.executeUpdate(); 

		            JOptionPane.showMessageDialog(null, "Kullanici başarıyla kaydedildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
		            
		            kullaniciAdiText.setText("");
		            adiSoyadiText.setText("");
		            pinKoduText.setText("");
		            gorevCombo.setSelectedIndex(0);
		            kullaniciAdiText.requestFocus(); 

		        } catch (NumberFormatException nfe) {
		            JOptionPane.showMessageDialog(null, "Lütfen Pin Kodu alanına geçerli bir sayı giriniz.", "Geçersiz Veri", JOptionPane.ERROR_MESSAGE);
		        
		        } catch (Exception hata) {
		            JOptionPane.showMessageDialog(null, "Bir veritabanı hatası oluştu: " + hata.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
		            hata.printStackTrace(); 
		        
		        } finally {
		            try {
		                if (prst != null) {
		                    prst.close();
		                }
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }
		        }
		        
		    }
		});
		
		btnKaydet.setForeground(Color.BLACK);
		btnKaydet.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
		btnKaydet.setBackground(Color.WHITE);
		btnKaydet.setBounds(421, 416, 173, 50);
		contentPane.add(btnKaydet);
		
		
		JButton btnGeri = new JButton("Geri");
		btnGeri.setForeground(Color.BLACK);
		
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
	}
		
}