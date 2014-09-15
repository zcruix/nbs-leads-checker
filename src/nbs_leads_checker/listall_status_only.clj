(ns nbs-leads-checker.listall_status_only
  (:require [table.core :as table]
            [clojure.pprint :as pp])
  (:use [nbs-leads-checker.connect :as connect]
        [clj-xpath.core]))

(defn print-all-statuses[url & params]
  (def statuses
      (apply connect/download-response-body url params))

  (def statuses-xml
     (memoize (fn [] statuses)))

  (def statuses-xml-doc
     (memoize (fn [] (xml->doc (statuses-xml)))))

  (apply println "The leads retrieved for" params "follows:")

  (def status-item
   (map
    (fn [item]
      {
        :ID (clean-newline ($x:text? "." item))
        :STATUS (connect/clean-newline ($x:text? "../STATUS" item))
      })
     ($x "/LEADS/NODE/ID" (statuses-xml-doc))))

  (table/table (take 10 status-item) :style :github-markdown))


(defn status-info[]

  (def command-listall-status-only {:command "listallstatusonly"})
  (def status {:thestatus ""})

  (println "All statuses old and new.")
  (print-all-statuses connect/old-nbs-url connect/token-for-merchant-lane command-listall-status-only)
  (print-all-statuses connect/new-nbs-url connect/token-for-merchant-lane command-listall-status-only))