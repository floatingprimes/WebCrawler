package spyder;

import java.io.IOException;
import java.net.MalformedURLException;

public class SpyderTask implements Runnable
{
	private Spyder mySpyder;
	private static final int DELAY = 50;
	
	public SpyderTask(Spyder spidey)
	{
		mySpyder = spidey;
	}
	
	@Override
	public void run()
	{
		
		try
		{
			System.out.println("Entered Task run() method...");
			mySpyder.crawlExhaustion();
			Thread.sleep(DELAY);
			
		}
		catch (MalformedURLException | InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(mySpyder.willWrite())
		{
			System.out.println("Finished with exhaustive crawl and writing of: " + mySpyder);
		}
		else
		{
			System.out.println("Finished with exhaustive crawl of: " + mySpyder);
		}
		
	}

}
