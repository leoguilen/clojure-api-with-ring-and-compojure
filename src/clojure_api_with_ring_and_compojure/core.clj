(ns clojure-api-with-ring-and-compojure.core
  (:require
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.util.response :refer [response]]
   [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
   [ring.middleware.params :refer [wrap-params]]))

(defroutes app-routes
  (GET "/greetings" [name] (response {:message (str "Hello, " name)}))
  (POST "/greetings" request (response (:body request)))
  (route/not-found (response {:message "Not Found"})))

(def app
  (-> app-routes 
      (wrap-params {:encoding "UTF-8"})
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))

;; (app {:uri "/greetings" :request-method :get :params {:name "John"}})
;; (app {:uri "/greetings" :request-method :post :headers {"Content-Type" "application/json"} :body {:name "Leonardo"}})

(defn -main
  []
  (let [http-port (Integer/parseInt (System/getenv "HTTP_PORT"))]
    (println "Starting server on port" http-port)
    (jetty/run-jetty app {:port http-port :join? false})))
