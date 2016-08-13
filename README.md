# Spider Project

Primary Goal:

- Design a web crawler that will crawl:
  - 1.) Up to a user-determined number of pages
  - 2.) To Exhaustion

- Crawler is given the seed URL prior to runtime and will deploy breadth-first search to crawl until one of its stopping conditions have been reached

- Crawler will only stop if either:
    1.) Pre-determined limit of pages have been crawled (if applicable)
    2.) Queue of pending links to explore becomes completely exhausted

- Crawler may implement 3rd-party Java libraries if they improve/simplify the overall functionality
  - External Libraries for web operationalization include:
    - .net
    - apache
    - jsoup

- If Crawler throws a MalformedURLException or a IOException, it will stop operations on the current URL and will pop off the next URL to be operationalized.
  - We don't want the crawler to stop simply because we attempt to access an link that throws an IOException or a MalformedURLException.

- Particular schemes that the crawler will support are subject to change (and the list will be updated here respectively):
  - HTTP
  - HTTPS


Desirable Feature(s) (To be implemented):

- Tag sites that have mentioned a specific user-given String. (Done)

- Implement an exhaustive crawl, to continue crawling until the queue of links to be searched. It is accessed by using an overloaded static crawl method. (Done)

- Have the crawler create and write the list of URLs into a txtFile, whose path is determined by a passed String parameter. (Done)
