-- :name read-notes :? :*
-- :doc Get all drugs
SELECT * FROM  note;

-- :name create-data :insert :1
INSERT INTO
note(text)
VALUES(:text);