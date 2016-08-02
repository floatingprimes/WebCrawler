# Project1

Primary Goal:

- To design a primitive web crawler that will begin with a given HTML (seed) and identify every Hyperlink embedded within the webpage. (Must be of HTTP or HTTPS protocol for simplicity/brevity)

- Each Hyperlink is added to a given Queue to be visited at a later time during Runtime, so long as the limit
  hasn't been reached.

- The crawler continues to explore new sites until a user-determined crawl-limit is reached.

Nice Feature(s) to think about Implementing:

- Tag (perhaps by storing them in another data structure) each Hyperlink whose page contains a given keyword or phrase to be returned by the web crawler.

- Return the Data Structure of "hits" when finished.
