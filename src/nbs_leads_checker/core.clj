(ns nbs-leads-checker.core
  (:require [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.string :as string]
            [table.core :as table]
            [clojure.data.zip.xml :as zip-xml]
            )
)

(defn parse [s]
   (clojure.xml/parse
     (java.io.ByteArrayInputStream. (.getBytes s))))

(defn map-form-post-params [& params]
  {:form-params (apply merge params)})

(defn retrieve-leads [url & params]
  (client/post  url (apply map-form-post-params params)) )

(defn download-response-body[url & params]
  (get-in (apply retrieve-leads url params) [:body]))

(defn parsed-body [url & params]
  (zip/xml-zip (parse (apply download-response-body url params))))

(defn reorder [amap]
 (conj {}
       (select-keys amap
                    [:ID :FIRSTNAME :LASTNAME :BUSINESS_NAME :CITY :STATE :ZIP :STATUS :CREATED])))

(defn lead-map[lead-node]
   (reorder (into {}
              (for [x lead-node]
                [(:tag x)  (string/replace (str (first (:content x))) #"\n" "")]))))

(defn print-first-lead[url & params]
  (def body-content (zip/xml-zip (:content (-> (apply parsed-body url params) zip/down zip/right zip/node))))
  (def lead-node (first body-content))
  (table/table (lead-map lead-node) :style :github-markdown) )

(defn -main[]
  (def old-nbs-url "https://www.nationalbankservices.com/api/")
  (def new-nbs-url "http://api-dev.nationalbankservices.com/leads/v1/leads/")
  (def token-for-merchant-lane {:token "392jhha93m$!%2"})
  (def token-for-drew-sharma {:token "klsjdf$*3790snj"})
  (def token-for-checkbyphonepro {:token "ujsfm938la@!as"})
  (def command-listall {:command "listall"})

  (def command-retieve {:command "retrieve"})
  (def old-nbs-id {:id "12872"})

  (println "Merchant Lane" "old - new")
  (print-first-lead  old-nbs-url token-for-merchant-lane command-listall)
  (print-first-lead  new-nbs-url token-for-merchant-lane command-listall)

  (println "Drew Sharma" "old - new")
  (print-first-lead  old-nbs-url token-for-drew-sharma command-listall)
  (print-first-lead  new-nbs-url token-for-drew-sharma command-listall)

  (println "Check by phone pro" "new - old")
  (print-first-lead  new-nbs-url token-for-checkbyphonepro command-listall)
  (print-first-lead  old-nbs-url token-for-checkbyphonepro command-listall)
 )
