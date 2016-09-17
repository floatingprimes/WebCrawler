package spyder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author justinstuart
 * @version 1.1.0
 * @since 2016-09-17
 * 
 */

public class Spyder
{
	
	/**
	 * This program is to be utilized by the GUI class and implements the actual crawling
	 * mechanism coupled with a print to .txt file method if the user designates a path.
	 * 
	 */
	
	private static final String[] schemes = {"http", "https"};
	
	private Set<URL> myHitSet = new HashSet<URL>();
	private Set<URL> visitedURL_Set = new HashSet<URL>();
	private Queue<String> queueOfURLsToParse = new ArrayDeque<String>();
	private final UrlValidator myValidator = new UrlValidator(Spyder.schemes);
	private boolean writeFlag = false;
	
	private final String SPYDER_SEED;
	private final String KEY_TO_FIND;
	private final String PATH_TO_TXT_FILE;
	
    private final String REGEX_TO_PARSE_FOR; 
		
  public Spyder(Object... myArgs)
  {

	  for(int i = 0; i < myArgs.length; i++)
		  assert myArgs[i] instanceof String;
	  
	  if(myArgs[0].equals(new Integer(0)))
		  this.writeFlag = true; // We know we want to write to a txt file

		  
	  this.SPYDER_SEED = (String) myArgs[1]; // Initialize both our seed url and the key to search for
	  this.KEY_TO_FIND = (String) myArgs[2];
	  
	  if(writeFlag)
		  this.PATH_TO_TXT_FILE = (String) myArgs[3]; // If we have a path to txt file and wish to write, store the value.
	  else this.PATH_TO_TXT_FILE = null;
	  
	  if(this.myValidator.isValid(SPYDER_SEED))
		  this.queueOfURLsToParse.add(SPYDER_SEED);
	  else
	  {
		  System.out.println("Seed Value: " + SPYDER_SEED + " is invalid, exiting...");
		  System.exit(0);
	  }
	  
	  this.REGEX_TO_PARSE_FOR = ".*" + KEY_TO_FIND.toLowerCase() + ".*";
	  	  
  }

  
  public String getPath()
  {
	  return this.PATH_TO_TXT_FILE;
  }
  
  public boolean willWrite()
  {
	  return this.writeFlag;
  }
 
  
  /**
   * Method crawls from a given seed URL seeking a key with a user-specified limit of pages to crawl. It has been
   * deprecated as of 2016-9-02
   * 
   * @deprecated
   * @param seed_Url This parameter is the seed URL to begin the crawling algorithm at; first parameter passed
   * @param keyTo_Find This parameter is the key String to find embedded within the links; second parameter passed
   * @param pages_To_Crawl This parameter is the limit of pages to be crawled; third parameter passed
   * @throws MalformedURLException If the String representation of the URL cannot be resolved, the method throws
   * 							   a MalformedURLException
   * @throws InterruptedException If the current thread is interrupted while blocked (waiting, sleeping, or otherwise occupied)
   * 							  the method throws an InterruptedException
   */

