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

public class StokEkle extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable urunlerTable;
    private JTextField urunNoText;
    private JTextField miktarText; // New text field for the amount
    private DefaultTableModel model;

    Connection conn = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StokEkle frame = new StokEkle();
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
    public StokEkle() {
        conn = Baglanti.Bagla();
        setTitle("Stok Ekleme Paneli");
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

        // --- UI for Inputs (Modified for two inputs) ---
        JLabel lblUrunNo = new JLabel("Ürün No:");
        lblUrunNo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUrunNo.setForeground(Color.WHITE);
        lblUrunNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblUrunNo.setBounds(30, 350, 110, 30);
        contentPane.add(lblUrunNo);

        urunNoText = new JTextField();
        urunNoText.setFont(new Font("Tahoma", Font.BOLD, 14));
        urunNoText.setBounds(150, 350, 100, 30);
        contentPane.add(urunNoText);
        urunNoText.setColumns(10);

        JLabel lblMiktar = new JLabel("Eklenecek Miktar:");
        lblMiktar.setHorizontalAlignment(SwingConstants.RIGHT);
        lblMiktar.setForeground(Color.WHITE);
        lblMiktar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblMiktar.setBounds(270, 350, 120, 30);
        contentPane.add(lblMiktar);

        miktarText = new JTextField(); // The new text field
        miktarText.setFont(new Font("Tahoma", Font.BOLD, 14));
        miktarText.setBounds(400, 350, 100, 30);
        contentPane.add(miktarText);
        miktarText.setColumns(10);
        
        // The "Stok Ekle" (Add Stock) Button
        JButton btnStokEkle = new JButton("Stok Ekle");
        btnStokEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get text from both fields
                String urunNoStr = urunNoText.getText();
                String miktarStr = miktarText.getText();

                // Check if either field is empty
                if (urunNoStr.isEmpty() || miktarStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen hem Ürün No hem de Miktar giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int urunNoToUpdate = Integer.parseInt(urunNoStr);
                    int miktarToAdd = Integer.parseInt(miktarStr);

                    // A simple check to ensure a positive amount is being added
                    if (miktarToAdd <= 0) {
                        JOptionPane.showMessageDialog(null, "Lütfen 0'dan büyük bir miktar giriniz.", "Geçersiz Miktar", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // *** THE CORE LOGIC: UPDATE a record ***
                    // This SQL command adds the new amount to the existing Stok value.
                    String sql = "UPDATE Urunler SET Stok = Stok + ? WHERE UrunNo = ?";
                    
                    PreparedStatement prst = conn.prepareStatement(sql);
                    // IMPORTANT: The order of parameters must match the '?' in the SQL query
                    prst.setInt(1, miktarToAdd);      // First '?': the amount to add
                    prst.setInt(2, urunNoToUpdate);   // Second '?': the product number

                    int affectedRows = prst.executeUpdate();

                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Stok başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        urunleriYukle(); // Refresh the table
                        urunNoText.setText("");
                        miktarText.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Bu Ürün No ile eşleşen bir ürün bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                    prst.close();

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Lütfen geçerli sayısal değerler giriniz.", "Geçersiz Giriş", JOptionPane.ERROR_MESSAGE);
                } catch (Exception hata) {
                    JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + hata.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                    hata.printStackTrace();
                }
            }
        });
        btnStokEkle.setForeground(Color.BLACK);
        btnStokEkle.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnStokEkle.setBackground(Color.WHITE);
        btnStokEkle.setBounds(401, 416, 173, 50);
        contentPane.add(btnStokEkle);

        // The "Geri" (Back) Button - unchanged from UrunSil.java
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
     * This method is identical to the one in UrunSil.java.
     * It fetches data from the Urunler table and populates the JTable.
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