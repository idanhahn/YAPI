import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;


public class YAPIDB {
	public enum UpdateType{USEFILEDB,USEINTDB};
	
	private static final YAPIDB  YAPIDBO = new YAPIDB();
	
	private static HashMap<String,YAPIBean> yapiStocks = new HashMap<String,YAPIBean>();
	
	
	public static YAPIDB getInstance(){
		return YAPIDBO;
	}
	
	public Boolean addYapiBean(YAPIBean yapiBean){
		
		
		if (yapiStocks.containsKey(yapiBean.getTicker())){
			if (yapiStocks.get(yapiBean.getTicker()).getLastUpdated() <  yapiBean.getLastUpdated()){
				//replace stock
				yapiStocks.remove((yapiBean.getTicker()));
				yapiStocks.put(yapiBean.getTicker(),yapiBean);				
			}
		} else {
			yapiStocks.put(yapiBean.getTicker(),yapiBean);
		}
		
		
		return true;
	}
	
	public Boolean saveDB() throws FileNotFoundException, UnsupportedEncodingException{
		Iterator<Entry<String, YAPIBean>> it = yapiStocks.entrySet().iterator();
		boolean writeresult = true;		
		String writeString = "";
		//BufferedReader file = new BufferedReader(new FileReader("/C:/Users/idanhahn/Google Drive/WorkArea/YAPIDB.DB"));
		

		while(it.hasNext()){
			
			String yapiName = it.next().getKey(); 
			YAPIBean yapiStock = (YAPIBean) yapiStocks.get(yapiName);
			writeString += yapiStock.print()+"\n";		
		}
		
		PrintWriter writer = new PrintWriter("/C:/Users/idanhahn/Google Drive/WorkArea/YAPIDB.DB", "UTF-8");
		writer.print(writeString);
		writer.close();
		
		return writeresult;
	}
	
	// calling yahoo API with current Data base
	// Flags: USEFILEDB - read main disk DataBase and update
	//        USEINTDB  - use the Hash DataBase
	public void updateStockList(UpdateType updateType) throws IOException{
		
		if (updateType == UpdateType.USEFILEDB){
			updateInternalDB();
		}
		callYahooAndUpdate();

		
		
	}
	
	private void updateInternalDB() throws IOException{
		
		
		BufferedReader br = new BufferedReader(new FileReader(YAPICFG.getInstance().getFileDB()));
		String line;

        while ((line = br.readLine()) != null) {
        	YAPIBean nextYapiBean;
        	String[] stock = line.split(",");
        	LinkedList<Float> pastPrices = new LinkedList<Float>();
        	// TODO: change fields
        	for (int i = 6 ; i < stock.length ; i++ ){
        		pastPrices.push(Float.parseFloat(stock[i]));
        	}
        	
        	nextYapiBean = new YAPIBean(stock[0], 					//ticker
        								Float.valueOf(stock[2]),	//price
        								Long.valueOf(stock[3]),		//volume
        								Long.valueOf(stock[4]),		//lastupdated
        								pastPrices,					//pastprices
        								stock[5],					//notes
        								Integer.valueOf(stock[1]));	//grade
        						
        	YAPIDB.getInstance().addYapiBean(nextYapiBean);
        
        }
        


		
		
		
	}
	
	
	//connection to yahoo API VIA StockTicker
    public void callYahooAndUpdate(){
    	String internalDBStocks = getInternalDBStocks();
    	long currentTime = (new Date()).getTime();
    	
    	try {
			URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + internalDBStocks + "&f=sl1d1t1c1ohgvm3m4&e=.csv");
		
            URLConnection yc = yahoofin.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	YAPIBean stockInfo;
            	String[] yahooStockInfo = inputLine.split(",");
            	if (yapiStocks.containsKey(yahooStockInfo[0].replaceAll("\"", ""))){
            		stockInfo = yapiStocks.remove(yahooStockInfo[0].replaceAll("\"", ""));
            		stockInfo.pastPrices.push(stockInfo.price);            		

            	} else {
                	stockInfo = new YAPIBean(); 	
                	stockInfo.setTicker(yahooStockInfo[0].replaceAll("\"", ""));
            	}
            	
            	stockInfo.setPrice(Float.valueOf(yahooStockInfo[1]));
            	stockInfo.setLastUpdated(currentTime);
            	stockInfo.setVolume(Long.valueOf(yahooStockInfo[8]));
            	
            	yapiStocks.put(stockInfo.getTicker(), stockInfo);
            	
            }
    	
    	
    	
    	
    	} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
    	
    	
    }
    
    private String getInternalDBStocks(){
		String internalDBStocks = "";
    	Iterator<Entry<String, YAPIBean>> it = yapiStocks.entrySet().iterator();

		while(it.hasNext()){
			internalDBStocks += "\\+"+it.next().getKey();		
		}
    	
		internalDBStocks.replaceFirst("\\+", "");
		
		return internalDBStocks;
    	
    }
	
}
