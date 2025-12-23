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

public class KullaniciSil extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable kullanicilarTable;
    private JTextField hesapNoText;
    private DefaultTableModel model;
    private String currentKullaniciAdi;
    Connection conn = null;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    KullaniciSil frame = new KullaniciSil("");
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
    public KullaniciSil(String currentKullaniciAdi) {
        this.currentKullaniciAdi = currentKullaniciAdi;
        conn = Baglanti.Bagla();
        setTitle("Kullanici Silme Paneli");
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
        
        kullanicilarTable = new JTable();
        scrollPane.setViewportView(kullanicilarTable);
        
        JLabel lblHesapNo = new JLabel("Silinecek Hesap No:");
        lblHesapNo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblHesapNo.setForeground(Color.WHITE);
        lblHesapNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblHesapNo.setBounds(110, 350, 130, 30);
        contentPane.add(lblHesapNo);
        
        hesapNoText = new JTextField();
        hesapNoText.setFont(new Font("Tahoma", Font.BOLD, 14));
        hesapNoText.setBounds(250, 350, 150, 30);
        contentPane.add(hesapNoText);
        hesapNoText.setColumns(10);
        
        JButton btnSil = new JButton("Sil");
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String hesapNoStr = hesapNoText.getText();
                if (hesapNoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen silmek için bir Hesap No giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    int hesapNoToDelete = Integer.parseInt(hesapNoStr);
                    
                    String checkSql = "SELECT KullaniciAdi, Gorev FROM Kullanici WHERE HesapNo = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setInt(1, hesapNoToDelete);
                    ResultSet rs = checkStmt.executeQuery();
                    
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Bu Hesap No ile eşleşen bir kullanıcı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                        checkStmt.close();
                        return;
                    }
                    
                    String kullaniciAdi = rs.getString("KullaniciAdi");
                    String gorev = rs.getString("Gorev");
                    checkStmt.close();
                    
                    if ("super-admin".equals(gorev)) {
                        JOptionPane.showMessageDialog(null, "Super-admin kullanıcıları silemezsiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (currentKullaniciAdi.equals(kullaniciAdi)) {
                        JOptionPane.showMessageDialog(null, "Kendi hesabınızı silemezsiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int confirmation = JOptionPane.showConfirmDialog(null, 
                            "Hesap No " + hesapNoToDelete + " olan kullanıcıyı (" + kullaniciAdi + ") silmek istediğinize emin misiniz?", 
                            "Silme Onayı", 
                            JOptionPane.YES_NO_OPTION);
                    
                    if (confirmation == JOptionPane.YES_OPTION) {
                        String sql = "DELETE FROM Kullanici WHERE HesapNo = ?";
                        PreparedStatement prst = conn.prepareStatement(sql);
                        prst.setInt(1, hesapNoToDelete);
                        int affectedRows = prst.executeUpdate();
                        
                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(null, "Kullanıcı başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                            kullanicilariyukle();
                            hesapNoText.setText("");
                        }
                        prst.close();
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayısal Hesap No giriniz.", "Geçersiz Giriş", JOptionPane.ERROR_MESSAGE);
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
        
        kullanicilariyukle();
    }
    
    /**
     * Fetch Kullanici and create a JTable
     */
    private void kullanicilariyukle() {
        String[] columnNames = {"HesapNo", "KullaniciAdi", "AdiSoyadi", "Gorev"};
        model = new DefaultTableModel(columnNames, 0);
        try {
            String sql = "SELECT HesapNo, KullaniciAdi, AdiSoyadi, Gorev FROM Kullanici ORDER BY HesapNo";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int hesapNo = rs.getInt("HesapNo");
                String kullaniciAdi = rs.getString("KullaniciAdi");
                String adiSoyadi = rs.getString("AdiSoyadi");
                String gorev = rs.getString("Gorev");
                Object[] row = {hesapNo, kullaniciAdi, adiSoyadi, gorev};
                model.addRow(row);
            }
            
            kullanicilarTable.setModel(model);
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Kullanıcılar yüklenirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}