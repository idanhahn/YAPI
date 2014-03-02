
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class stockTicker {
    private static final Log log = LogFactory.getLog(stockTicker.class);
    private static final stockTicker stockDAO = new stockTicker();
    private static HashMap<String, StockBean> stocks = new HashMap<String, StockBean>();
    private static final long TWENTY_MIN = 1200000;
    public static final int DELAY = 1000;
    
    private stockTicker() {}
 
    public static stockTicker getInstance() {
        return stockDAO;
    }
 
    public HashMap<String, StockBean> getDB(){
    	return stocks;
    }
    
    /**
     *
     * @param symbol
     * @return StockBean
     * will return null if unable to retrieve information
     */
    public StockBean getStockPrice(String symbol) {
        StockBean stock;

        long currentTime = (new Date()).getTime();
        // Check last updated and only pull stock on average every 20 minutes

        
        if (stocks.containsKey(symbol)) {
            stock = stocks.get(symbol);
            if(currentTime - stock.getLastUpdated() > TWENTY_MIN) {
                stock = refreshStockInfo(symbol, currentTime);
            }
        } else {
            stock = refreshStockInfo(symbol, currentTime);
        }
        return stock;
    }
    
    /**
    *
    * @param symbol
    * @return StockBean
    * will return null if unable to retrieve information
    */
   public LinkedList<StockBean> getStocksPrice(String symbol,int numberStocks) {
       LinkedList<StockBean> StockBeanList = new LinkedList<StockBean>();
       
       long currentTime = (new Date()).getTime();

       StockBeanList = refreshStocksInfo(symbol, currentTime,numberStocks);
            
       return StockBeanList;
   } 
    
    //This is synched so we only do one request at a time
    //If yahoo doesn't return stock info we will try to return it from the map in memory
    private synchronized StockBean refreshStockInfo(String symbol, long time) {
        try {
            
        	URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1d1t1c1ohgvm3m4&e=.csv");
            
                    
            
            URLConnection yc = yahoofin.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] yahooStockInfo = inputLine.split(",");
                StockBean stockInfo = new StockBean();
                stockInfo.setTicker(yahooStockInfo[0].replaceAll("\"", ""));
                stockInfo.setPrice(Float.valueOf(yahooStockInfo[1]));
                stockInfo.setChange(Float.valueOf(yahooStockInfo[4]));
                stockInfo.setChartUrlSmall("http://ichart.finance.yahoo.com/t?s=" + stockInfo.getTicker());
                stockInfo.setChartUrlLarge("http://chart.finance.yahoo.com/w?s=" + stockInfo.getTicker());
                stockInfo.setLastUpdated(time);
                stockInfo.setVolume(Long.valueOf(yahooStockInfo[8]));
                stockInfo.setMA50(Float.valueOf(yahooStockInfo[10]));
                stocks.put(symbol, stockInfo);
                break;
            }
            in.close();
        } catch (Exception ex) {
            log.error("Unable to get stockinfo for: " + symbol + ex);
        }
        return stocks.get(symbol);
     }
    
    //This is synched so we only do one request at a time
    //If yahoo doesn't return stock info we will try to return it from the map in memory
    private synchronized LinkedList<StockBean> refreshStocksInfo(String symbol, long time,int stockLength) {
    	LinkedList<StockBean> stockBeansList = new LinkedList<StockBean>();
    	String[] symbols = symbol.split("\\+"); 
    	int removedStocks = 0;
    	
    	try {
            int stockIndex=0;
            Thread.sleep(DELAY);
            
            URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1d1t1c1ohgvm3m4&e=.csv");
            //URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1d1t1c1ohgvm3m4&e=.csv");
            
            URLConnection yc = yahoofin.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	String[] yahooStockInfo = inputLine.split(",");
            	StockBean stockInfo = new StockBean();
            	
            	if (yahooStockInfo[0].contains("N/A"))// ticker not available
                {          	
            		removedStocks++;
                	continue;
                }
                if (yahooStockInfo[1].contains("N/A"))// price not available
                {          	
            		removedStocks++;
                	continue;
                }
                if (yahooStockInfo[4].contains("N/A"))// change not available
                {          	
            		removedStocks++;
                	continue;
                }
                if (yahooStockInfo[8].contains("N/A"))// Volume not available 
                {          	
            		removedStocks++;
                	continue;
                }
                if (yahooStockInfo[10].contains("N/A")){// MA50 not available

                } else {
                	stockInfo.setMA50(Float.valueOf(yahooStockInfo[10]));
                }      	     	
                
                

                stockInfo.setTicker(yahooStockInfo[0].replaceAll("\"", ""));
                stockInfo.setPrice(Float.valueOf(yahooStockInfo[1]));
                stockInfo.setChange(Float.valueOf(yahooStockInfo[4]));
                //stockInfo.setChartUrlSmall("http://ichart.finance.yahoo.com/t?s=" + stockInfo.getTicker());
                //stockInfo.setChartUrlLarge("http://chart.finance.yahoo.com/w?s=" + stockInfo.getTicker());
                stockInfo.setLastUpdated(time);
                stockInfo.setVolume(Long.valueOf(yahooStockInfo[8]));
                stocks.put(symbols[stockIndex], stockInfo);
                stockIndex++;
                
            }
            in.close();
        } catch (Exception ex) {
            log.error("Unable to get stockinfo for: " + symbol + ex);
        }
        for (int i = 0 ; i < stockLength-removedStocks ; i++) 
        {
        	if ( (symbols[i] != null) & (stocks.containsKey(symbols[i])) ){
        		System.out.println(symbols[i]);
        		stockBeansList.push(stocks.get(symbols[i]));
        
        	}
        }
        return stockBeansList;
     }
    

}
