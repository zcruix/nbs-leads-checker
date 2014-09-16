(ns nbs-leads-checker.listall
  (:require [table.core :as table])
  (:use [nbs-leads-checker.connect :as c]
        [clj-xpath.core]))

(defn print-leads[url & params]
  (apply println "The leads retrieved for" params "follows:")

  (def lead-items
   (map
    (fn [item]
      (c/asort {:ID (clean-newline ($x:text? "." item))
                :FIRSTNAME (c/clean-newline ($x:text? "../FIRSTNAME" item))
                :LASTNAME (c/clean-newline ($x:text? "../LASTNAME" item))
                :BUSINESS_NAME (c/clean-newline ($x:text? "../BUSINESS_NAME" item))
                :CITY (c/clean-newline ($x:text? "../CITY" item))
                :STATE (c/clean-newline ($x:text? "../STATE" item))
                :ZIP (c/clean-newline ($x:text? "../ZIP" item))
                :STATUS (c/clean-newline ($x:text? "../STATUS" item))
                :CREATED (c/clean-newline ($x:text? "../CREATED" item))}
               [:ID :FIRSTNAME :LASTNAME :BUSINESS_NAME :CITY :STATE :ZIP :STATUS :CREATED]))
    ($x "/LEADS/NODE/ID" (c/parse url params))))

  (table/table (take 5 lead-items) :style :github-markdown))

(defn leads-info[]
  (def command-listall {:command "listall"})

  (println "Merchant Lane" "old - new")
  (print-leads  c/old-nbs-url c/token-for-merchant-lane command-listall)
  (print-leads  c/new-nbs-url c/token-for-merchant-lane command-listall)

  (println "Drew Sharma" "old - new")
  (print-leads  c/old-nbs-url c/token-for-drew-sharma command-listall)
  (print-leads  c/new-nbs-url c/token-for-drew-sharma command-listall)

  (println "CB PRO" "old - new")
  (print-leads  c/new-nbs-url c/token-for-checkbyphonepro command-listall)
  (print-leads  c/old-nbs-url c/token-for-checkbyphonepro command-listall))
