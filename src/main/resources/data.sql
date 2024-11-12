drop procedure if exists graph$$

CREATE PROCEDURE graph(start_date DATE, end_date DATE)
BEGIN
    DECLARE h INT DEFAULT 1;
    DECLARE total INT;
    DECLARE no_served INT;
    DECLARE served INT;
    DECLARE no_people INT;
    DECLARE new_date Date;

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
        WHERE m.date <= DATE_ADD(new_date, INTERVAL 1 MONTH);
        SELECT COUNT(*)
        INTO no_served
        FROM mapping m
        WHERE NOT EXISTS (
            SELECT * FROM mapping_action ma
            JOIN action a ON ma.action_id = a.id
            WHERE ma.mapping_id = m.id AND a.datetime_start BETWEEN DATE(new_date) AND DATE_ADD(new_date, INTERVAL 1 MONTH) AND ma.no_people = 1
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
        INSERT INTO tags (name) VALUES 
        ("Comidas"),
        ("Roupas"),
        ("Remedios"),
        ("Cobertores"),
        ("Higiene"),
        ("Agasalhos"),
        ("Calçados"),
        ("Atendimento Médico"),
        ("Apoio Psicológico"),
        ("Abrigos Temporários"),
        ("Apoio Jurídico"),
        ("Transporte"),
        ("Documentação"),
        ("Emprego"),
        ("Educação"),
        ("Serviços Sociais"),
        ("Acessibilidade"),
        ("Doações"),
        ("Atividades Recreativas"),
        ("Voluntariado");
    END IF;
END $$

call create_tags();