import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;



public class YAPI {
	public enum GetMethod{SINGLESTOCK,ALLSTOCKS};
	public static final int STOCKITERATION = 200;
	
	public static LinkedList<String> stockList = new LinkedList<String>();
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		
		LinkedList<StockBean> filteredStocks = new LinkedList<StockBean>();
		
		String line;
		//String inputFile = "/C:/Users/idanhahn/Google Drive/WorkArea/companyfilteredlist.csv";
		//String inputFile = "/C:/Users/idanhahn/Google Drive/WorkArea/companylist.csv";
		String inputFile = "/C:/Users/User/Google Drive/WorkArea/companylist.csv";
		
		
		File stockFile = new File(inputFile);
		BufferedReader reader = null;
		int i = 0;
		
		try {
			reader = new BufferedReader(new FileReader(stockFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			while ( (line = reader.readLine()) != null)
			{
				String[] lineParse = line.toString().split(",");
				System.out.println(lineParse[0]);		
				stockList.push(lineParse[0]);
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Number of Stocks in the system" + i);
		stockList.pop();
		filteredStocks = FilterAllStocks(stockList,STOCKITERATION,1000000);
		System.out.println("Final Size "+ filteredStocks.size());
		printAllStocks(filteredStocks);
		System.out.println("Final Size "+ filteredStocks.size());
		
		//PrintWriter writer = new PrintWriter("/C:/Users/idanhahn/Google Drive/WorkArea/companyfilteredlist.csv", "UTF-8");
		PrintWriter writer = new PrintWriter("/C:/Users/Users/Google Drive/WorkArea/companyfilteredlist.csv", "UTF-8");
		writer.println(printAllStocks(filteredStocks));
		writer.close();
		
		convertStockBeanDBToYAPIBeanDB(filteredStocks);
		YAPIDB.getInstance().saveDB();
		YAPIDB.getInstance().updateStockList(YAPIDB.UpdateType.USEFILEDB);
		
		
		
	}
	
	
	
	// ------------------------------------------- //
	// - Filter Stocks, currently only by volume - //
	// ------------------------------------------- //
	
	public static LinkedList<StockBean> FilterAllStocks(LinkedList<String> allStocks,int iteration, int valumeFilter){
		LinkedList<StockBean> modifiedListAll = new LinkedList<StockBean>();
		LinkedList<StockBean> iterationList = new LinkedList<StockBean>();
		
		// main loop for all stocks given
		while (allStocks.size() > 0){
			LinkedList<String> nextIterationStocks = new LinkedList<String>();
			int nextIterationSize = STOCKITERATION;
			
			if (allStocks.size() < STOCKITERATION)
				nextIterationSize = allStocks.size();
			
			for(int i = 0 ; (i < iteration) & allStocks.size() > 0 ; i++){
				nextIterationStocks.push(allStocks.pop());				
			}
			iterationList = getFilteredStocks(nextIterationStocks,valumeFilter,nextIterationSize);
			modifiedListAll.addAll(iterationList);			
		}
		
		
		return modifiedListAll;
	}
	
	
	// returns a modified list by filter given
	public static LinkedList<StockBean> getFilteredStocks(LinkedList<String> stockList, int valumeFilter,int nextIterationSize){
		
		String yapiString = getNextStockString(stockList);
		LinkedList<StockBean> stockBeanList = new LinkedList<StockBean>();
		LinkedList<StockBean> modifiedList = new LinkedList<StockBean>();
		
		// Calling stockTicker
		stockBeanList = stockTicker.getInstance().getStocksPrice(yapiString,nextIterationSize);
		
		while (stockBeanList.size() > 0){
			
			if (stockBeanList.element().getVolume() > valumeFilter ){ 
				modifiedList.push(stockBeanList.pop());
			} else {
				stockBeanList.pop();
			}
		}
		
		return modifiedList;
		
	}
	
	// get modified string for the next yahoo query.
	public static String getNextStockString(LinkedList<String> stockList ){
		String nextStockString = null;
		
		while (stockList.size() > 0){
			nextStockString += "+"+stockList.pop();
		}
		System.out.println("tick");
		nextStockString = nextStockString.replaceAll("\"", "");
		nextStockString = nextStockString.replaceAll(" ", "");
		nextStockString = nextStockString.replaceFirst("null\\+",  "");
		if (nextStockString.contains("N/A")){
			System.out.println("error");
		}
		return nextStockString;
	}
	
	public static String printAllStocks(LinkedList<StockBean> stockList){
		String stockString = "";
		for (int i = 0 ; i < stockList.size(); i++){
			stockString += stockList.get(i).print()+"\n";
		}
		System.out.println(stockString);
		return stockString;
	}
	
	public String printStock(StockBean stock){
		System.out.println(stock.print());
		return stock.print();
	}
	
	public YAPIDB convertStockBeanDBToYAPIBeanDB(HashMap<String,StockBean> stockbeanDB){
		YAPIDB yapiDB = new YAPIDB();
		Iterator<Entry<String, StockBean>> it = stockbeanDB.entrySet().iterator();

		while(it.hasNext()){
			
			yapiDB.addYapiBean(it.next().getValue().convertToYAPIBean());
		
		}
		
		
		return yapiDB;
	}
	
	public static void convertStockBeanDBToYAPIBeanDB(LinkedList<StockBean> stockBeanList){
		
		
		
		for (StockBean nextBean : stockBeanList){
			YAPIDB.getInstance().addYapiBean(nextBean.convertToYAPIBean());
		}
		
		
	}
	

}
