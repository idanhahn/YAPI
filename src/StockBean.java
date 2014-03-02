import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class StockBean {
    String ticker;
    float price;
    float change;
    String chartUrlSmall;
    String chartUrlLarge;
    long lastUpdated;
    long volume;
    float ma50;
    
    public String print(){
    	return ticker + "," + price + "," + change + "," + lastUpdated + "," + volume;
    }
    
    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public float getChange() {
        return change;
    }
    public void setChange(float change) {
        this.change = change;
    }
    public String getChartUrlSmall() {
        return chartUrlSmall;
    }
    public void setChartUrlSmall(String chartUrlSmall) {
        this.chartUrlSmall = chartUrlSmall;
    }
    public String getChartUrlLarge() {
        return chartUrlLarge;
    }
    public void setChartUrlLarge(String chartUrlLarge) {
        this.chartUrlLarge = chartUrlLarge;
    }
    public long getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public float getMA50() {
        return ma50;
    }
    public void setMA50(float ma50) {
        this.ma50 = ma50;
    }
    
    public void setVolume(long volume) {
    	this.volume = volume;
    }
    
    public long getVolume(){
    	return volume;
    }
    
    public String getData(){
    	String[] stockData = new String[5];
    	DateFormat formatter = new SimpleDateFormat("(HH mm ss) dd/MM/yyyy");
    	Calendar calendar = Calendar.getInstance();
        	   	
    	stockData[0] = String.valueOf(price);
    	stockData[1] = String.valueOf(change);
    	stockData[2] = String.valueOf(volume);
    	stockData[3] = chartUrlLarge;
    	stockData[4] = String.valueOf(lastUpdated);
    	//stockData[5] = String.valueOf(ma50);
    	calendar.setTimeInMillis(lastUpdated);
    	
    	return new String (stockData[0] + " "+
        		stockData[1] + " "+
        		stockData[2] + " "+
        		//stockData[3] + " "+
        		formatter.format(calendar.getTime()) + " "
        		);
    }
    
    public YAPIBean convertToYAPIBean(){
    	YAPIBean yapiBean = new YAPIBean(this.ticker,
    									this.price,
    									this.volume,
    									this.lastUpdated
    	);
    	return yapiBean;
    	
    }
    
}
