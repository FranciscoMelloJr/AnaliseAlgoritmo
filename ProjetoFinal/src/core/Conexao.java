package core;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
	
	public static Connection getConexao() {
		Connection conn =  null;
		
		try {
			File file = new File("ProjetoFinal.db");
			if (file.exists()) {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection("jdbc:sqlite:ProjetoFinal.db");
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
		return conn;
	}
}
