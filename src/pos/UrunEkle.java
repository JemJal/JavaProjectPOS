package pos;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class UrunEkle extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JTextField urunAdiText;
	private JTextField urunMiktarText;
	private JTextField urunFiyatText;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UrunEkle frame = new UrunEkle();
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
	public UrunEkle() {
		conn = Baglanti.Bagla();
		setTitle("Urun Ekleme Paneli");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 250, 600, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUrunAdi = new JLabel("Urun Adı:");
		lblUrunAdi.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUrunAdi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUrunAdi.setForeground(new Color(255, 255, 255));
		lblUrunAdi.setBounds(116, 163, 80, 14);
		contentPane.add(lblUrunAdi);
		
		JLabel lblMiktari = new JLabel("Miktar:");
		lblMiktari.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMiktari.setForeground(Color.WHITE);
		lblMiktari.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMiktari.setBounds(116, 247, 80, 14);
		contentPane.add(lblMiktari);

		JLabel lblFiyat = new JLabel("Fiyati:");
		lblFiyat.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFiyat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFiyat.setForeground(new Color(255, 255, 255));
		lblFiyat.setBounds(116, 205, 80, 14);
		contentPane.add(lblFiyat);
		
		urunAdiText = new JTextField();
		urunAdiText.setBounds(218, 156, 225, 30);
		contentPane.add(urunAdiText);
		urunAdiText.setColumns(10);
		
		urunMiktarText = new JTextField();
		urunMiktarText.setBounds(218, 240, 225, 30);
		contentPane.add(urunMiktarText);
		urunMiktarText.setColumns(10);
		
		urunFiyatText = new JTextField();
		urunFiyatText.setBounds(218, 198, 225, 30);
		contentPane.add(urunFiyatText);
		urunFiyatText.setColumns(10);
		
		JButton btnKaydet = new JButton("Kaydet");
		btnKaydet.setForeground(Color.BLACK);
		
		btnKaydet.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        if (urunAdiText.getText().isEmpty() || urunFiyatText.getText().isEmpty() || urunMiktarText.getText().isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Tüm alanların doldurulması zorunludur.", "Uyarı", JOptionPane.WARNING_MESSAGE);
		            return; 
		        }

		        PreparedStatement prst = null;
		        try {
		            String ad = urunAdiText.getText();
		            
		            String fiyatStr = urunFiyatText.getText().replace(',', '.');
		            double fiyat = Double.parseDouble(fiyatStr);
		            
		            String stokStr = urunMiktarText.getText();
		            int stok = Integer.parseInt(stokStr);

		            String komut = "INSERT INTO Urunler (Ad, Fiyat, Stok) VALUES (?, ?, ?)";
		            prst = conn.prepareStatement(komut);

		            prst.setString(1, ad);   
		            prst.setDouble(2, fiyat);  
		            prst.setInt(3, stok);    

		            prst.executeUpdate(); 

		            JOptionPane.showMessageDialog(null, "Ürün başarıyla kaydedildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
		            
		            urunAdiText.setText("");
		            urunFiyatText.setText("");
		            urunMiktarText.setText("");
		            urunAdiText.requestFocus(); 

		        } catch (NumberFormatException nfe) {
		            JOptionPane.showMessageDialog(null, "Lütfen Fiyat ve Miktar alanlarına geçerli sayılar giriniz.", "Geçersiz Veri", JOptionPane.ERROR_MESSAGE);
		        
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