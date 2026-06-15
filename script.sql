



SELECT * FROM funcionario;

INSERT INTO funcionario (nome, email, senha)
VALUES ('Administrador', 'admin@admin.com', '123');

ALTER TABLE andar
ADD COLUMN numero INTEGER;

SELECT *
FROM funcionario;

ALTER TABLE apartamento
ADD COLUMN descricao VARCHAR(500);

SELECT column_name
FROM information_schema.columns
WHERE table_name = 'manutencao';

ALTER TABLE manutencao
RENAME COLUMN dataabertura TO data_abertura;

ALTER TABLE manutencao
RENAME COLUMN datafechamento TO data_fechamento;

ALTER TABLE manutencao
ADD COLUMN tipo VARCHAR(100);

ALTER TABLE manutencao
ADD COLUMN observacao_fechamento VARCHAR(500);

ALTER TABLE andar
ADD CONSTRAINT uk_andar_numero UNIQUE (numero);

SELECT numero, COUNT(*)
FROM andar
GROUP BY numero
HAVING COUNT(*) > 1;

ALTER TABLE apartamento
ADD CONSTRAINT uk_apartamento_numero UNIQUE (numero)