# rubixcodefest

##background
In this hands on session we are going to create an end-to-end integration solution for processing and querying orders. The orders are received from an external system in bulk csv format. The integration solution is responsible for receiving the csv file, transforming it into an xml cdm format, routing the order based on country code and storing it into the order database. For management purposes a webservice is available for querying orders. The webservice has two operations one for retrieving order IDs based on district (NL or international) and one operation for retrieving the order based on ID. This fictional integration solution does cover a variety of different message types, protocols and messaging patterns. For example csv and xml messages are covered, JMS, SQL and webservices are covered and both synchronous and asynchronous messaging is covered. 
Graphically the overall integration solution is depicted as below:

![alt text](https://raw.githubusercontent.com/pimg/rubixcodefest/master/docs/pics/overall.png "overall integration solution")
###Use case 1
![alt text](https://raw.githubusercontent.com/pimg/rubixcodefest/master/docs/pics/Use_case_1_receiveOrders.png "use case 1")
The first use case we are going to develop is an interface which will perform the following tasks:
* Receive a csv file containing bulk orders
* Marshal the csv file into a Java object
* Debatch the bulk orders
* Transform the message into a CDM xml message
* Route the message based on countryCode
* Publish NL orders into an nl orders JMS queue
* Publish international orders into an international orders JMS queue

###Use case 2
![alt text](https://raw.githubusercontent.com/pimg/rubixcodefest/master/docs/pics/Use_case_2_StoreOrders.png "Use case 2")
The second use case we are going to develop is an interface performing the following tasks:
* Receive message from JMS queues
* Prepare message for insert into SQL database
* Insert row in SQL database

### Use case 3 
The third use case we are going to develop is a webservice which queries the orders db.
![alt text](https://raw.githubusercontent.com/pimg/rubixcodefest/master/docs/pics/Use_case_3_OrderInfoService.png "Use case 3")
* Expose a webservice with two operations
.* Search orders by district (NL or IN)
.* getOrder by ID
* Both operations query the database for retrieving the orders

##Github repository structure
* Docs: folder with a PowerPoint containing tips & tricks and a word document with a step by step tutorial for use case 1
* Orderprocessing: het JBoss Fuse project containing all the code of all three use cases
* Resources: a set of resources to speed up development (tips in the PowePoint)
* Test: SoapUI project to test the webservice in use case three

