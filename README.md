# Project1

Primary Goal:

- Design a web crawler that will crawl up to a user-determined number of pages

- Crawler is given the seed URL prior to runtime and will deploy breadth-first search to crawl until one of its stopping conditions have been reached

- Crawler will only stop if either:
    1.) Pre-determined limit of pages have been crawled
    2.) Queue of pending links to explore becomes completely exhausted

- Crawler may implement 3rd-party Java libraries if they improve/simplify the overall functionality

- If Crawler throws a MalformedURLException or a IOException, it will stop operations on the current URL and will pop off the next URL to be operationalized.


Desirable Feature(s) (To be implemented later):

- Tag sites that have mentioned a specific user-given String
