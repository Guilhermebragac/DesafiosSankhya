package acaoagendada;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;


public class Registralog implements ScheduledAction{

	@Override
	public void onTime(ScheduledActionContext ctx) {
		JdbcWrapper jdbc = null;
		NativeSql query = null;
		ResultSet rset = null;

        try {

            EntityFacade entity = EntityFacadeFactory.getDWFFacade();
            jdbc = entity.getJdbcWrapper();
            jdbc.openSession();
        
        
        query = new NativeSql(jdbc);
        
        query.appendSql("SELECT COUNT(NUNOTA) AS NUNOTAS, COUNT(DISTINCT CODPARC) AS PARCEIROS, SUM(VLRNOTA) AS TOTAISNOTA, \r\n"
        		+ "\r\n"
        		+ "(SELECT COUNT(NUNOTA) FROM TGFCAB WHERE DTNEG BETWEEN '16/05/2023' AND GETDATE() AND CODVEND = 0) AS NUNOTASVENDEDOR,\r\n"
        		+ "\r\n"
        		+ "(SELECT COUNT(CONSULTA.CODPARC) \r\n"
        		+ "FROM (SELECT CODPARC FROM TGFCAB WHERE DTNEG BETWEEN '16/05/2023' AND GETDATE() GROUP BY CODPARC HAVING COUNT(NUNOTA) = 1) AS CONSULTA) \r\n"
        		+ "AS PARCEIROSCOM1NUNOTA,\r\n"
        		+ "\r\n"
        		+ "(SELECT COUNT(CONSULTA.CODPARC) \r\n"
        		+ "FROM (SELECT CODPARC FROM TGFCAB WHERE DTNEG BETWEEN '16/05/2023' AND GETDATE() GROUP BY CODPARC HAVING COUNT(NUNOTA) > 1) AS CONSULTA) \r\n"
        		+ "AS PARCEIROSCOMMAIS1NUNOTA\r\n"
        		+ "\r\n"
        		+ "\r\n"
        		+ "FROM TGFCAB WHERE DTNEG BETWEEN '16/05/2023' AND GETDATE()");
        rset = query.executeQuery();
        
        if(rset.next()) {
        	BigDecimal nunota =  rset.getBigDecimal("NUNOTAS");
        	BigDecimal parceiro = rset.getBigDecimal("PARCEIROS");
        	BigDecimal totais = rset.getBigDecimal("TOTAISNOTA");
        	BigDecimal nunotasvendedor = rset.getBigDecimal("NUNOTASVENDEDOR");
        	BigDecimal parceiros1vend = rset.getBigDecimal("PARCEIROSCOM1NUNOTA");
        	BigDecimal parceiros1vendmais = rset.getBigDecimal("PARCEIROSCOMMAIS1NUNOTA");
        	
        	DecimalFormat df = new DecimalFormat("#,##0.00");
        	String totaisFormatado = df.format(totais);
        	
        	if (!rset.wasNull()) {
        		
        		System.out.println("Os somatórios da TGFCAB no último ano até hoje são: " + nunota + " notas geradas, " +
        		        "com os respectivos " + parceiro + " parceiros. O valor total das notas é de R$ " + totaisFormatado +
        		        ". Das notas geradas, " + nunotasvendedor + " estão sem vendedor. Dos " + parceiro +
        		        " parceiros, " + parceiros1vend + " deles têm apenas uma venda, enquanto " + parceiros1vendmais +
        		        " tiveram mais de uma.");

        	}
        	
        }
		
        } catch (Exception e) {
        	e.printStackTrace();
        	StringBuffer mensagem = new StringBuffer(); 
        	mensagem.append("Erro ao mensagem:");
        	
        	  } finally {
  	
        	JdbcWrapper.closeSession(jdbc);
        	NativeSql.releaseResources(query);}

        }


}

