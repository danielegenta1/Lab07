package it.polito.tdp.poweroutages.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


import it.polito.tdp.poweroutages.model.Nerc;

public class NercDAO {

	public List<Nerc> getAllNerc()
	{
		final String sql = "SELECT * FROM Nerc";

		List<Nerc> nerc = new LinkedList<Nerc>();

		try 
		{
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) 
			{
				int id = rs.getInt("id");
				String value = rs.getString("value");
				Nerc n = new Nerc(id, value);
				nerc.add(n);
			}
			conn.close();
			return nerc;

		} 
		catch (SQLException e) 
		{
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}

}
