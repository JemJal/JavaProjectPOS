package pos;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

public class SatisDetay extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable detayTable;
    private DefaultTableModel model;
    private int satisNo;
    Connection conn = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SatisDetay frame = new SatisDetay(1);
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
    public SatisDetay(int satisNo) {
        this.satisNo = satisNo;
        conn = Baglanti.Bagla();
        setTitle("Satış Detayı - Satış No: " + satisNo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 250, 700, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(82, 82, 82));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblBaslik = new JLabel("Satış No: " + satisNo + " - Detaylar");
        lblBaslik.setHorizontalAlignment(SwingConstants.CENTER);
        lblBaslik.setForeground(Color.WHITE);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 20));
        lblBaslik.setBounds(10, 11, 664, 30);
        contentPane.add(lblBaslik);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 52, 664, 350);
        contentPane.add(scrollPane);

        detayTable = new JTable();
        scrollPane.setViewportView(detayTable);

        JButton btnGeri = new JButton("Geri");
        btnGeri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Satislar sl = new Satislar();
                sl.setVisible(true);
            }
        });
        btnGeri.setForeground(Color.BLACK);
        btnGeri.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnGeri.setBackground(Color.WHITE);
        btnGeri.setBounds(10, 413, 173, 50);
        contentPane.add(btnGeri);

        detaylariYukle();
    }

    /**
     * Fetch Satis_Detay records for this SatisNo
     */
    private void detaylariYukle() {
        String[] columnNames = {"Detay No", "Ürün Adı", "Miktar", "Fiyat", "Toplam"};
        model = new DefaultTableModel(columnNames, 0);
        try {
            String sql = "SELECT sd.SatisDetayNo, u.Ad, sd.Miktar, sd.Fiyat " +
                        "FROM Satis_Detay sd " +
                        "JOIN Urunler u ON sd.UrunNo = u.UrunNo " +
                        "WHERE sd.SatisNo = ? " +
                        "ORDER BY sd.SatisDetayNo";
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setInt(1, satisNo);
            ResultSet rs = prst.executeQuery();

            while (rs.next()) {
                int detayNo = rs.getInt("SatisDetayNo");
                String urunAdi = rs.getString("Ad");
                int miktar = rs.getInt("Miktar");
                double fiyat = rs.getDouble("Fiyat");
                double toplam = miktar * fiyat;

                Object[] row = {detayNo, urunAdi, miktar, fiyat, toplam};
                model.addRow(row);
            }

            detayTable.setModel(model);

            rs.close();
            prst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Detaylar yüklenirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
