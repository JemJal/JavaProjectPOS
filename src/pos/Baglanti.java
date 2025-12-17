package pos;

import java.sql.*;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Baglanti {

	public static Connection Bagla()
	{
		Connection conn = null;
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:/Users/cem/Library/Mobile Documents/com~apple~CloudDocs/Yasar/Coding/JavaProjectPOS/Data/memory.db");
			return conn;
		} catch(Exception hata)
		{
			JOptionPane.showMessageDialog(null, hata);
			return null;
		}
	}
}
