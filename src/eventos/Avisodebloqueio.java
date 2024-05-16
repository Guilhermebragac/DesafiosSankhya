package eventos;

import java.math.BigDecimal;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;

public class Avisodebloqueio implements EventoProgramavelJava{

	@Override
	public void afterDelete(PersistenceEvent arg0) throws Exception {
		
		
	}

	@Override
	public void afterInsert(PersistenceEvent event) throws Exception {

		
		 DynamicVO cab = (DynamicVO) event.getVo();
		 
		 BigDecimal codparc = cab.asBigDecimal("CODPARC");
		 
		 if(codparc!= null) {
		 
		 if(existe(codparc) != null) {
			 
			 if(cotacao(codparc).equals("N")) {
				 
				 String html = "<!DOCTYPE html>" +
			              "<html lang=\"en\">" +
			              "<head>" +
			              "<meta charset=\"UTF-8\">" +
			              "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
			              "</head>" +
			              "<body>" +
			              "<p>Cliente em cotação: Necessário finalizar cotação para realizar pedido." + // Descrição do erro
			              "<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAACB0lEQVR4nO2Yz0sCQRiG59LMJQiiQzf7aWVZYkHXDnkpLIKKICSCCPppJVgRUUIQBEFEaBAEQRCBdAw6hLMd/Au6BHbrGv4BCV+4ymim7uzq2kjzwAtz/F5m99mPRUgikUgkEhMASlxA8buaCBlG1QREUT1Q8gkKgXTiEKltQNUCUBzKGj4dHETVALxgGyjk63cBkgBaY0eiAxQ/5Rk+FYqfkcgAxWMFh8+UcCMRgVeEgZI3zQIKjsEjIkg0gGKf9vDsFraQ4NoEjYilVcirTc1HSQytFtLmutsJnqFBNRvjThBWq1BAm74JByvgn3SIqVUoos3d6T5WYG+mVzytgoY2D2btrEDAYxdPq6ChzaO5HlbgeL5bLK0ChzZPFmyswOlil1haBQ5tni11sgLnKx3iaBUKb5s/ElyzsgKXXivPtyFREa0W3TazcrXZzgpc+9r4Pm7UZK1ybZvp3PhbWYHbnRa+AoqJWuXfNlO522tmBe73m/gLKCZpVde2qRAIH1pYgYeARUcBUn6tGtg2S028rFo1tm2WGhysqDZz8xGug5DXqio0eTZQIlEWrfJqMzfbU5llLnk2dAu0RK3q0WZulkcGWIHk2fCjRA1qVa82cxO9aITV0X41yXMJ70LMkFb1atPUUANaVX/K/vXgSuYW/mMB4lKfPwGGh2r7PS+RSCQSVCm+AQ0vLbmgWFIkAAAAAElFTkSuQmCC\" width=\"25\">" + // URL da imagem de erro com estilo para centralizar
			              "</p>" +
			              "</div>" +
			              "</body>" +
			              "</html>";


				 
				 throw new Exception(html);
			 }
		 }
		 }
		
	
		
	}
	
	
	public String cotacao(BigDecimal codparc) throws Exception {
		String cotacao = NativeSql.getString("FINALIZADO", "AD_TESTEAPI", "CODPARC=?", codparc);
		
		if(cotacao == null) { //TRATAMENTO DE CHECKBOX == NULL
			cotacao ="N";
		}
		
		
		return cotacao;
	}
	
	public BigDecimal existe(BigDecimal codparc) throws Exception {
		BigDecimal existe = NativeSql.getBigDecimal("ID", "AD_TESTEAPI", "CODPARC=?", codparc);
		return existe;
	}
	

	@Override
	public void afterUpdate(PersistenceEvent event) throws Exception {}
	
	
	
	

	@Override
	public void beforeCommit(TransactionContext arg0) throws Exception {
		
		
	}

	@Override
	public void beforeDelete(PersistenceEvent arg0) throws Exception {
		
		
	}

	@Override
	public void beforeInsert(PersistenceEvent arg0) throws Exception {
		
		
	}

	@Override
	public void beforeUpdate(PersistenceEvent arg0) throws Exception {
		
		
	}

}
