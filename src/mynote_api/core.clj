(ns mynote-api.core
  (:require [io.pedestal.http :as http]
            ;importing api routes for mapping it to route table
            [mynote-api.notesroute :as myn]
            ;used for refreshing repl without exiting repl when code changed
            [clojure.tools.namespace.repl :refer [refresh]]
            ;used to accept parameters through http body
            [io.pedestal.http.body-params :refer [body-params]]
    ))
;;docker run --name=mk-mysql -p3306:3306 -v mysql-volume:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=1234 -d mysql/mysql-server:8.0.20    
(defn respond-hello [request]
  {:status 200
   :body "Hello World"})

;Route table with routes
(def routes
  #{["/" :get `respond-hello]
    ["/notes" :get myn/all-notes :route-name :get-notes]
    ["/notes" :post 
              [(body-params) myn/create-note] 
              :route-name :save-notes]
    ["/note/:id" :delete myn/delete-note :route-name :delete-a-note]          
  })


(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8890})

;; For interactive development
(defonce server (atom nil))

(defn go []
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                               ;; all origins are allowed in dev mode
                               ::http/allowed-origins {:creds true :allowed-origins (constantly true)}
                               ;; Content Security Policy (CSP) is mostly turned off in dev mode
                               ::http/secure-headers {:content-security-policy-settings {:object-src "'none'"}}
                               ::http/join? false))))
  (prn "Server started on localhost:8890")
  (prn "Enter (reset) to reload.")
  :started)

(defn halt []
  (http/stop @server))

(defn reset []
  (halt)
  (refresh :after 'mynote-api.core/go))
