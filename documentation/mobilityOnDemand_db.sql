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

-- Add some users:
INSERT INTO Users (User_ID, First_Name, Last_Name, Gender, Age)
SELECT 
    1, 
    'Lisa', 
    'Simpson', 
	0,
	14
WHERE not exists (SELECT * FROM Users where User_ID=1);

INSERT INTO Users (User_ID, First_Name, Last_Name, Gender, Age)
SELECT
    2,
    'Bart',
    'Simpson',
	1,
	15
WHERE not exists (SELECT * FROM Users where User_ID=2);

INSERT INTO Users (User_ID, First_Name, Last_Name, Gender, Age)
SELECT
    3,
    'Marge',
    'Simpson',
	0,
	43
WHERE not exists (SELECT * FROM Users where User_ID=3);

INSERT INTO Users (User_ID, First_Name, Last_Name, Gender, Age)
SELECT
    4,
    'Homer',
    'Simpson',
	1,
	48
WHERE not exists (SELECT * FROM Users where User_ID=4);

-- add some cars
INSERT INTO Static_Car (Car_ID,Model,Engine,Infotainment_System,Interior_Design)
SELECT
    101,
    'Polo',
    '100 PS',
	'Basic',
	'Comfort'
WHERE not exists (SELECT * FROM Static_Car where Car_ID=101);

INSERT INTO Dynamic_Car (Car_ID,Status,Current_Location)
SELECT
    101,
    'free',
    12
WHERE not exists (SELECT * FROM Dynamic_Car where Car_ID=101);

INSERT INTO Static_Car (Car_ID,Model,Engine,Infotainment_System,Interior_Design)
SELECT
    102,
    'Passat',
    '130 PS',
	'Comfort',
	'Comfort'
WHERE not exists (SELECT * FROM Static_Car where Car_ID=102);

INSERT INTO Dynamic_Car (Car_ID,Status,Current_Location)
SELECT
    102,
    'free',
    5
WHERE not exists (SELECT * FROM Dynamic_Car where Car_ID=102);

INSERT INTO Static_Car (Car_ID,Model,Engine,Infotainment_System,Interior_Design)
SELECT
    103,
    'Golf',
    '110 PS',
	'Comfort',
	'Luxury'
WHERE not exists (SELECT * FROM Static_Car where Car_ID=103);

INSERT INTO Dynamic_Car (Car_ID,Status,Current_Location)
SELECT
    103,
    'free',
    17
WHERE not exists (SELECT * FROM Dynamic_Car where Car_ID=103);