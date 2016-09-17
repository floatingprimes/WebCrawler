package spyder;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * 
 * @author justinstuart
 * @version 1.1.0
 * @since 2016-09-17
 * 
 */

public class SpyderTask implements Runnable
{
	/**
	 * This program is written as a Runnable task to enable multithreading if desirable.
	 */
	
	private Spyder mySpyder;
	private static final int DELAY = 50;
	
	/**
	 * SpyderTask constructor
	 * 
	 * @param spidey Spyder object corresponding to this particular task
	 */
	
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

