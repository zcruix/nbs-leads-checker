(ns nbs-leads-checker.retrieve
  (:require [table.core :as table]
            [clojure.pprint :as pp])
  (:use [nbs-leads-checker.connect :as connect]
        [clj-xpath.core]))


(defn print-notes[url & params]
  (def notes
      (apply connect/download-response-body url params))

  (def notes-xml
     (memoize (fn [] notes)))

  (def notes-xml-doc
     (memoize (fn [] (xml->doc (notes-xml)))))

  (apply println "The notes retrieved for" params "follows:")

  (def header
   (map
    (fn [item]
       {
        :ID (clean-newline ($x:text? "." item))
        :STATUS  (connect/clean-newline ($x:text? "../STATUS" item))
       })
     ($x "/LEADS/NODE/ID" (notes-xml-doc))))

  (def details
   (map
    (fn [item]
      {:NOTEFORID (connect/clean-newline ($x:text? "../NOTEFORID" item))
       :NOTE (connect/clean-newline ($x:text? "." item))
       :USERSUBMIT (connect/clean-newline ($x:text? "../USERSUBMIT" item))
       :DATE  (connect/clean-newline ($x:text? "../DATE" item))
       })
     ($x "/LEADS/NODE/NOTE" (notes-xml-doc))))

  (table/table header :style :github-markdown)
  (table/table details :style :github-markdown))

(defn notes-info[]
  (def command-retrieve {:command "retrieve"})
  (def old-lead-id {:id "12894"})
  (def new-lead-id {:id "1000425"})

  (print-notes connect/old-nbs-url connect/token-for-merchant-lane command-retrieve old-lead-id)
  (print-notes connect/new-nbs-url connect/token-for-merchant-lane command-retrieve new-lead-id))

