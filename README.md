# Spider Project

Primary Goal:

- Design a web crawler that will crawl to exhaustion and be restricted to the home seed url domain
	- i.e. If your seed url is http://www.cnn.com/, then the crawler will never exit cnn.com
  
- Crawler is given the seed URL prior to runtime and will deploy breadth-first search (using a form of Queue) to crawl until a stopping condition has been met

- Crawler will only stop if:
	 Queue of pending links to explore becomes completely exhausted

- Crawler may implement 3rd-party Java libraries if they improve/simplify the overall functionality
  - External Libraries for web operationalization include:
    - apache (URL Validator objects)
    - jsoup (URL connections for HTML document sourcing)

- If Crawler throws a MalformedURLException or a IOException, it will stop operations on the current URL and will pop off the next URL to be operationalized.
  - Put differently, we don't want the crawler to stop simply because we attempt to access an link that throws an IOException or a MalformedURLException.

- Particular schemes that the crawler will support are subject to change (and the list will be updated here respectively):
  - HTTP
  - HTTPS


Desirable Feature(s) (To be implemented):

- Tag sites that have mentioned a specific user-given String. (Done)

- Implement an exhaustive crawl, to continue crawling until the queue of links to be searched. It is accessed by using an overloaded static crawl method. (Done)

- Have the crawler create and write the list of URLs into a txtFile, whose path is determined by a passed String parameter. (Done)

- Implement a javafx enabled GUI for interactivity with the user. (Done)

- Provide a Travis CI YAML file for continuous integration and testing on each git pull leveraging Apache Ant. (Done)
