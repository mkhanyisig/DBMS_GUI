----------------------------------------------------------------
-- Mkhanyisi Gamedze


-- COSI 127B Assignment #1, Template for Answers to Question #2
----------------------------------------------------------------
-- Use CREATE TABLE statement to create tables for these eight relations.


---------------------------------------------------------------
--a. FOREST (forest no, name, area, acid level, mbr xmin, mbr xmax, mbr ymin, mbr ymax)
--     PK (forest no);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS FOREST CASCADE;
CREATE TABLE FOREST (
	"forest no" VARCHAR ( 10 ) PRIMARY KEY,
	name VARCHAR ( 30 ) NOT NULL,
	area REAL ,
    "acid level" REAL,
	"mbr xmin" REAL,
    "mbr xmax" REAL,
    "mbr ymin" REAL,
    "mbr ymax" REAL
);


---------------------------------------------------------------
--b: STATE (name, abbreviation, area, population)
--     PK (abbreviation);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS STATE CASCADE;
CREATE TABLE STATE (
	name VARCHAR ( 30 ) NOT NULL,
	abbreviation VARCHAR ( 2 ) PRIMARY KEY ,
	area REAL,
  population INT
);


---------------------------------------------------------------
--c: COVERAGE (forest no, state, percentage, area)
--     PK (forest no, state);
--     FK (forest no) --> FOREST(forest no);
--     FK (state) --> STATE(abbreviation);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS COVERAGE;
CREATE TABLE COVERAGE (
	"forest no" VARCHAR ( 10 ) ,
	"state" VARCHAR ( 2 )  ,
    percentage REAL,
    area REAL,
    PRIMARY KEY ("forest no", state),
	FOREIGN KEY ("forest no") REFERENCES FOREST("forest no"),
	FOREIGN KEY ("state") REFERENCES STATE(abbreviation)
);


---------------------------------------------------------------
--d: ROAD (road no, name, length)
--     PK (road no);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS ROAD CASCADE;
CREATE TABLE ROAD(
	"road no" VARCHAR ( 10 ) PRIMARY KEY,
	name VARCHAR ( 30 ) ,
    length REAL
);


---------------------------------------------------------------
--e: INTERSECTION (forest no, road no)
--     PK (forest no, road no);
--     FK (forest no) --> FOREST(forest no);
--     FK (road no) --> ROAD(road no);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS INTERSECTION;
CREATE TABLE INTERSECTION(
	"road no" VARCHAR ( 10 ) ,
	"forest no" VARCHAR ( 10 ) ,
  PRIMARY KEY ("forest no", "road no"),
	FOREIGN KEY ("road no") REFERENCES ROAD("road no")
);


---------------------------------------------------------------
--f: WORKER (ssn, name, rank, employing_state)
--     PK (ssn);
--     FK (employing_state) --> STATE(abbreviation);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS WORKER;
CREATE TABLE WORKER(
	ssn VARCHAR ( 9 ) PRIMARY KEY,
	name VARCHAR ( 30 ),
    rank INT,
    employing_state VARCHAR (2) ,
	FOREIGN KEY ("employing_state") REFERENCES STATE("abbreviation")
);

---------------------------------------------------------------
--g: SENSOR (sensor id, x, y, last charged, maintainer, last read)
--     PK (sensor id);
--     FK (maintainer) --> WORKER(ssn);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS SENSOR CASCADE;
CREATE TABLE SENSOR(
	"sensor id" INTEGER PRIMARY KEY,
	x REAL,
    y REAL,
    "last charged" TIMESTAMP,
    maintainer VARCHAR(9),
    "last read" TIMESTAMP,
    energy REAL
);

---------------------------------------------------------------
--h: REPORT (sensor id, report time, temperature)
--     PK (sensor id, report time);
--     FK (sensor id) --> SENSOR(sensor id);
-- [PASTE DDL BELOW]
---------------------------------------------------------------
DROP TABLE IF EXISTS REPORT;
CREATE TABLE REPORT(
	"sensor id" INTEGER ,
	"report time" TIMESTAMP,
    temperature REAL,
    PRIMARY KEY ("report time", "sensor id"),
	FOREIGN KEY ("sensor id") REFERENCES SENSOR("sensor id")
);

----------------------------------------------------------------
-- COSI 127B Assignment #1, Template for Answers to Question #3
----------------------------------------------------------------

---------------------------------------------------------------
--a: List the names of all forests that have acid_level between 0.65 and 0.85 inclusive.
-- [PASTE SQL BELOW]
---------------------------------------------------------------
SELECT name FROM FOREST WHERE ("acid level">=0.65 AND "acid level" <=0.85);

