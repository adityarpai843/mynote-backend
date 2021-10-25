-- :name read-notes :? :*
-- :doc Get all drugs
SELECT * FROM  note;

-- :name create-data :! :n
INSERT INTO
note(text)
VALUES(:text);