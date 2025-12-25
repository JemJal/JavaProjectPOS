package pos;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
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

public class Satislar extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable satislarTable;
    private JTextField satisNoText;
    private DefaultTableModel model;
    Connection conn = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Satislar frame = new Satislar();
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
    public Satislar() {
        conn = Baglanti.Bagla();
        setTitle("Satışlar Listesi");
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

        satislarTable = new JTable();
        scrollPane.setViewportView(satislarTable);

        JLabel lblSatisNo = new JLabel("Satış No:");
        lblSatisNo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSatisNo.setForeground(Color.WHITE);
        lblSatisNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblSatisNo.setBounds(110, 350, 130, 30);
        contentPane.add(lblSatisNo);

        satisNoText = new JTextField();
        satisNoText.setFont(new Font("Tahoma", Font.BOLD, 14));
        satisNoText.setBounds(250, 350, 150, 30);
        contentPane.add(satisNoText);
        satisNoText.setColumns(10);

        JButton btnGetir = new JButton("Getir");
        btnGetir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String satisNoStr = satisNoText.getText();
                if (satisNoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen bir Satış No giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int satisNo = Integer.parseInt(satisNoStr);

                    // Open SatisDetay screen with the given SatisNo
                    dispose();
                    SatisDetay sd = new SatisDetay(satisNo);
                    sd.setVisible(true);

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayısal Satış No giriniz.", "Geçersiz Giriş", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnGetir.setForeground(Color.BLACK);
        btnGetir.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
        btnGetir.setBackground(Color.WHITE);
        btnGetir.setBounds(401, 416, 173, 50);
        contentPane.add(btnGetir);

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
        btnGeri.setBounds(10, 416, 173, 50);
        contentPane.add(btnGeri);

        satislariYukle();
    }

    /**
     * Fetch Satislar and create a JTable
     */
    private void satislariYukle() {
        String[] columnNames = {"SatisNo", "Satici", "Musteri", "Total"};
        model = new DefaultTableModel(columnNames, 0);
        try {
            String sql = "SELECT SatisNo, Satici, Musteri, Total FROM Satislar ORDER BY SatisNo DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int satisNo = rs.getInt("SatisNo");
                String satici = rs.getString("Satici");
                String musteri = rs.getString("Musteri");
                double total = rs.getDouble("Total");
                Object[] row = {satisNo, satici, musteri != null ? musteri : "-", total};
                model.addRow(row);
            }

            satislarTable.setModel(model);

            rs.close();
            stmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Satışlar yüklenirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
