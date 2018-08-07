# Mobility-On-Demand API
1. Problem Scope
    -   users create an account with personal data: name, gender, and age
    -   a range of different car models is part of the fleet
    -   car details: regarding the model, the engine, the infotainment system, the interior design, and the current location
    -   user demand details: pick-up and the drop-off location, the earliest pick-up and the latest drop-off time, and the desired car features

2. Assumptions
    -   users have to add billing information for payment
    -   users have to verify as a licensed driver, in case human intervention is necessary or car is not self driving
    -   mobile and web application for users to place a demand
    -   some kind of telematic services to connect cars, users and backend
    -   cars support some kind of black-box, logging and communicating their position, state (e.g. on/off/driving), reservations
    -   users may want to specify favorite car types
    -   one or more car brands could be part of the service
 
3. Major Components
    -   Identity and access 
    -   Service 
    -   User profile 
    -   Billing 
    -   Static car 
    -   Dynamic car 
    
4. Key Issues
    -   demand will vary a lot during the day -> load balancing
    -   when not used, cars are turned off, but some functionality must persist -> battery/energy management is crucial
    
![alt text](https://github.com/iCorv/mobilityOnDemandAPI/blob/master/documentation/mod_database_schema.png "Logo Title Text 1")

## Task 1
Splitting the car entity into three entities allows to individually change the status and current position of the car, 
while other car features remain quite permanent. This might be convenient since the position of the car could be 
updated very often. The virtual car, which will be added to the demand as soon as it meets the requirements, 
will hold the actual pick up and drop off time.
![alt text](https://github.com/iCorv/mobilityOnDemandAPI/blob/master/documentation/er-db-diagram.png "Logo Title Text 1")

The script used to initialize the PostgreSQL db:
```sql
-- Database: mobilityOnDemand_db
   
   -- create tables:
   CREATE TABLE Users (
     User_ID int,
     First_Name varchar(50),
     Last_Name varchar(50),
     Gender int,
     Age int,
     PRIMARY KEY (User_ID)
   );
   
   CREATE TABLE Static_Car (
     Car_ID int,
     Model varchar(50),
     Engine varchar(50),
     Infotainment_System varchar(50),
     Interior_Design varchar(50),
     PRIMARY KEY (Car_ID)
   );
   
   CREATE TABLE Demand (
     Demand_Number int,
     User_ID int references Users(User_ID),
     From_Pick_Up int,
     To_Drop_Off int,
     Earliest_Pick_Up timestamp,
     Latest_Drop_Off timestamp,
     Model varchar(50),
     Engine varchar(50),
     Infotainment_System varchar(50),
     Interior_Design varchar(50),
     PRIMARY KEY (Demand_Number)
   );
   
   
   CREATE TABLE Dynamic_Car (
     Car_ID int references Static_Car(Car_ID),
     Status varchar(50),
     Current_Location int,
     PRIMARY KEY (Car_ID)
   );
   
   
   CREATE TABLE Virtual_Car (
     Demand_Number int references Demand(Demand_Number),
     User_ID int references Users(User_ID),
     Pick_Up_Time timestamp,
     Drop_Off_Time timestamp,
     Car_ID int references Static_Car(Car_ID),
     PRIMARY KEY (Demand_Number, User_ID)
   );
```
## Task 2
### Requirements
1. Cars
    -   adding and removing cars
    -   changing the car details
    -   update the current location of the car independently
2. Users
    -   adding and removing users
    -   changing the user details
3. Demand
    -   adding and removing demands
    -   changing demand details

Implementation of the API was carried out using Intellij IDE, Postman, local PostgreSQL db and a local Glassfish 
application server. Current state: User data can be modified by POST, PUT, GET and DELETE requests, as well as adding 
demands by POST request.

## Task 3
The schedule service could use an execution queue to work through the demands from the database. 
1. Cars have to be identified that are in a certain range to the user
2. Query if these cars meet the desired features (pick-up and drop-off locations as well as the earliest pick-up and drop-off times)
3. Present query results. Advise which features have to be changed in case the schedule could not be met.

Minimization of covered distance by each car, this is a typical "traveling salesman" problem. Possible helpful algorithm could be the A*-Algorithm or the Dijkstra-Algorithm for finding a shortest path in a Graph representation of the coordinate system..

Pick-up and drop-off times are part of the total trip time, e.g. trip_time = pick_up_latency + transit_latency + drop_off_latency. Where the pick_up_latency = walking_time + wait_time and drop_off_latency = walking_time_to_destination.

## Task 4
Four unit test have been implemented. Further testing was done by running http request from Postman on the local glassfish server.



