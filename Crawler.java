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

  public static HashSet<URL> crawl(String seed_Url, int pages_To_Crawl) throws MalformedURLException{

    // We will return a list of Strings corresponding to "hits" to investigate.

    ArrayDeque<URL> queueOf_SearchableURLs = new ArrayDeque<>(); // Initialize queue of URLs to search
    HashSet<URL> visitedSites = new HashSet<>(); // Set to track our progress
    int limit = pages_To_Crawl;

    String[] schemes = {"http", "https"}; // We will be looking at only http and https protocols as our seed

    UrlValidator urlValidatorObj = new UrlValidator(schemes);


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

      // while the pending URL queue isn't empty AND we haven't reached the limit of pages to parse.

      URL currentURL = queueOf_SearchableURLs.remove();


      // currentURL is the URL previously at head of the queue.



      // At this point, we KNOW we have a valid, unvisited URL in "currentURL"

      Document myDoc = null; // Solely declared for scoping purposes here.

      try{
    	  /*
    	   * Jsoup.parse(URL myURL, int millisToTimeOut) is a method in JSoup library that allows
    	   * us to parse a URL into a document for operationalization.
    	   */
    	  myDoc = Jsoup.parse(currentURL, 10000000);
      }catch (IOException ex){
    	  System.out.println("IO");
    	  /*
    	   * Promptly note the IO exception in console and continue
    	   * Note: We do NOT want to stop the program just because a certain URL can't be resolved.
    	   */
    	  continue;
      }

      // We now have an acceptable HTML document to parse via Jsoup connection

      Elements currentURL_Links = myDoc.select("a[href]");
      /* Returns an ArrayList<Element> object, in this case, links to be used.
       * Empty if none are found.
       */


      for(int i = 0; i < currentURL_Links.size(); i++){
    	  /*
    	   * Iterate over each Element object and add them to the queue if they
    	   * fit our specifications.
    	   */
    	  Element currentEle = currentURL_Links.get(i);

    	  /*
    	   * Retrieves the ith element (link) within our list of elements (links)
    	   * Now we have a sole element (link) to work with. (href = "https://www.example.com")
    	   */

    	  String link_Href = currentEle.attr("abs:href");

    	  /*
    	   * Node.attr(String key) method returns the value of the attribute matching our key
    	   * In this case, it's href because we want a String representation of the actual link value.
    	   */

    	  if(!visitedSites.contains(link_Href) && (queueOf_SearchableURLs.size() + visitedSites.size()) < limit){

    		  /*
    		   *  If our HashSet does not contain this URL AND we KNOW that
    		   *  the amount of visited sites PLUS the amount of sites
    		   *  waiting in the queue is less than our overall limit of sites
    		   *  to explore, then we add the URL to the queue.
    		   */

    		  queueOf_SearchableURLs.add(new URL(link_Href));
    	  } else continue;


      }

      /*
       *  Don't forget to add the current URL to the HashSet for future reference.
       *  Note that adding duplicates to the HashSet won't actually put them in, so we don't
       *  need an if-statement before we add the current URL.
       */

      visitedSites.add(currentURL);


      System.out.println("Just visited: " + currentURL.toString() +  "\nSize of Path: " + visitedSites.size());
      // Test code.


    }

    return visitedSites; // return our URL path.

  }

  public static void main(String[] args) throws MalformedURLException{


	  HashSet<URL> pathTraveled = crawl("http://www.wired.com/", 15);


	  Iterator<URL> iterator = pathTraveled.iterator();
	  /*
	   * Iterator will serve to print out each URL in string format to show us where
	   * we've been, No duplicates.
	   */

	  while(iterator.hasNext()){
		  System.out.println(iterator.next());
	  }


  } // main

} // class
