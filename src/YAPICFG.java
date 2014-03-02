


public class YAPICFG {
	private static final long SECOND = 1000;
	private static final long MINUTE = SECOND*60;
	private static final long HOUR = MINUTE*60;
	private static final long DAY = HOUR*24;
	
	
	private static final YAPICFG  YAPICFGO = new YAPICFG();
	private static long nextUpdateTime = 6*HOUR;
	private static String fileDB = "/C:/Users/idanhahn/Google Drive/WorkArea/YAPIDB.DB";
	private static String fileAllStocks;
	private static String fileFilteredStocks;
	
	
	
	public static YAPICFG getInstance(){
		return YAPICFGO;
	}
	
	// Update Time:
	// ------------
	public long getNextUpdateTime(){
		return nextUpdateTime;
	}
	
	public void setNextUpdateTime(long nextUpdateTime){
		YAPICFG.nextUpdateTime = nextUpdateTime;
	}
	
	// file DataBase:
	// --------------
	public String getFileDB(){
		return YAPICFG.fileDB;
	}
	
	public void setFileDB(String fileDB){
		YAPICFG.fileDB = fileDB;
	}
	
	// file All Stocks:
	// ----------------
	public String getFileAllStocks(){
		return fileAllStocks;
	}
	
	public void setFileAllStocks(String fileAllStocks){
		YAPICFG.fileAllStocks = fileAllStocks;
	}
	
	// file Filtered Stocks
	public String getFileFilteredStocks(){
		return fileFilteredStocks;
	}
	
	public void setFileFilteredStocks(String fileFilteredStocks){
		YAPICFG.fileFilteredStocks = fileFilteredStocks;
	}
	
}
