(ns nbs-leads-checker.listall
  (:require [clojure.zip :as zip]
            [table.core :as table])
  (:use [nbs-leads-checker.connect :as connect]))

(defn print-first-lead[url & params]
  (def ordered-tags [:ID :FIRSTNAME :LASTNAME :BUSINESS_NAME :CITY :STATE :ZIP :STATUS :CREATED])
  (def body-content (zip/xml-zip (:content (-> (apply connect/parsed-body url params) zip/down zip/right zip/node))))
  (def lead-node (first body-content))
  (table/table (connect/item-map lead-node ordered-tags) :style :github-markdown) )

(defn first-leads[]
  (def command-listall {:command "listall"})

  (println "Merchant Lane" "old - new")
  (print-first-lead  connect/old-nbs-url connect/token-for-merchant-lane command-listall)

  (println "Drew Sharma" "old - new")
  (print-first-lead  connect/old-nbs-url connect/token-for-drew-sharma command-listall)

  (println "CB PRO" "old - new")
  (print-first-lead  connect/old-nbs-url connect/token-for-checkbyphonepro command-listall))
