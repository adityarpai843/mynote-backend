(ns mynote-api.config)

;Database configuration for MYSQL Please ensure that database is created in MYSQL DB
;(here notes is the DB name and note is name of table)

 (def db-config {
    :subprotocol "mysql"
    :subname "//127.0.0.1:3306/notes"
    :user "root"
    :password "1234"})