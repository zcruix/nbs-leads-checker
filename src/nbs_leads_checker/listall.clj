(ns nbs-leads-checker.listall
  (:require
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.string :as string]
            [table.core :as table]
            [clojure.data.zip.xml :as zip-xml]
            )
  (:use [nbs-leads-checker.connect :as connect])
)

(defn reorder [amap]
 (conj {}
       (select-keys amap
                    [:ID :FIRSTNAME :LASTNAME :BUSINESS_NAME :CITY :STATE :ZIP :STATUS :CREATED])))

(defn lead-map[lead-node]
   (reorder (into {}
              (for [x lead-node]
                [(:tag x)  (string/replace (str (first (:content x))) #"\n" "")]))))

(defn print-first-lead[url & params]
  (def body-content (zip/xml-zip (:content (-> (apply connect/parsed-body url params) zip/down zip/right zip/node))))
  (def lead-node (first body-content))
  (table/table (lead-map lead-node) :style :github-markdown) )

(defn first-leads[]
  (def command-listall {:command "listall"})

  (println "Merchant Lane" "old - new")
  (print-first-lead  connect/old-nbs-url connect/token-for-merchant-lane command-listall)
  (print-first-lead  connect/new-nbs-url connect/token-for-merchant-lane command-listall)

  (println "Drew Sharma" "old - new")
  (print-first-lead  connect/old-nbs-url connect/token-for-drew-sharma command-listall)
  (print-first-lead  connect/new-nbs-url connect/token-for-drew-sharma command-listall)

  (println "Check by phone pro" "new - old")
  (print-first-lead  connect/new-nbs-url connect/token-for-checkbyphonepro command-listall)
  (print-first-lead  connect/old-nbs-url connect/token-for-checkbyphonepro command-listall)
 )
