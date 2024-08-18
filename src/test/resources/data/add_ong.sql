INSERT INTO Address (cep, city, neighbourhood, number, state, street)
VALUES ('04446060', 'São Paulo', 'Jardim Sabará', 100, 'SP', 'Avenida Assaré');

INSERT INTO ONG (address_id, cnpj, name, status)
    VALUES (1, '20.438.196/0001-08', 'Hamburgada do Bem',	'PENDING'),
           (1, '14.248.661/0001-45', 'Instituto Heleninha',	'PENDING'),
           (1, '44.454.154/0001-29', 'Instituto A Corrente do Bem',	'PENDING');