  public void crawlWithPageLimit(String seed_Url, String keyTo_Find, Integer pages_To_Crawl) throws MalformedURLException, InterruptedException
  {

    int limit = (int) pages_To_Crawl;


    if(!this.myValidator.isValid(seed_Url))
    { 
      // If the URL is invalid, we will stop before we begin.
      System.out.println("Seed URL is invalid");
      System.exit(0);
    }

    System.out.println("Seed URL is valid.");
	queueOfURLsToParse.add(seed_Url); // Add the validated seed URL to the queue.


    while(!queueOfURLsToParse.isEmpty() && visitedURL_Set.size() < limit){

      // while the pending URL queue is NOT empty AND we haven't reached the limit of pages to parse.

      String currentURL = queueOfURLsToParse.remove();


      // currentURL is the URL previously at head of the queue.


      // At this point, we KNOW we have a valid, unvisited URL in "currentURL"

      Document myDoc = null; // Solely declared for scoping purposes here.

      try{
    	  /*
    	   * Jsoup.parse(URL myURL, int millisToTimeOut) is a method in JSoup library that allows
    	   * us to parse a URL into a document for operationalization.
    	   */
    	  myDoc = Jsoup.connect(currentURL.toString()).get();

      }catch (IOException ex){
    	  System.out.println("IO");
    	  /*
    	   * Promptly note the IO exception in console and continue
    	   * Note: We do NOT want to stop the program just because a certain URL can't be resolved.
    	   */
    	  continue;
      } finally{
          visitedURL_Set.add(new URL(currentURL));
      }

      // We now have an acceptable HTML document to parse via Jsoup connection
      
      int linksPerPage = 10; // Limit of links to enqueue per page (so all links don't come from one page)
      

      Elements currentURL_Links = myDoc.select("a[href]");

      // Selects all address tags hrefs and stores in ArrayList<Element> extension --> Elements
      
      if(currentURL_Links.isEmpty()) // If the Links page is empty, dequeue the next searchable link without further processing
    	  continue;


      String regexToParseFor = ".*" + keyTo_Find.toLowerCase() + ".*"; // Stores the regex we will look into titles for



      for(int i = 0; i < linksPerPage; i++){
    	  /*
    	   * Iterate over each Element object and add them to the queue if they
    	   * fit our specifications.
    	   */
    	  Element currentEle = currentURL_Links.get((int) ((Math.random() * (currentURL_Links.size()))));
    	  
    	  /*
    	   * Use a Math.random for a value between 0 and 1 (exclusive), scale it by the size of the link list then cast to int.
    	   * Ex: size = 10, we can get 0 if random() yields .01 --> 10*.01 --> .10, which is int casted to 0.
    	   */

    	  String link_Href = currentEle.attr("abs:href");

    	  /*
    	   * Node.attr(String key) method returns the value of the attribute matching our key
    	   * In this case, it's href because we want a String representation of the actual link value.
    	   */



    	  /*
    	   * To circumvent the issue of MalformedURLExceptions, utilize a boolean variable
    	   * and if a MalformedURLException occurs, simply continue, causing the algorithm to pop the
    	   * next URL and proceed.
    	   */

    	  boolean queueContainsLink = true;

    	  try{
    		  queueContainsLink = queueOfURLsToParse.contains(new URL(link_Href));
    	  }catch(MalformedURLException ex){
    		  continue;
    	  }


    	  if(!queueContainsLink && !visitedURL_Set.contains(link_Href) && (queueOfURLsToParse.size() + visitedURL_Set.size()) < limit){
    		  /*
    		   * If BOTH our Pending URL queue AND VisitedSite Set do NOT contain the link
    		   * AND we haven't reached our limit of links.
    		   */

    		  if(link_Href.contains("#") || !(this.myValidator.isValid(link_Href))){
    			  /*
    			   * If the link contains a pound sign OR it is deemed invalid by our
    			   * Http and Https URL validator, re-loop WITHOUT adding anything to the
    			   * search queue.
    			   *
    			   */

    			  continue;
    		  }

    		  if(!link_Href.contains(seed_Url)) // If our link has left the home site, restart process with next link
    			  continue;


    		  if(link_Href.matches(regexToParseFor))
    			  myHitSet.add(new URL(link_Href));
    		  /*
    		   *  If our link matches our user-given string, add it to the Set to return
    		   *  for further investigation.
    		   */

    		  queueOfURLsToParse.add(link_Href);

    		  /*
    		   * Regardless of whether or not it matches our regex, add it to searchable queue of URLs
    		   * to keep crawling for more regex hits.
    		   */

    	  } else continue;

      }

    }

  }
  
  
  /**
   * Instance method to crawl the given Spyder object's Seed URL to exhaustion. The supported protocols for seed_Urls are
   * http or https
   *
   * @throws InterruptedException Thrown if the current thread is interrupted abruptly while blocked
   * @throws IOException Thrown with invalid input
   */
  
  
  public void crawlExhaustion() throws InterruptedException, IOException
  {
	  
		System.out.println("Entered crawlExhaustion Method");


	    // currentURL will be the URL previously at head of the queue.
	  
	  while(!this.queueOfURLsToParse.isEmpty()){
		  
	    String currentURL = queueOfURLsToParse.remove();
	    	    
	    crawl(currentURL);

	  }
	    // At this point, we KNOW we have a valid, unvisited URL in "currentURL"
  }
  
  /**
   * Individual crawling method to parse an HTML Document of passed URL
   * 
   * @param currentURL String representation of current URL to be crawled
   * @throws InterruptedException will be thrown if current thread is abruptly interrupted while sleeping or waiting
   * @throws IOException will be thrown if input cannot be resolved
   *
   */
  
