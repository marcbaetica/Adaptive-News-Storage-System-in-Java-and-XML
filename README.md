NewsStorageSystem
=================

Group 6 project by:

Yuan Szsun

Marc-Alexandru Baetica

Myles Mohammed

------------
Progress:
------------

1. Code for the subsystem is done, as well as the Javadoc.

2. Some basic testings have been carried out during the implementation process. Myles and Marc are working on the JUnit testing.



------------
Issues:
------------

1. Adding/removing a node from the database will mess up the format of the file(it's still a valid xml file, just not easy for reading).

2. Removing a node with id attribute will cause the numbering of nodes to have gap. For example:

 ==Before==(each bracket represent a node)

 (1.ab)(2.cd)(3.ef)(4.gh)

 ==After==(delete node 2)

 (1.ab)(3.ef)(4.gh)



------------
Notes:
------------

1. news.xml is a simulation of message from the news controller to the news storage subsystem. Couple assumptions we made:

 (1)The NLP is used to categorized the news in the news controller, so the news.xml is a string of error-free message that is ready for storing to database.

 (2)Time of news has already been error-checked in news controller (leap-year,boundaries etc.).

2. List for all the categories and locations are pre-defined in the database, we assuming the database.xml has the complete list. As a result, any add news request with a non-existing category/location will be rejected.

3. For this version, single piece of news can only have up to 1 category and 1 location entry.
/////
