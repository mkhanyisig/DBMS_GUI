----------------------------------------------------------------
-- Mkhanyisi Gamedze

-- COSI 127B Programming Assignment #2
----------------------------------------------------------------


-----------
-- Task #1 (20pt):
-----------
/*
Add an integer attribute called sensor_count to the FOREST table with a default value of zero.
*/
ALTER TABLE forest
ADD sensor_count INT
DEFAULT 0;

/*
Create a stored procedure called incrementSensorCount_proc that checks which forests
have the specified sensor coordinates in them, and increments the sensor_count
attribute of all these forests. The procedure has the following inputs:
- sensor x: the X coordinate of a sensor.
- sensor y: the Y coordinate of a sensor.
*/
CREATE OR REPLACE PROCEDURE incrementSensorCount_proc(sensor_x REAL, sensor_y REAL)
	language 'plpgsql'
	AS $$
	BEGIN
		UPDATE FOREST
		SET sensor_count = sensor_count+1
		WHERE ( "mbr xmin" <= sensor_x AND "mbr xmax" >= sensor_x ) AND ("mbr ymin" <= sensor_y  AND "mbr ymax" >= sensor_y );
	END
	$$;

-----------
-- Task #2 (20pt):
-----------
/*
Create the function computePercentage that, given a specific area covered amount,
returns the ratio of that part to the full forest’s area. The function has the following inputs:
- forest_no: The ID of the forest in consideration.
- area_covered: the part of the forest’s area covered by some state
*/
CREATE OR REPLACE FUNCTION computePercentage(forest_no varchar(10), area_covered REAL)
	RETURNS REAL
	AS $$
	DECLARE
		coverage_percentage REAL;
	BEGIN
		SELECT area_covered / area INTO coverage_percentage
		FROM coverage
		WHERE coverage."forest no"=forest_no;

		RETURN coverage_percentage;
	END;
	$$ language 'plpgsql';

-----------
-- Task #3 (30pt):
-----------
/*
Define a trigger sensorCount_tri, so that when a new sensor is added, the sensor_count
value for all forests in which the new sensor is located, will automatically be incremented
by one.
- Hint: you will need to write the corresponding function for the trigger
*/

CREATE OR REPLACE FUNCTION updateForestSensor_count()
	RETURNS TRIGGER
	AS $$
	BEGIN
		CALL incrementSensorCount_proc(NEW.x,NEW.y);
		RETURN NEW;
	END;
	$$ language 'plpgsql';

CREATE OR REPLACE TRIGGER sensorCount_tri AFTER INSERT
	ON sensor
	FOR EACH ROW
	EXECUTE PROCEDURE updateForestSensor_count();

  -----------
  -- Task #4 (30pt):
  -----------
/*
Add a table named EMERGENCY. It has two attributes, and it is defined as follows:
- EMERGENCY (sensor id, report time)
- FK (sensor id, report time) → REPORT(sensor id, report time)
Define a trigger emergency_tri, so that when a new report is inserted with the reported
temperature higher than 100 degrees, the trigger inserts a corresponding tuple into the
table EMERGENCY.
*/
DROP TABLE IF EXISTS EMERGENCY CASCADE;
CREATE TABLE EMERGENCY(
	"sensor id" INTEGER,
    "report time" TIMESTAMP,
	FOREIGN KEY ("sensor id","report time") REFERENCES REPORT("sensor id","report time")
);

CREATE OR REPLACE FUNCTION updateEmegerncyTable()
	RETURNS TRIGGER
	AS $$
	BEGIN
		-- No need to create procedure, just duplicate report values
		INSERT INTO EMERGENCY VALUES (NEW."sensor id",NEW."report time");
		RETURN NEW;
	END;
	$$ language 'plpgsql';