---------------------------------------------------------------
--b: Find the names of all roads in the forest whose name is "Allegheny National Forest".
-- [PASTE SQL BELOW]
---------------------------------------------------------------

SELECT ROAD.name FROM
	(SELECT "road no", name FROM INTERSECTION INNER JOIN FOREST ON INTERSECTION."forest no" = FOREST."forest no"  WHERE name='Allegheny National Forest') as ROADS
	LEFT JOIN ROAD ON ROADS."road no" = ROAD."road no"
;

---------------------------------------------------------------
--c: List all the sensors along with the name of the workers who maintain them. The sensors without maintainers should also be listed.
-- [PASTE SQL BELOW]
---------------------------------------------------------------
SELECT SENSOR."sensor id" as "Sesnor ID",WORKER.name as "Maintainer Name" FROM SENSOR LEFT JOIN WORKER ON SENSOR.maintainer=WORKER.ssn;



---------------------------------------------------------------
--d: List the pairs of states that share at least one forest (i.e., cover parts of the same forests). Each pair should be listed only once, e.g., if the tuple (PA, OH) is already listed, then the tuple (OH, PA) should not be listed.
-- [PASTE SQL BELOW]
---------------------------------------------------------------
SELECT DISTINCT *  FROM
(SELECT SI.state as "State I", COVERAGE.state as "State II" FROM
  (SELECT "road no",INTERSECTION."forest no",COVERAGE.state FROM INTERSECTION INNER JOIN COVERAGE ON INTERSECTION."forest no"=COVERAGE."forest no") as SI
  INNER JOIN COVERAGE ON (SI."forest no"=COVERAGE."forest no" and SI.state!=COVERAGE.state )
 ) as PAIRS
;



---------------------------------------------------------------
--e: For each forest, find its average temperature and number of sensors. Display the result in descending order of the average temperatures.
-- [PASTE SQL BELOW]
---------------------------------------------------------------
SELECT name, AVG(FOREST_SENSOR."Average Temperature") AS "Average Sensor Temperature", COUNT("sid I") AS "Num Sensors" FROM
	(SELECT * FROM
			(SELECT * FROM
				(SELECT "sensor id" AS "sid I",AVG(temperature) AS "Average Temperature", COUNT("sensor id") AS "Num Reports"
				FROM REPORT
				GROUP BY "sensor id"
				ORDER BY "sensor id" ASC) AS SENSORS
					LEFT JOIN SENSOR ON SENSORS."sid I"=SENSOR."sensor id"
				) AS LOC_SENSOR
					LEFT JOIN FOREST ON ((LOC_SENSOR.x>=FOREST."mbr xmin") AND
										 (LOC_SENSOR.x<=FOREST."mbr xmax") AND
										 (LOC_SENSOR.y>=FOREST."mbr ymin") AND
										 (LOC_SENSOR.y<=FOREST."mbr ymax"))
	) AS FOREST_SENSOR
	GROUP BY name
	ORDER BY "Average Sensor Temperature" DESC
;

---------------------------------------------------------------
--f: List the number of sensors and the workers that maintain them. Sensors that are not assigned to maintainers, count them and associate them with '-NO MAINTAINER-'.
-- [PASTE SQL BELOW]
---------------------------------------------------------------

SELECT COUNT("sensor id") AS "sensor count", coalesce(maintainer, '-NO MAINTAINER-') AS "Worker (SSN)"  ,coalesce(WORKER.name, '-NA-') AS "Worker Name" FROM SENSOR
LEFT JOIN WORKER ON SENSOR.maintainer=WORKER.ssn
GROUP BY maintainer,WORKER.name
;


INSERT INTO FOREST VALUES ('1','Allegheny National Forest',3500,0.31,20,90,10,60);
INSERT INTO FOREST VALUES ('2','Pennsylvania Forest',2700,0.74,40,70,20,110);
INSERT INTO FOREST VALUES ('3','Stone Valley',5000,0.56,60,160,30,80);
INSERT INTO FOREST VALUES ('4','Big Woods',3000,0.92,150,180,20,120);
INSERT INTO FOREST VALUES ('5','Crooked Forest',2400,0.23,100,140,70,130);

INSERT INTO STATE (name, abbreviation, area, population) VALUES ('Pennsylvania', 'PA', '50000', '14000000');
INSERT INTO STATE (name, abbreviation, area, population) VALUES ('Ohio', 'OH', '45000', '12000000');
INSERT INTO STATE (name, abbreviation, area, population) VALUES ('Virginia', 'VA', '35000', '10000000');

