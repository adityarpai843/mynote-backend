(ns mynote-api.notesroute
(:require [mynote-api.sql.notes :as sql]
          [io.pedestal.http :as http]
          [clojure.spec.alpha :as s]
          [mynote-api.config :refer [db-config]]))

;(http/json-response) converts map into json

;sql/query is used to query MYSQL database (Refer here http://clojure-doc.org/articles/ecosystem/java_jdbc/using_sql.html)   

(defn all-notes [_] 
(http/json-response (sql/read-notes db-config)))

;Regular expression checks for empty string and whitespace

;;(def regexp #"!(^$|\s)")

;clojure registry spec for validation of server side data (Refer here https://clojure.org/guides/spec for more)

(s/def ::text string?)

;Clojure Spec Entity map is used for validating map data, :req means its required field (Refer above link)

(s/def ::notespec (s/keys :req-un [::text]))

;create note request handler
(defn create-note [request]
;select specific keys in the request (ie json-params) using select-keys 
(let [new-note (select-keys (-> request :json-params) [:text])]
;we used clojure spec valid to check whether data validation defined above are met
 (if (s/valid? ::notespec new-note)
 ;sql/insert inserts data into database where field is text and if its successfull a message 
 ;Note created successfully is sent tot frontend as JSON {"msg":"Note Created Successfully"} with status code for resource created
         ((sql/create-data db-config new-note)
        (assoc (http/json-response {:msg "Note Created Successfully"}) :status 201))
 ;Below statement is else statement which is sends error message because of some failure in data validation       
      (assoc (http/json-response {:msg "Please Enter a Valid Note."}):status 400))))

 (s/def ::id string?) 

 (s/def ::idspec (s/keys :req-un [::id]))           

(defn delete-note [request]
(let [id (select-keys (-> request :path-params)[:id])]
(println (s/valid? ::idspec id))
(if (s/valid? ::idspec id)
    (assoc (http/json-response {:msg "Note Deleted successfully"}) :status 200)
   (assoc (http/json-response {:msg "Oops Unable to delete something went wrong"}) :status 400)
   )))

