package botaodeacao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.wrapper.JapeFactory;

public class Apimoeda implements AcaoRotinaJava{
	
	 public void doAction(ContextoAcao ctx) throws Exception {
		 
		 
	    //CHAVE DE ACESSO	
	    	String key= "faa628be7e69befd95f77bb1";
	    	
			Registro linhas[] = ctx.getLinhas();
			
	        String moedaorigem = "";
	        BigDecimal valormoedaorig = BigDecimal.ZERO;
	        String moedadesti = "";
	        BigDecimal id = BigDecimal.ZERO;
	       
	       //CAMPOS DA TELA
	        for (Registro linha : linhas) {
	            moedaorigem = (String) linha.getCampo("MOEDAESCOLHIDA");
	            moedadesti = (String) linha.getCampo("MOEDADESTINO");
	            valormoedaorig = (BigDecimal) linha.getCampo("VALOR");
	            id = (BigDecimal) linha.getCampo("ID");

	            if (moedadesti != null && moedaorigem != null && valormoedaorig != null) {
	            	
	            	//URL DE REQUISICAO
	                String apiUrl = "https://v6.exchangerate-api.com/v6/" + key + "/latest/" + moedaorigem;

	                try {
	                    URL url = new URL(apiUrl);
	                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                    conn.setRequestMethod("GET");
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                    StringBuilder response = new StringBuilder();
	                    String line;
	                    while ((line = reader.readLine()) != null) {
	                        response.append(line);
	                    }
	                    reader.close();

	                    JSONObject jsonResponse = new JSONObject(response.toString());
	                    JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates"); //CONVERSAO DA MOEDA
	                    double rateForDestination = conversionRates.getDouble(moedadesti);
	                    BigDecimal rateBigDecimal = BigDecimal.valueOf(rateForDestination);

	                    if(rateBigDecimal!= null) {
	                    	update(rateBigDecimal, id, valormoedaorig);
	                    }
	                    
	                    
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

		private void update(BigDecimal rateBigDecimal, BigDecimal id, BigDecimal valormoedaorig) {
			SessionHandle hnd = null;
			try {
				hnd = JapeSession.open();
				JapeFactory.dao("AD_TESTEAPI").prepareToUpdateByPK(id)
				.set("COTACAO", rateBigDecimal)
				.set("VALORMOEDAFINAL", rateBigDecimal.multiply(valormoedaorig))

				.update();
				
			
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
			} finally {
				JapeSession.close(hnd);
			}
			
		}
	}