INSERT INTO COVERAGE VALUES (1,'OH',1,3500);
INSERT INTO COVERAGE VALUES (2,'OH',1,2700);
INSERT INTO COVERAGE VALUES (3,'OH',0.3,1500);
INSERT INTO COVERAGE VALUES (3,'PA',0.42,2100);
INSERT INTO COVERAGE VALUES (3,'VA',0.28,1400);
INSERT INTO COVERAGE VALUES (4,'PA',0.4,1200);
INSERT INTO COVERAGE VALUES (4,'VA',0.6,1800);
INSERT INTO COVERAGE VALUES (5,'VA',1,2400);

INSERT INTO ROAD VALUES (1,'Forbes',500);
INSERT INTO ROAD VALUES (2,'Bigelow',300);
INSERT INTO ROAD VALUES (3,'Bayard',555);
INSERT INTO ROAD VALUES (4,'Grant',100);
INSERT INTO ROAD VALUES (5,'Carson',150);
INSERT INTO ROAD VALUES (6,'Greatview',180);
INSERT INTO ROAD VALUES (7,'Beacon',333);

INSERT INTO INTERSECTION VALUES (1,1);
INSERT INTO INTERSECTION VALUES (1,2);
INSERT INTO INTERSECTION VALUES (1,4);
INSERT INTO INTERSECTION VALUES (1,7);
INSERT INTO INTERSECTION VALUES (2,1);
INSERT INTO INTERSECTION VALUES (2,4);
INSERT INTO INTERSECTION VALUES (2,5);
INSERT INTO INTERSECTION VALUES (2,6);
INSERT INTO INTERSECTION VALUES (2,7);
INSERT INTO INTERSECTION VALUES (3,3);
INSERT INTO INTERSECTION VALUES (3,5);
INSERT INTO INTERSECTION VALUES (4,4);
INSERT INTO INTERSECTION VALUES (4,5);
INSERT INTO INTERSECTION VALUES (4,6);
INSERT INTO INTERSECTION VALUES (5,1);
INSERT INTO INTERSECTION VALUES (5,3);
INSERT INTO INTERSECTION VALUES (5,5);
INSERT INTO INTERSECTION VALUES (5,6);

INSERT INTO WORKER (ssn, name,  rank, employing_state) VALUES ('123456789','John',6, 'OH');
INSERT INTO WORKER (ssn, name,  rank, employing_state) VALUES ('121212121','Jason',5,'PA');
INSERT INTO WORKER (ssn, name,  rank, employing_state) VALUES ('222222222','Mike',4,'OH');
INSERT INTO WORKER (ssn, name,  rank, employing_state) VALUES ('333333333','Tim',2,'VA');

