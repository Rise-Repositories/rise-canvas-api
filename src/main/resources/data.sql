drop procedure if exists graph$$

CREATE PROCEDURE graph(start_date DATE, end_date DATE, tag_ids VARCHAR(7))
BEGIN
    DECLARE h INT DEFAULT 1;
    DECLARE total INT;
    DECLARE no_served INT;
    DECLARE served INT;
    DECLARE no_people INT;
    DECLARE new_date Date;

    DROP TEMPORARY TABLE IF EXISTS temp_tags;
    CREATE TEMPORARY TABLE temp_tags(id INT);

    IF (tag_ids IS NULL) THEN
		INSERT INTO temp_tags VALUES (1),(2),(3),(4);
    ELSE
		INSERT INTO temp_tags VALUES
			(CAST(SUBSTRING_INDEX(tag_ids, '|', 1) AS UNSIGNED)),
			(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(tag_ids, '|', -3), '|', 1) AS UNSIGNED)),
			(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(tag_ids, '|', -2), '|', 1) AS UNSIGNED)),
			(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(tag_ids, '|', -1), '|', 1) AS UNSIGNED));
	END IF;

    if (end_date IS NULL) THEN
		SET end_date := NOW();
	END IF;

    IF (start_date IS NULL) THEN
		SET new_date := date_sub(end_date, interval 1 year);
	ELSE
		SET new_date := start_date;
	END IF;

    DROP TEMPORARY TABLE if exists response;
    CREATE TEMPORARY TABLE response(
		total INT,
        no_served INT,
		served INT,
		no_people INT,
        month INT,
      year INT
    );

    WHILE new_date <= end_date AND h <= 12 DO
		SELECT COUNT(*)
        INTO total
        FROM mapping m
        WHERE m.date <= DATE_ADD(new_date, INTERVAL 1 MONTH)
			AND EXISTS(
				SELECT * FROM mapping_tags mt WHERE mt.mapping_id = m.id AND mt.tags_id IN (SELECT id FROM temp_tags)
            );

        SELECT COUNT(*)
        INTO no_served
        FROM mapping m
        WHERE NOT EXISTS (
            SELECT * FROM mapping_action ma
            JOIN action a ON ma.action_id = a.id
            WHERE ma.mapping_id = m.id AND a.datetime_start BETWEEN DATE(new_date) AND DATE_ADD(new_date, INTERVAL 1 MONTH) AND ma.no_people = 1
        ) AND EXISTS(
			SELECT * FROM mapping_tags mt WHERE mt.mapping_id = m.id AND mt.tags_id IN (SELECT id FROM temp_tags)
		);

        select count(distinct(ma.mapping_id))
        into served
        from mapping_action ma
			join action a on ma.action_id = a.id
		where month(a.datetime_start) = month(new_date) and year(a.datetime_start) = year(new_date) and ma.no_people = 0
		AND EXISTS(
			SELECT * FROM mapping_tags mt WHERE mt.mapping_id = ma.mapping_id AND mt.tags_id IN (SELECT id FROM temp_tags)
		);

        select count(distinct(ma.mapping_id))
        into no_people
        from mapping_action ma
			join action a on ma.action_id = a.id
		where month(a.datetime_start) = month(new_date) and year(a.datetime_start) = year(new_date) and ma.no_people = 1
        AND EXISTS(
			SELECT * FROM mapping_tags mt WHERE mt.mapping_id = ma.mapping_id AND mt.tags_id IN (SELECT id FROM temp_tags)
		);

		SET total = total - no_people;

		SET no_served = total - served;
        INSERT INTO response (total, no_served, served, no_people, month, year) VALUES (total, no_served, served, no_people, MONTH(new_date), YEAR(new_date));

        SET new_date = DATE_ADD(new_date, INTERVAL 1 MONTH);
        SET h = h + 1;
    END WHILE;

    select * from response order by year, month;
END $$

drop procedure if exists create_tags$$

CREATE PROCEDURE create_tags()
BEGIN
    DECLARE total INT;
    select count(*) into total from tags;
    IF(total = 0) THEN
        INSERT INTO tags (id, name) VALUES
        (1, "Comida"),
        (2, "Itens de Higiene"),
        (3, "Roupas/Cobertores"),
        (4, "Outros");
    END IF;
END $$

call create_tags();