import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;

public class Crawler{

  private Crawler(){}; // No need to instantiate objects here, so constructor stays private.


  public static HashSet<URL> crawl(String seed_Url, int pages_To_Crawl, String keyToFind) throws MalformedURLException{

    // We will return a list of URLs corresponding to "hits" to investigate.


	// Initialize data structures and variables to use

    ArrayDeque<URL> queueOf_SearchableURLs = new ArrayDeque<>(); // Initialize queue of URLs to search
    HashSet<String> visitedSites = new HashSet<>(); // Set to track our progress

    HashSet<URL> sitesWith_Key = new HashSet<>(); // Will track sites with a given key found

    int limit = pages_To_Crawl;

    String[] schemes = {"http", "https"}; // We will be looking at only http and https protocols as our seed

    UrlValidator urlValidatorObj = new UrlValidator(schemes);

    /*
     * Validator made to be predisposed to protocols: HTTP and HTTPS
     */

    if(!urlValidatorObj.isValid(seed_Url)){ // If the URL is invalid, we will stop before we begin.
      System.out.println("Seed URL is invalid");
      return null;
    }

    // If we are at this point, we know the seed_Url is valid.
    try{
    	System.out.println("Seed URL is valid.");
    	queueOf_SearchableURLs.add(new URL(seed_Url)); // Add the validated seed URL to the queue.
    }catch (MalformedURLException ex){

    	ex.printStackTrace();
    }



    while(!queueOf_SearchableURLs.isEmpty() && visitedSites.size() < limit){

      // while the pending URL queue is NOT empty AND we haven't reached the limit of pages to parse.

      URL currentURL = queueOf_SearchableURLs.remove();


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
          visitedSites.add(currentURL.toString());
      }

      // We now have an acceptable HTML document to parse via Jsoup connection

      int linksPerPage = 10; // Limit of links to enqueue per page (so all links don't come from one page)


      Elements currentURL_Links = myDoc.select("a[href]");

      // Selects all address tags hrefs and stores in ArrayList<Element> extension --> Elements

      if(currentURL_Links.isEmpty()) // If the Links page is empty, dequeue the next searchable link without further processing
    	  continue;


      String regexToParseFor = ".*" + keyToFind.toLowerCase() + ".*"; // Stores the regex we will look into titles for



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
    		  queueContainsLink = queueOf_SearchableURLs.contains(new URL(link_Href));
    	  }catch(MalformedURLException ex){
    		  continue;
    	  }


    	  if(!queueContainsLink && !visitedSites.contains(link_Href) && (queueOf_SearchableURLs.size() + visitedSites.size()) < limit){
    		  /*
    		   * If BOTH our Pending URL queue AND VisitedSite Set do NOT contain the link
    		   * AND we haven't reached our limit of links.
    		   */

    		  if(link_Href.contains("#") || !(urlValidatorObj.isValid(link_Href))){
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
    			  sitesWith_Key.add(new URL(link_Href));
    		  /*
    		   *  If our link matches our user-given string, add it to the Set to return
    		   *  for further investigation.
    		   */

    		  queueOf_SearchableURLs.add(new URL(link_Href));

    		  /*
    		   * Regardless of whether or not it matches our regex, add it to searchable queue of URLs
    		   * to keep crawling for more regex hits.
    		   */

    	  } else continue;


      }