INSERT INTO SENSOR VALUES (1,33,29,TO_TIMESTAMP('6/28/2020 22:00', 'mm/dd/yyyy hh24:mi'),'123456789',TO_TIMESTAMP('12/1/2020 22:00', 'mm/dd/yyyy hh24:mi'),6);
INSERT INTO SENSOR VALUES (2,78,24,TO_TIMESTAMP('7/9/2020 23:00', 'mm/dd/yyyy hh24:mi'),'222222222',TO_TIMESTAMP('11/1/2020 18:30', 'mm/dd/yyyy hh24:mi'),8);
INSERT INTO SENSOR VALUES (3,51,51,TO_TIMESTAMP('9/1/2020 18:30', 'mm/dd/yyyy hh24:mi'),'222222222',TO_TIMESTAMP('11/9/2020 8:25', 'mm/dd/yyyy hh24:mi'),4);
INSERT INTO SENSOR VALUES (4,67,49,TO_TIMESTAMP('9/9/2020 22:00', 'mm/dd/yyyy hh24:mi'),'121212121',TO_TIMESTAMP('12/6/2020 22:00', 'mm/dd/yyyy hh24:mi'),6);
INSERT INTO SENSOR VALUES (5,66,92,TO_TIMESTAMP('9/11/2020 22:00', 'mm/dd/yyyy hh24:mi'),'123456789',TO_TIMESTAMP('11/7/2020 22:00', 'mm/dd/yyyy hh24:mi'),6);
INSERT INTO SENSOR VALUES (6,100,52,TO_TIMESTAMP('9/13/2020 22:00', 'mm/dd/yyyy hh24:mi'),'121212121',TO_TIMESTAMP('11/9/2020 23:00', 'mm/dd/yyyy hh24:mi'),5);
INSERT INTO SENSOR VALUES (7,111,41,TO_TIMESTAMP('9/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),'222222222',TO_TIMESTAMP('11/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),2);
INSERT INTO SENSOR VALUES (8,120,75,TO_TIMESTAMP('10/13/2020 22:00', 'mm/dd/yyyy hh24:mi'),'123456789',TO_TIMESTAMP('11/13/2020 22:00', 'mm/dd/yyyy hh24:mi'),6);
INSERT INTO SENSOR VALUES (9,124,108,TO_TIMESTAMP('10/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),'333333333',TO_TIMESTAMP('11/28/2020 22:00', 'mm/dd/yyyy hh24:mi'),7);
INSERT INTO SENSOR VALUES (10,153,50,TO_TIMESTAMP('11/10/2020 20:00', 'mm/dd/yyyy hh24:mi'),'333333333',TO_TIMESTAMP('11/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),1);
INSERT INTO SENSOR VALUES (11,151,33,TO_TIMESTAMP('11/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),'222222222',TO_TIMESTAMP('11/27/2020 22:00', 'mm/dd/yyyy hh24:mi'),2);
INSERT INTO SENSOR VALUES (12,151,73,TO_TIMESTAMP('11/28/2020 22:00', 'mm/dd/yyyy hh24:mi'),'121212121',TO_TIMESTAMP('11/30/2020 9:03', 'mm/dd/yyyy hh24:mi'),2);
INSERT INTO SENSOR VALUES (13,100,20,TO_TIMESTAMP('11/28/2020 22:00', 'mm/dd/yyyy hh24:mi'),NULL,TO_TIMESTAMP('11/30/2020 9:03', 'mm/dd/yyyy hh24:mi'),2);

INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('5/10/2020 22:00', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (11,TO_TIMESTAMP('5/24/2020 13:40', 'mm/dd/yyyy hh24:mi'),88);
INSERT INTO REPORT VALUES (12,TO_TIMESTAMP('6/28/2020 22:00', 'mm/dd/yyyy hh24:mi'),87);
INSERT INTO REPORT VALUES (6,TO_TIMESTAMP('7/9/2020 23:00', 'mm/dd/yyyy hh24:mi'),38);
INSERT INTO REPORT VALUES (2,TO_TIMESTAMP('9/1/2020 18:30', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (1,TO_TIMESTAMP('9/1/2020 22:00', 'mm/dd/yyyy hh24:mi'),34);
INSERT INTO REPORT VALUES (3,TO_TIMESTAMP('9/5/2020 10:00', 'mm/dd/yyyy hh24:mi'),57);
INSERT INTO REPORT VALUES (4,TO_TIMESTAMP('9/6/2020 22:00', 'mm/dd/yyyy hh24:mi'),62);
INSERT INTO REPORT VALUES (5,TO_TIMESTAMP('9/7/2020 22:00', 'mm/dd/yyyy hh24:mi'),52);
INSERT INTO REPORT VALUES (3,TO_TIMESTAMP('9/9/2020 8:25', 'mm/dd/yyyy hh24:mi'),61);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('9/9/2020 22:00', 'mm/dd/yyyy hh24:mi'),37);
INSERT INTO REPORT VALUES (1,TO_TIMESTAMP('9/10/2020 20:00', 'mm/dd/yyyy hh24:mi'),58);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('9/10/2020 22:00', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (8,TO_TIMESTAMP('9/11/2020 2:00', 'mm/dd/yyyy hh24:mi'),44);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('9/11/2020 22:00', 'mm/dd/yyyy hh24:mi'),49);
INSERT INTO REPORT VALUES (8,TO_TIMESTAMP('9/13/2020 22:00', 'mm/dd/yyyy hh24:mi'),51);
INSERT INTO REPORT VALUES (9,TO_TIMESTAMP('9/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),55);
INSERT INTO REPORT VALUES (10,TO_TIMESTAMP('9/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),70);
INSERT INTO REPORT VALUES (11,TO_TIMESTAMP('9/24/2020 13:40', 'mm/dd/yyyy hh24:mi'),88);
INSERT INTO REPORT VALUES (11,TO_TIMESTAMP('9/27/2020 22:00', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (12,TO_TIMESTAMP('9/30/2020 9:03', 'mm/dd/yyyy hh24:mi'),60);
INSERT INTO REPORT VALUES (2,TO_TIMESTAMP('10/1/2020 18:30', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (1,TO_TIMESTAMP('10/1/2020 22:00', 'mm/dd/yyyy hh24:mi'),34);
INSERT INTO REPORT VALUES (3,TO_TIMESTAMP('10/5/2020 10:00', 'mm/dd/yyyy hh24:mi'),57);
INSERT INTO REPORT VALUES (5,TO_TIMESTAMP('10/7/2020 22:00', 'mm/dd/yyyy hh24:mi'),52);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('10/9/2020 22:00', 'mm/dd/yyyy hh24:mi'),37);
INSERT INTO REPORT VALUES (6,TO_TIMESTAMP('10/9/2020 23:00', 'mm/dd/yyyy hh24:mi'),38);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('10/10/2020 22:00', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('10/11/2020 22:00', 'mm/dd/yyyy hh24:mi'),49);
INSERT INTO REPORT VALUES (8,TO_TIMESTAMP('10/13/2020 22:00', 'mm/dd/yyyy hh24:mi'),51);
INSERT INTO REPORT VALUES (10,TO_TIMESTAMP('10/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),70);
INSERT INTO REPORT VALUES (11,TO_TIMESTAMP('10/24/2020 13:40', 'mm/dd/yyyy hh24:mi'),88);
INSERT INTO REPORT VALUES (11,TO_TIMESTAMP('10/27/2020 22:00', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (12,TO_TIMESTAMP('10/30/2020 9:03', 'mm/dd/yyyy hh24:mi'),60);
INSERT INTO REPORT VALUES (2,TO_TIMESTAMP('11/1/2020 18:30', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (3,TO_TIMESTAMP('11/5/2020 10:00', 'mm/dd/yyyy hh24:mi'),57);
INSERT INTO REPORT VALUES (3,TO_TIMESTAMP('11/6/2020 11:00', 'mm/dd/yyyy hh24:mi'),53);
INSERT INTO REPORT VALUES (4,TO_TIMESTAMP('11/6/2020 22:00', 'mm/dd/yyyy hh24:mi'),62);
INSERT INTO REPORT VALUES (5,TO_TIMESTAMP('11/7/2020 22:00', 'mm/dd/yyyy hh24:mi'),52);
INSERT INTO REPORT VALUES (3,TO_TIMESTAMP('11/9/2020 8:25', 'mm/dd/yyyy hh24:mi'),61);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('11/9/2020 22:00', 'mm/dd/yyyy hh24:mi'),37);
INSERT INTO REPORT VALUES (6,TO_TIMESTAMP('11/9/2020 23:00', 'mm/dd/yyyy hh24:mi'),38);
INSERT INTO REPORT VALUES (1,TO_TIMESTAMP('11/10/2020 20:00', 'mm/dd/yyyy hh24:mi'),58);
INSERT INTO REPORT VALUES (8,TO_TIMESTAMP('11/11/2020 2:00', 'mm/dd/yyyy hh24:mi'),44);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('11/11/2020 22:00', 'mm/dd/yyyy hh24:mi'),49);
INSERT INTO REPORT VALUES (11,TO_TIMESTAMP('11/11/2020 22:00', 'mm/dd/yyyy hh24:mi'),76);
INSERT INTO REPORT VALUES (8,TO_TIMESTAMP('11/13/2020 22:00', 'mm/dd/yyyy hh24:mi'),51);
INSERT INTO REPORT VALUES (7,TO_TIMESTAMP('11/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),47);
INSERT INTO REPORT VALUES (9,TO_TIMESTAMP('11/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),55);
INSERT INTO REPORT VALUES (10,TO_TIMESTAMP('11/21/2020 22:00', 'mm/dd/yyyy hh24:mi'),70);
INSERT INTO REPORT VALUES (12,TO_TIMESTAMP('11/24/2020 13:40', 'mm/dd/yyyy hh24:mi'),77);
INSERT INTO REPORT VALUES (9,TO_TIMESTAMP('11/27/2020 22:00', 'mm/dd/yyyy hh24:mi'),33);
INSERT INTO REPORT VALUES (11,TO_TIMESTAMP('11/27/2020 22:00', 'mm/dd/yyyy hh24:mi'),46);
INSERT INTO REPORT VALUES (9,TO_TIMESTAMP('11/28/2020 22:00', 'mm/dd/yyyy hh24:mi'),35);
INSERT INTO REPORT VALUES (12,TO_TIMESTAMP('11/28/2020 22:00', 'mm/dd/yyyy hh24:mi'),87);
INSERT INTO REPORT VALUES (12,TO_TIMESTAMP('11/30/2020 9:03', 'mm/dd/yyyy hh24:mi'),60);
INSERT INTO REPORT VALUES (1,TO_TIMESTAMP('12/1/2020 22:00', 'mm/dd/yyyy hh24:mi'),34);
INSERT INTO REPORT VALUES (4,TO_TIMESTAMP('12/6/2020 22:00', 'mm/dd/yyyy hh24:mi'),62);
