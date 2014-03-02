import java.io.IOException;


public class YAPIThread extends Thread {
	private static final long SECOND = 1000;
	private static final long MINUTE = SECOND*60;
	private static final long HOUR = MINUTE*60;
	private static final long DAY = HOUR*24;

	public void run(){
		
		while (true){
			long nextUpdateTime = getNextUpdateTime();
		
			try {
				
				YAPIDB.getInstance().updateStockList(YAPIDB.UpdateType.USEFILEDB);
				
				Thread.sleep(nextUpdateTime);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		}
		
		
	}
	
	public static void main(String args[]){
		(new YAPIThread()).start();
	}
	
	private long getNextUpdateTime(){
		return YAPICFG.getInstance().getNextUpdateTime();
	}
	
}