      System.out.println("Just visited: " + currentURL.toString() +  "\nSize of Path: " + visitedSites.size());
      // Test code.

    }

    return sitesWith_Key; // return our URL path.

  }

  public static HashSet<URL> crawl(String seed_Url, String keyToFind) throws MalformedURLException{

	    // We will return a list of URLs corresponding to "hits" to investigate.


		// Initialize data structures and variables to use

	    ArrayDeque<URL> queueOf_SearchableURLs = new ArrayDeque<>(); // Initialize queue of URLs to search
	    HashSet<String> visitedSites = new HashSet<>(); // Set to track our progress

	    HashSet<URL> sitesWith_Key = new HashSet<>(); // Will track sites with a given key found

	    int pageCount = 0;


	    String[] schemes = {"http", "https"}; // We will be looking at only http and https protocols as our seed

	    UrlValidator urlValidatorObj = new UrlValidator(schemes);

	    /*
	     * Validator made to be predisposed to protocols: HTTP and HTTPS
	     */

	    if(!urlValidatorObj.isValid(seed_Url)){ // If the URL is invalid, we will stop before we begin.
	      System.out.println("Seed URL is invalid");
	      pageCount++;
	      return null;
	    }

	    // If we are at this point, we know the seed_Url is valid.
	    try{
	    	System.out.println("Seed URL is valid.");
	    	queueOf_SearchableURLs.add(new URL(seed_Url)); // Add the validated seed URL to the queue.
	    }catch (MalformedURLException ex){

	    	ex.printStackTrace();
	    }



	    while(!queueOf_SearchableURLs.isEmpty()){

	      // while the pending URL queue is NOT empty AND we haven't reached the limit of pages to parse.

	      URL currentURL = queueOf_SearchableURLs.remove();


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
	          visitedSites.add(currentURL.toString()); // add the URL to visitedSites so we do not visit it again later.
	          pageCount++; // Track pages
	      }

	      // We now have an acceptable HTML document to parse via Jsoup connection

	      int linksPerPage = 10; // Limit of links to enqueue per page (so all links don't come from one page)


	      Elements currentURL_Links = myDoc.select("a[href]");

	      // Selects all address tags hrefs and stores in ArrayList<Element> extension --> Elements

	      if(currentURL_Links.isEmpty()) // If the Links page is empty, dequeue the next searchable link without further processing
	    	  continue;


	      String regexToParseFor = ".*" + keyToFind.toLowerCase() + ".*"; // Stores the regex we will look into titles for



	      for(int i = 0; i < currentURL_Links.size(); i++){
	    	  /*
	    	   * Iterate over each Element object and add them to the queue if they
	    	   * fit our specifications.
	    	   */
	    	  Element currentEle = currentURL_Links.get(i);

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
	    		  queueContainsLink = queueOf_SearchableURLs.contains(new URL(link_Href));
	    	  }catch(MalformedURLException ex){
	    		  continue;
	    	  }


	    	  if(!queueContainsLink && !visitedSites.contains(link_Href)){
	    		  /*
	    		   * If BOTH our Pending URL queue AND VisitedSite Set do NOT contain the link
	    		   * AND we haven't reached our limit of links.
	    		   */

	    		  if(link_Href.contains("#") || !(urlValidatorObj.isValid(link_Href))){
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
	    			  sitesWith_Key.add(new URL(link_Href));
	    		  /*
	    		   *  If our link matches our user-given string, add it to the Set to return
	    		   *  for further investigation.
	    		   */

	    		  queueOf_SearchableURLs.add(new URL(link_Href));

	    		  /*
	    		   * Regardless of whether or not it matches our regex, add it to searchable queue of URLs
	    		   * to keep crawling for more regex hits.
	    		   */

	    	  } else continue;


	      }


	      System.out.println("Just visited: " + currentURL.toString() +  "\nSize of Path: " + visitedSites.size());
	      // Test code.

	    }

	    System.out.println("Total of:  " + pageCount + " pages." + "\nHome URL:  " + seed_Url);

	    return sitesWith_Key; // return our URL path.

	  }
  public static void main(String[] args) throws MalformedURLException{


	  HashSet<URL> pathTraveled = crawl("http://www.cnn.com/", "Trump");


	  Iterator<URL> iterator = pathTraveled.iterator();
	  /*
	   * Iterator will serve to print out each URL in string format to show us links of interest.
	   */

	  while(iterator.hasNext()){
		  System.out.println(iterator.next());
	  }


  } // main

} // class
