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
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class UrunDuzelt extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable urunlerTable;
    private DefaultTableModel model;

    Connection conn = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UrunDuzelt frame = new UrunDuzelt();
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
    public UrunDuzelt() {
        conn = Baglanti.Bagla();
        setTitle("Ürün Düzeltme Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 600, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(82, 82, 82));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 564, 385);
        contentPane.add(scrollPane);

        urunlerTable = new JTable();
        scrollPane.setViewportView(urunlerTable);

        JButton btnKaydet = new JButton("Kaydet");
        btnKaydet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (urunlerTable.isEditing()) {
                    urunlerTable.getCellEditor().stopCellEditing();
                }

                try {
                    HashSet<String> productNames = new HashSet<>();
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String urunAdi = model.getValueAt(i, 1).toString().trim();

                        if (urunAdi.isEmpty()) {
                            JOptionPane.showMessageDialog(null,
                                "Satır " + (i + 1) + ": Ürün adı boş olamaz.",
                                "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (productNames.contains(urunAdi.toLowerCase())) {
                            JOptionPane.showMessageDialog(null,
                                "İki ürün aynı isme sahip olamaz: '" + urunAdi + "'",
                                "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        productNames.add(urunAdi.toLowerCase());
                    }

                    for (int i = 0; i < model.getRowCount(); i++) {
                        try {
                            String fiyatStr = model.getValueAt(i, 2).toString().replace(',', '.');
                            double fiyat = Double.parseDouble(fiyatStr);

                            if (fiyat < 0) {
                                JOptionPane.showMessageDialog(null,
                                    "Satır " + (i + 1) + ": Fiyat negatif olamaz.",
                                    "Hata", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(null,
                                "Satır " + (i + 1) + ": Geçersiz fiyat değeri.",
                                "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try {
                            int stok = Integer.parseInt(model.getValueAt(i, 3).toString());

                            if (stok < 0) {
                                JOptionPane.showMessageDialog(null,
                                    "Satır " + (i + 1) + ": Stok negatif olamaz.",
                                    "Hata", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(null,
                                "Satır " + (i + 1) + ": Geçersiz stok değeri.",
                                "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    int updatedCount = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        int urunNo = Integer.parseInt(model.getValueAt(i, 0).toString());
                        String ad = model.getValueAt(i, 1).toString().trim();
                        String fiyatStr = model.getValueAt(i, 2).toString().replace(',', '.');
                        double fiyat = Double.parseDouble(fiyatStr);
                        int stok = Integer.parseInt(model.getValueAt(i, 3).toString());

                        String sql = "UPDATE Urunler SET Ad = ?, Fiyat = ?, Stok = ? WHERE UrunNo = ?";
                        PreparedStatement prst = conn.prepareStatement(sql);
                        prst.setString(1, ad);
                        prst.setDouble(2, fiyat);
                        prst.setInt(3, stok);
                        prst.setInt(4, urunNo);

                        int affected = prst.executeUpdate();
                        updatedCount += affected;
                        prst.close();
                    }

                    JOptionPane.showMessageDialog(null,
                        updatedCount + " ürün başarıyla güncellendi.",
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);

                    urunleriYukle();

                } catch (Exception hata) {
                    JOptionPane.showMessageDialog(null,
                        "Veritabanı hatası: " + hata.getMessage(),
                        "Hata", JOptionPane.ERROR_MESSAGE);
                    hata.printStackTrace();
                }
            }
        });
        btnKaydet.setForeground(Color.BLACK);
        btnKaydet.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnKaydet.setBackground(Color.WHITE);
        btnKaydet.setBounds(401, 407, 173, 50);
        contentPane.add(btnKaydet);

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
        btnGeri.setBounds(10, 407, 173, 50);
        contentPane.add(btnGeri);

        urunleriYukle();
    }

    /**
     * Fetch Urunler and create an editable JTable
     */
    private void urunleriYukle() {
        String[] columnNames = {"UrunNo", "Ad", "Fiyat", "Stok"};

        // Make table editable except for UrunNo column (column 0)
        model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Only UrunNo (column 0) is not editable
            }
        };

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
