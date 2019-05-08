package it.polito.tdp.poweroutages;


import java.util.List;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PowerOutagesController {

    @FXML
    private ComboBox<Nerc> cmbNerc;

    @FXML
    private TextField txtYears;

    @FXML
    private TextField txtHours;

    @FXML
    private Button btnAnalysis;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void doAnalysis(ActionEvent event)
    {
    	int maxYears = -1; int maxHours = -1;
    	try
    	{
    		maxYears = Integer.parseInt(txtYears.getText());
    		maxHours = Integer.parseInt(txtHours.getText());
    	}
    	catch (NumberFormatException e)
    	{
    		txtResult.setText("ERRORE: Devi inserire dei valori di anni ed ore validi.\n");
    	}
    	if (maxYears >= 1 && maxHours >= 1)
    	{
    		String res;
    		if (cmbNerc.getSelectionModel().getSelectedItem() != null)
    		{
    			int nerc_id = cmbNerc.getSelectionModel().getSelectedItem().getId();
    			res = model.worstCaseAnalysis(nerc_id, maxYears, maxHours);
    			if (res != null)
    				txtResult.setText(res);
    			else
    				txtResult.setText("Nessun dato disponibile per il NERC selezionato.\n");
    		}	
    		else
    			txtResult.setText("ERRORE: Devi selezionare un NERC valido.\n");
    	}
    		
    	else
    		txtResult.setText("ERRORE: Devi inserire dei valori di anni ed ore positivi.\n");
    }

	public void setModel(Model model) 
	{
		this.model = model;
	}
	
	public void inizializzaCmb()
	{
		List<Nerc> nerc = model.loadNerc();
		cmbNerc.getItems().addAll(nerc);
	}

}
