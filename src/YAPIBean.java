import java.util.LinkedList;


public class YAPIBean {
	
	public enum Fields{TICKERFIELD,GRADEFIELD,PRICEFIELD,VOLUMEFIELD,LASTUPDATEFIELD,NOTESFIELD,STARTLASTPRICESFIELD};
	String ticker;
	float price;
	long volume;
	long lastUpdated;
	LinkedList<Float> pastPrices;
	
	String notes;
	int grade;
	
	public YAPIBean() {
		// TODO Auto-generated constructor stub
	}
	
	public YAPIBean(String ticker,
					float price,
					long volume,
					long lastUpdated,
					LinkedList<Float> pastPrices,
					String notes,
					int grade){
		this.ticker			= ticker		;
		this.price  		= price			;
		this.volume 		= volume		;
		this.lastUpdated	= lastUpdated	;
		this.pastPrices		= pastPrices	;
		this.notes			= notes			;
		this.grade			= grade			;
		pastPrices = new LinkedList<Float>();
	};
	
	public YAPIBean(String ticker,
			float price,
			long volume,
			long lastUpdated
			){
		this.ticker			= ticker		;
		this.price  		= price			;
		this.volume 		= volume		;
		this.lastUpdated	= lastUpdated	;
		this.notes			= ""			;
		this.grade			= 0				;
		pastPrices = new LinkedList<Float>();
	};
	
	


	public String print(){
		return ticker +"," + grade + "," + price + "," + volume + "," +  lastUpdated + "," + notes 
				+ "," + printLastPrices(pastPrices);
	}
	
	public String printLastPrices(LinkedList<Float> pastPrices){
		String lastPricesString = "";
		
		for (Float nextPrice : pastPrices){
			lastPricesString += nextPrice.toString();
		}		
		return lastPricesString;

	}
	
	public void setTicker (String ticker){
		this.ticker = ticker;
	}
	
	public void setPrice (float price){
		this.price	= price;
	}
	
	public void setVolume (long volume){
		this.volume = volume;
	}
	
	public void setLastUpdated (long lastUpdated){
		this.lastUpdated = lastUpdated; 
	}
	
	public void setNotes (String notes){
		this.notes = notes; 
	}
	
	public void setGrade (int grade){
		this.grade = grade; 
	}
	
	public void addPrice (Float price){
		this.pastPrices.push(price); 
	}
	
	public String getTicker (){
		return this.ticker;
	}
	
	public float getPrice (){
		return this.price;
	}
	
	public long getVolume (){
		return this.volume;
	}
	
	public long getLastUpdated (){
		return this.lastUpdated; 
	}
	
	public String getNotes (){
		return this.notes; 
	}
	
	public int getGrade (){
		return this.grade; 
	}
	
	public LinkedList<Float> getPastPrices (){
		return this.pastPrices; 
	}
	
	public StockBean convetToStockBean(){
		StockBean stockBean = new StockBean();
		stockBean.setTicker(this.ticker);
		stockBean.setPrice(this.price);
		stockBean.setChange(0);
		stockBean.setLastUpdated(this.lastUpdated);
		stockBean.setVolume (this.volume);
				
		return stockBean;
	}
	
}
