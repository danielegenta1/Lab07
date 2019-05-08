package it.polito.tdp.poweroutages.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;

public class PowerOutageDAO 
{
	//spostato in NercDAo
	public List<Nerc> getNercList() 
	{

		/*String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;*/
		return null;
	}

	public List<PowerOutages> getAllPoByNerc(int nerc_id) 
	{
		final String sql = "SELECT * FROM PowerOutages where nerc_id = ?";

		List<PowerOutages> po = new LinkedList<PowerOutages>();
		try 
		{
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc_id);
			ResultSet rs = st.executeQuery();
	
				while (rs.next()) 
				{
					int id = rs.getInt("id");
					int event_type_id = rs.getInt("event_type_id");
					int tag_id = rs.getInt("tag_id");
					int area_id = rs.getInt("area_id");
					int nerc_id_1 = rs.getInt("nerc_id");
					int responsible_id = rs.getInt("responsible_id");
					long customers_affected = rs.getLong("customers_affected");
					LocalDateTime date_event_began = rs.getTimestamp("date_event_began").toLocalDateTime();
					LocalDateTime date_event_finished = rs.getTimestamp("date_event_finished").toLocalDateTime();
					int demand_loss = rs.getInt("demand_loss");
					PowerOutages aus = new PowerOutages(id, event_type_id, tag_id, area_id, nerc_id_1, 
										responsible_id, customers_affected, date_event_began, 
										date_event_finished,demand_loss);
					po.add(aus);
				
			}
			conn.close();
			return po;

		} 
		catch (SQLException e) 
		{
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

}
