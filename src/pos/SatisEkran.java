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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SatisEkran extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable satisTable;
    private DefaultTableModel model;
    private JLabel lblToplamTutar;
    private List<Integer> urunNoList;
    
    private double hesaplananTutar = 0.0;

    Connection conn = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SatisEkran frame = new SatisEkran();
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
    public SatisEkran() {
        conn = Baglanti.Bagla();
        urunNoList = new ArrayList<>();

        setTitle("Satış Ekranı");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 700, 550);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(82, 82, 82));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 664, 380);
        contentPane.add(scrollPane);

        satisTable = new JTable();
        scrollPane.setViewportView(satisTable);
        
        lblToplamTutar = new JLabel("Toplam Tutar: 0.00 TL");
        lblToplamTutar.setHorizontalAlignment(SwingConstants.CENTER);
        lblToplamTutar.setFont(new Font("Arial", Font.BOLD, 18));
        lblToplamTutar.setForeground(Color.WHITE);
        lblToplamTutar.setBounds(10, 402, 664, 25);
        contentPane.add(lblToplamTutar);

        JButton btnGeri = new JButton("Geri");
        btnGeri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	dispose();
				Giris ge = new Giris();
				ge.setVisible(true);
            }
        });
        btnGeri.setForeground(Color.BLACK);
        btnGeri.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnGeri.setBackground(Color.WHITE);
        btnGeri.setBounds(10, 450, 173, 50);
        contentPane.add(btnGeri);

        JButton btnHesapla = new JButton("Hesapla");
        btnHesapla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hesaplananTutar = 0.0;
                
                if (satisTable.isEditing()) {
                    satisTable.getCellEditor().stopCellEditing();
                }
                
                for (int i = 0; i < model.getRowCount(); i++) {
                    try {
                        double fiyat = Double.parseDouble(model.getValueAt(i, 1).toString());
                        int satisMiktari = Integer.parseInt(model.getValueAt(i, 3).toString());
                        hesaplananTutar += (fiyat * satisMiktari);
                    } catch (NumberFormatException ex) {
                
                    }
                }
                lblToplamTutar.setText(String.format("Toplam Tutar: %.2f TL", hesaplananTutar));
            }
        });
        btnHesapla.setForeground(Color.BLACK);
        btnHesapla.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnHesapla.setBackground(Color.WHITE);
        btnHesapla.setBounds(252, 450, 173, 50);
        contentPane.add(btnHesapla);

        JButton btnKaydet = new JButton("Kaydet");
        btnKaydet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (hesaplananTutar <= 0) {
                    JOptionPane.showMessageDialog(null, "Kaydedilecek bir satış yok. Lütfen önce ürün miktarlarını girip 'Hesapla' butonuna basın.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (satisTable.isEditing()) {
                    satisTable.getCellEditor().stopCellEditing();
                }

                int confirmation = JOptionPane.showConfirmDialog(null, 
                        String.format("Toplam %.2f TL tutarındaki satışı onaylıyor musunuz? Stoklar güncellenecektir.", hesaplananTutar),
                        "Satış Onayı", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        conn.setAutoCommit(false); 

                        boolean stokYeterli = true;
                       
                        for (int i = 0; i < model.getRowCount(); i++) {
                            int satisMiktari = Integer.parseInt(model.getValueAt(i, 3).toString());
                            if (satisMiktari > 0) {
                                int mevcutStok = Integer.parseInt(model.getValueAt(i, 2).toString());
                                if (satisMiktari > mevcutStok) {
                                    JOptionPane.showMessageDialog(null, "Yetersiz stok! '" + model.getValueAt(i, 0) + "' için stokta sadece " + mevcutStok + " adet var.", "Stok Hatası", JOptionPane.ERROR_MESSAGE);
                                    stokYeterli = false;
                                    break; 
                                }

                                int urunNo = urunNoList.get(i); 
                                String sql = "UPDATE Urunler SET Stok = Stok - ? WHERE UrunNo = ?";
                                PreparedStatement prst = conn.prepareStatement(sql);
                                prst.setInt(1, satisMiktari);
                                prst.setInt(2, urunNo);
                                prst.executeUpdate();
                                prst.close();
                            }
                        }

                        if (stokYeterli) {
                            conn.commit();
                            JOptionPane.showMessageDialog(null, "Satış başarıyla tamamlandı. Stoklar güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                            urunleriYukle(); 
                            lblToplamTutar.setText("Toplam Tutar: 0.00 TL");
                            hesaplananTutar = 0.0;
                        } else {
                            conn.rollback();
                        }

                    } catch (Exception hata) {
                        try { conn.rollback(); } catch (Exception ex) {}
                        JOptionPane.showMessageDialog(null, "Bir hata oluştu: " + hata.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                        hata.printStackTrace();
                    } finally {
                        try { conn.setAutoCommit(true); } catch (Exception ex) {}
                    }
                }
            }
        });
        btnKaydet.setForeground(Color.BLACK);
        btnKaydet.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnKaydet.setBackground(Color.WHITE);
        btnKaydet.setBounds(494, 450, 173, 50);
        contentPane.add(btnKaydet);

        urunleriYukle(); 
    }

    /**
     * Diğerleriyle aynı ama son column editable
     */
    private void urunleriYukle() {
        urunNoList.clear(); 
        
        String[] columnNames = {"Ürün Adı", "Fiyat (TL)", "Stok", "Satış Miktarı"};
        
        model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        try {
            String sql = "SELECT UrunNo, Ad, Fiyat, Stok FROM Urunler WHERE Stok > 0 ORDER BY Ad";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                urunNoList.add(rs.getInt("UrunNo"));

                String ad = rs.getString("Ad");
                double fiyat = rs.getDouble("Fiyat");
                int stok = rs.getInt("Stok");

                Object[] row = {ad, fiyat, stok, 0}; 
                model.addRow(row);
            }
            
            satisTable.setModel(model);
            rs.close();
            stmt.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ürünler yüklenirken hata: " + e.getMessage());
            e.printStackTrace();
        }
    }
}