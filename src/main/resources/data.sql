drop  procedure if exists graph;

DELIMITER $$
CREATE PROCEDURE graph(between_date DATE)
BEGIN 
    DECLARE h INT DEFAULT 1;
    DECLARE no_served INT;
    DECLARE served INT;
    DECLARE no_people INT;
    DECLARE new_date Date;
    
    set new_date := date_sub(between_date, interval 1 year);
	
    DROP TEMPORARY TABLE if exists response;
    CREATE TEMPORARY TABLE response(
        no_served INT,
		served INT,
		no_people INT,
        month INT,
      year INT
    );

    WHILE h <= 12 DO
        SELECT COUNT(*)
        INTO no_served
        FROM mapping m
        WHERE NOT EXISTS (
            SELECT * FROM mapping_action ma
            JOIN action a ON ma.action_id = a.id
            WHERE ma.mapping_id = m.id AND a.datetime_start BETWEEN DATE(new_date) AND DATE_ADD(new_date, INTERVAL 1 MONTH)
        );
        select count(distinct(ma.mapping_id))
        into served
        from mapping_action ma
		join action a on ma.action_id = a.id
		where month(a.datetime_start) = month(new_date) and year(a.datetime_start) = year(new_date) and ma.no_people = 0;
		
        select count(distinct(ma.mapping_id))
        into no_people
        from mapping_action ma
		join action a on ma.action_id = a.id
		where month(a.datetime_start) = month(new_date) and year(a.datetime_start) = year(new_date) and ma.no_people = 1;

        INSERT INTO response (no_served, served, no_people, month, year) VALUES (no_served, served, no_people, MONTH(new_date), YEAR(new_date));

        SET new_date = DATE_ADD(new_date, INTERVAL 1 MONTH);
        SET h = h + 1;
    END WHILE;
    
    select * from response order by year, month;
END $$