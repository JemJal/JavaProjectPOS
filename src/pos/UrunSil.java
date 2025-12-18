package pos;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class UrunSil extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable urunlerTable;
    private JTextField urunNoText;
    private DefaultTableModel model;

    Connection conn = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UrunSil frame = new UrunSil();
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
    public UrunSil() {
        conn = Baglanti.Bagla();
        setTitle("Ürün Silme Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 600, 500); 
        contentPane = new JPanel();
        contentPane.setBackground(new Color(82, 82, 82));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 564, 320); 
        contentPane.add(scrollPane);

        urunlerTable = new JTable();
        scrollPane.setViewportView(urunlerTable);

        JLabel lblUrunNo = new JLabel("Silinecek Ürün No:");
        lblUrunNo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUrunNo.setForeground(Color.WHITE);
        lblUrunNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblUrunNo.setBounds(110, 350, 130, 30);
        contentPane.add(lblUrunNo);

        urunNoText = new JTextField();
        urunNoText.setFont(new Font("Tahoma", Font.BOLD, 14));
        urunNoText.setBounds(250, 350, 150, 30);
        contentPane.add(urunNoText);
        urunNoText.setColumns(10);

        JButton btnSil = new JButton("Sil");
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String urunNoStr = urunNoText.getText();

                if (urunNoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen silmek için bir Ürün No giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int urunNoToDelete = Integer.parseInt(urunNoStr);

                    int confirmation = JOptionPane.showConfirmDialog(null, 
                            "Ürün No " + urunNoToDelete + " olan ürünü silmek istediğinize emin misiniz?", 
                            "Silme Onayı", 
                            JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        String sql = "DELETE FROM Urunler WHERE UrunNo = ?";
                        PreparedStatement prst = conn.prepareStatement(sql);
                        prst.setInt(1, urunNoToDelete);

                        int affectedRows = prst.executeUpdate();

                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(null, "Ürün başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                            urunleriYukle();
                            urunNoText.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Bu Ürün No ile eşleşen bir ürün bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                        prst.close();
                    }

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayısal Ürün No giriniz.", "Geçersiz Giriş", JOptionPane.ERROR_MESSAGE);
                } catch (Exception hata) {
                    JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + hata.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                    hata.printStackTrace();
                }
            }
        });
        btnSil.setForeground(Color.BLACK);
        btnSil.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnSil.setBackground(Color.WHITE);
        btnSil.setBounds(401, 416, 173, 50);
        contentPane.add(btnSil);

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
        btnGeri.setBounds(10, 416, 173, 50);
        contentPane.add(btnGeri);

        urunleriYukle();
    }

    /**
     * Fetch Urunler and create a JTable
     */
    private void urunleriYukle() {
        String[] columnNames = {"UrunNo", "Ad", "Fiyat", "Stok"};
        model = new DefaultTableModel(columnNames, 0);

        try {
            String sql = "SELECT UrunNo, Ad, Fiyat, Stok FROM Urunler ORDER BY UrunNo";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int urunNo = rs.getInt("UrunNo");
                String ad = rs.getString("Ad");
                double fiyat = rs.getDouble("Fiyat");
                int stok = rs.getInt("Stok");

                Object[] row = {urunNo, ad, fiyat, stok};
                model.addRow(row);
            }
            
            urunlerTable.setModel(model);
            
            rs.close();
            stmt.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ürünler yüklenirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}