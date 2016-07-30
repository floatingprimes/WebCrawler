import java.util.ArrayDeque;  // To be our Structure for Queue
import java.util.ArrayList;   // Dynamic array to store URLs to visit later.
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Crawler{

  private Crawler(){};

  public static ArrayList<URL> crawl(String seed_URL, int numberOfLinksToCheck) throws IOException{ // return a "hit list" to explore or make note of.

    if(numberOfLinksToCheck <= 0)
      return null;

    int limit = numberOfLinksToCheck;
    ArrayDeque<URL> queueOfLinks = new ArrayDeque<>();
    ArrayList<URL> link_Hits = new ArrayList<>();

    URL myURL = null;

    try{
      myURL = new URL(seed_URL);  // If user input doesn't reconcile as a URL, print the stack trace.
    }catch (MalformedURLException ex){
      ex.printStackTrace();
    }

    queueOfLinks.add(myURL); // add the seed URL to our List

    while( queueOfLinks.size() < limit && !queueOfLinks.isEmpty() ){

      // While the queue of links to explore is less than our predetermined limit AND the
      // queue of Links to explore is not empty (signifying no more links to explore),then
      // proceed.

      URL current_URLToExplore = queueOfLinks.remove();

      BufferedReader input = null;

      try{
        // Create an InputStreamReader Object corresponding to the URL's stream and wrap it
        // in a BufferedReader for efficiency.

        input = new BufferedReader(new InputStreamReader(current_URLToExplore.openStream()));

      }catch (IOException ex){
        ex.printStackTrace();
      }

      String textLine = null;

      while((textLine = input.readLine()) != null){

        String[] tokens = textLine.split(" ");
        // Read a Line from InputStream and partition it into tokens (a String array) delimited via Space character.

        for(int i = 0; i < tokens.length; i++) // iterate over every token retrieved from the text line.
          if(tokens[i].matches("^https?|ftp|file)://[a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")){

            // If the token matches the regex pattern corresponding to syntax consistent with a URL, add it to be explored.

            queueOfLinks.add(new URL(tokens[i])); // Add the new URL to explore if it matches the URL syntactic Regex

          } else continue; // If it fails to match URL regex, just continue iterating.

      }

    }

    return null;
  }

  public static void main(String[] args){
    // main method (tester)

  }


}