  public void crawl(String currentURL) throws InterruptedException, IOException
  {

		System.out.println(currentURL);

	    Document myDoc = null; // Solely declared for scoping purposes

	      try
	      {
	    	  
	    	  myDoc = Jsoup.connect(currentURL).get();
	      }
	      catch (IOException ex)
	      {
	    	  System.out.println("IO Exception: crawlExhaustion() method");
	    	  /*
	    	   * Promptly note the IO exception in console and kill the thread by returning.
	    	   */
	    	  return;
	      }
	      finally
	      {
	          visitedURL_Set.add(new URL(currentURL)); // add the URL to visitedSites so we do not visit it again later.
	      }

	      // We now have an acceptable HTML document to parse via Jsoup library	  
	      

	      Elements currentURL_Links = myDoc.select("a[href]");

	      // Selects all address tags hrefs and stores in ArrayList<Element> extension --> Elements
	      
	      if(currentURL_Links.isEmpty()) // If the Links are empty, we're done here
	    	  return;


	      for(int i = 0; i < currentURL_Links.size(); i++)
	      {
	    	  
	    	  /*
	    	   * Iterate over each Element object and add them to the queue if they
	    	   * fit our specifications.
	    	   */
	    	  
	    	  Element currentEle = currentURL_Links.get(i); // get the i_th element
	    	  

	    	  String link_Href = currentEle.attr("abs:href");

	    	  /*
	    	   * Node.attr(String key) method returns the value of the attribute matching our key
	    	   * In this case, it's a[href] because we want a String representation of link values
	    	   */
	    	  
	    	  
	    	  if(queueOfURLsToParse.contains(link_Href))
	    		  continue;

	    	  
	    	  if(!visitedURL_Set.contains(link_Href))
	    	  {
	    		  
	    		  /*
	    		   * If BOTH our Pending URL queue AND VisitedSite Set do NOT contain the current link...
	    		   */

	    		  if(link_Href.contains("#") || !(myValidator.isValid(link_Href))){
	    			  
	    			  /*
	    			   * If the link contains a pound sign OR it is deemed invalid by the
	    			   * object's URL validator, re-loop WITHOUT adding anything to the
	    			   * search queue.
	    			   *
	    			   */

	    			  continue;
	    		  }

	    		  if(!link_Href.contains(this.SPYDER_SEED)) // If our link has no affiliation with our home seed, restart process with next link
	    			  continue;


	    		  if(link_Href.matches(REGEX_TO_PARSE_FOR))
	    			  myHitSet.add(new URL(link_Href));
	    		  /*
	    		   *  If our link matches our user-given string, add it to the Set to return
	    		   *  for further investigation.
	    		   */

	    		  queueOfURLsToParse.add(link_Href);

	    		  /*
	    		   * Regardless of whether or not it matches our regex, add it to searchable queue of URLs
	    		   * to keep crawling for more regex hits.
	    		   */

	    	  }
	    	  
	    	  else continue;
	    	  
	      }

}

  
  /**
   * Method to write our list of URLs that contain the user's key String into a user-given File as determined
   * through an absolute directory path passed through the GUI.
   * 
   * @throws IOException Thrown through the use of File object manipulation if needed.
   */
  
  public void writeHitList() throws IOException{
	  
	  
	  File hit_txt_File = new File(this.getPath()); // Does not actually create the new file, just corresponding object
	  
	  hit_txt_File.getParentFile().mkdirs();
	  
	  
	  try
	  {
		  hit_txt_File.createNewFile(); // Creates the new File ONLY IF that file doesn't already exist.
	  }
	  catch (IOException ex)
	  {
		  ex.printStackTrace();
	  }  
	  
	  
	  FileWriter myWriter = new FileWriter(hit_txt_File, true); // Character writer object to write characters into a txt file
	  myWriter.write("\n");
	  
	  Iterator<URL> iterator = myHitSet.iterator();
	  
	  while(iterator.hasNext())
	  { 
		  // While the list has another value, keep iterating
		  try
		  {
			  myWriter.write(iterator.next() + "\n"); // write the hit link into the file.
		  }
		  catch(IOException ex)
		  {
			  ex.printStackTrace();
		  }
	  }
	  	   
	  myWriter.close(); // close resource

	  
  }
  
  /**
   * Overridden toString method
   * 
   * @return String representation of this particular Spyder object
   */
  
 @Override
 public String toString()
 {
	 String description = "\nSeed URL: " + this.SPYDER_SEED + "\nParsed Key: " + this.KEY_TO_FIND;
	 
	 return description;
 }
 

} // end class 
