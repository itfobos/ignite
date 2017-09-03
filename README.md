# Ignite-WS

Used Apache Ignite as DB + Web Service(Play Framework) to allow CRUD operations.

SBT tool is used for the project building.

## How to start the project:

### Prerequisites:
1. [Docker Compose](https://docs.docker.com/compose/overview/) should be installed.
1. The project is tested with _Docker Compose version 1.15.0_. How to install the right veriosn, [check here](https://docs.docker.com/compose/install/)

### Start (Prod mode)
1. In the root project directory ```cd docker```
1. ```docker-compose up```

Two images will be downloaded from github and launched:
* _turukin/ignite-ws_ 
* _apacheignite/ignite_

### Starting dev mode
1. In the root project directory ```cd docker```
1. ```docker-compose -f docker-compose-dev.yml up```
1. ```cd ../ignite-ws ```
1. ```sbt run ```

In this case, _apacheignite/ignite_ container is started with option _network_mode: host_.
WS is started locally, without docker container.


## WS API:
After starting the WS binds at port _9000_

For POST operations _Content-Type_ header param should be defined as _application/json_

### API Methods

* [/cellUsers](#cellusers) 
* [/addUserToCell](#addusertocell) 
* [/addUserProfile](#adduserprofile) 
* [/populateTestData](#populatetestdata) 

 #### /cellUsers
 Returns all users inside the specified cell
 
 * Request method: **GET**
 * Signature: _/cellUsers/:cellId_
 
 * Example:  
    * _localhost:9000/cellUsers/123qwe_
    * _localhost:9000/cellUsers/anotherCellId_
 
 #### /addUserToCell
 Connets a specified user with a specified cell
 
 * Request method: **POST**
 * Signature: _/addUserToCell_
 
 * Example:  
     _localhost:9000/addUserToCell_
     
     Request body:
```json 
{
  "ctn" : "+79521112233",
  "cellId" : "reallyNewCellID"
}
```
  
 #### /addUserProfile
 Adds new user profile
 
 * Request method: **POST**
 * Signature: _/addUserProfile_
 
 * Example:  
     _localhost:9000/addUserProfile_
     
     Request body:
```json
{
  "ctn" : "+79521112299",
  "name" : "new one user",
  "email" : "some@mail.com"
}
```
  
 
 #### /populateTestData 
  Adds mock data.
  
  For _123qwe_ cellId:
```json
  {
    "ctn": "+79231112233",
    "name": "John",
    "email": "john@russinpost.ru",
    "activationDate": "2017-09-01 12:39:34"
   },
   {
    "ctn": "+79234445588",
    "name": "Alice",
    "email": "alice@russinpost.ru",
    "activationDate": "2017-09-01 12:39:34"
    }
```
  
  For _anotherCellId_ :
```json
  {
    "ctn": "+79521112233",
    "name": "Mark",
    "email": "mark@pechkin.ru",
    "activationDate": "2017-09-01 12:39:34"
  },
  {
    "ctn": "+79524445588",
    "name": "Jane",
    "email": "jane@pechkin.ru",
    "activationDate": "2017-09-01 12:39:34"
  }
```
 
 * Request method: **POST**
 * Signature: _/populateTestData_
 
 * Example:  
     _localhost:9000/populateTestData_
