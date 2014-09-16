(ns nbs-leads-checker.listall_status_only
  (:require [table.core :as table])
  (:use [nbs-leads-checker.connect :as c]
        [clj-xpath.core]))

(defn print-all-statuses[url & params]
  (apply println "The leads retrieved for" params "follows:")

  (def status-item
   (map
    (fn [item]
      {
        :ID (clean-newline ($x:text? "." item))
        :STATUS (c/clean-newline ($x:text? "../STATUS" item))
      })
     ($x "/LEADS/NODE/ID" (c/parse url params))))

  (table/table (take 10 status-item) :style :github-markdown))


(defn status-info[]

  (def command-listall-status-only {:command "listallstatusonly"})
  (def status {:thestatus ""})

  (println "All statuses old and new.")
  (print-all-statuses c/old-nbs-url c/token-for-merchant-lane command-listall-status-only)
  (print-all-statuses c/new-nbs-url c/token-for-merchant-lane command-listall-status-only))
