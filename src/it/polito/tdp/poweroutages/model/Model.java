package it.polito.tdp.poweroutages.model;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.poweroutages.db.NercDAO;
import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model 
{

	PowerOutageDAO podao;
	List<PowerOutages> listPO; //lista contentente tutti i PO per il NERC selezionato
	
	//recursion
	int max_affected_people;
	List<PowerOutages> best_solution;
	
	public Model() 
	{
		podao = new PowerOutageDAO();
	}
	
	// utility combobox
	public List<Nerc> loadNerc() 
	{
		NercDAO dao = new NercDAO();
		List<Nerc>lista = dao.getAllNerc();
		return lista;
	}

	//
	// 	Esercizio ricorsione
	//
	public String worstCaseAnalysis(int nerc_id, int maxYears, int maxHours) 
	{
		listPO = podao.getAllPoByNerc(nerc_id);
		max_affected_people = 0;
		
		List<PowerOutages>aus_start = new ArrayList<PowerOutages>();
		//start recursion
		calcola(aus_start, maxYears, maxHours);
		
		if (best_solution != null)
		{
			String soluzione = diagnostic(maxHours);
			return soluzione;
		}
		else
			return null;
		
	}
	
	
	private String diagnostic(int maxHours) 
	{
		int currentHours = 0;
		Set<Integer> currentYears = new HashSet<Integer>();
		String aus = "";
		int i = 0;
		for (PowerOutages po : best_solution)
		{
			currentHours += calcolaDurataOre(po);
			currentYears.add(po.getDate_event_began().getYear());
			aus += best_solution.get(i).toString() + "";
			i++;
		}
		
		//soluzione
		return "Persone affette: " + max_affected_people +
				"\nTotale ore di guasto: " + maxHours +
				"\n" + aus;
		
	}

	//recursion
	private void calcola(List<PowerOutages> partial, int maxYears, int maxHours) 
	{
		//non c'è una vera e propria terminazione, devo scorrere tutte le soluzioni valide
		int current_affected_people = sumAffectedPeople(partial);
		//soluzione migliore
		if (current_affected_people > max_affected_people)
		{
			max_affected_people = current_affected_people;
			best_solution = new ArrayList<PowerOutages>(partial);
		}
		
		for (PowerOutages po : listPO)
		{
			//aggiunta valida
			if (!partial.contains(po))
			{
				partial.add(po);
				//continuo ad esplorare soluzione solo se finora è valida
				if (aggiuntaValida(partial, po, maxYears, maxHours))
					calcola(partial, maxYears, maxHours); //backtrack
				partial.remove(po);
			}
		}
		
	}

	// Ho raggiunto il limite di ore? Ho raggiunto il limite di anni?
	private boolean aggiuntaValida(List<PowerOutages>partial, PowerOutages current, int maxYears, int maxHours) 
	{
		int currentHours = 0;
		Set<Integer> currentYears = new HashSet<Integer>();
		for (PowerOutages po : partial)
		{
			currentHours += calcolaDurataOre(po);
			currentYears.add(po.getDate_event_began().getYear());
		}
		//ho già aggiunto po...
		if (currentHours > maxHours || currentYears.size() > maxYears)
			return false;
		else
			return true;
	}

	// quante persone sono affette dai guasti in questa soluzione
	private int sumAffectedPeople(List<PowerOutages> solution)
	{
		int current_affected_people = 0;
		for (PowerOutages po : solution)
		{
			current_affected_people += po.getCustomers_affected();
		}
		return current_affected_people;
	}

	// restituisce durata del guasto in ore date due LocalDateTime
	private long calcolaDurataOre(PowerOutages po)
	{
		//safe way
		long durata = po.getDate_event_began().until(po.getDate_event_finished(), ChronoUnit.HOURS);
		return durata;
	}

}
