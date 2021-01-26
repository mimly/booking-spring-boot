--CREATE TABLE IF NOT EXISTS Assistants (
--    id INT AUTO_INCREMENT  PRIMARY KEY,
--    username VARCHAR(255) NOT NULL
--    password VARCHAR(255) NOT NULL
--);
--
--CREATE TABLE IF NOT EXISTS TimeSlots (
--    id INT AUTO_INCREMENT  PRIMARY KEY,
--    time VARCHAR(255) NOT NULL,
--    reserved BOOLEAN DEFAULT FALSE,
--    confirmedBy VARCHAR(255) DEFAULT NULL,
--    assistantId INT DEFAULT NULL REFERENCES Assistants (id) ON DELETE SET NULL ON UPDATE CASCADE
--);

INSERT INTO Assistants (username, password) VALUES
    ('Lorem', '$2y$12$y4KQSATbcCUSv6rXMVpdlec/PtDBh5JMZkHcZm99gYFsSfUULPbhS'),
    ('Ipsum', '$2y$12$xNbQOIxFwSyNPOvp2vy5jeBuLMYf3wsU.KcrepOjKtQezhaEqpboi');

INSERT INTO TimeSlots (time, assistantId) VALUES
    ('00:00', '1'),
    ('03:00', '1'),
    ('06:00', '1'),
    ('09:00', '1'),
    ('00:00', '2'),
    ('03:00', '2'),
    ('06:00', '2'),
    ('09:00', '2');