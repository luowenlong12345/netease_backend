INSERT INTO role(name, description, create_time, modify_time)
SELECT 'SELLER', 'SELLER', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS(SELECT name FROM role WHERE name = "SELLER");

INSERT INTO role(name, description, create_time, modify_time)
SELECT 'BUYER', 'BUYER', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS(SELECT name FROM role WHERE name = "BUYER");