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
