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

  (def xmldoc
     (memoize (fn [] (xml->doc (notes-xml)))))

  (println "The Notes Retrieved Follows:" )

  (def header
   (map
    (fn [item]
       {
        :ID (clean-newline ($x:text "." item))
        :STATUS  (connect/clean-newline ($x:text "../STATUS" item))
       })
     ($x "/LEADS/NODE/ID" (xmldoc))))

  (def details
   (map
    (fn [item]
      {:NOTEFORID (connect/clean-newline ($x:text "../NOTEFORID" item))
       :NOTE (connect/clean-newline ($x:text "." item))
       :USERSUBMIT (connect/clean-newline ($x:text "../USERSUBMIT" item))
       :DATE  (connect/clean-newline ($x:text "../DATE" item))
       })
     ($x "/LEADS/NODE/NOTE" (xmldoc))))

  (table/table header :style :github-markdown)
  (table/table details :style :github-markdown))

(defn notes-info[]
  (def command-retrieve {:command "retrieve"})
  (def old-lead-id {:id "12894"})

  (print-notes connect/old-nbs-url connect/token-for-merchant-lane command-retrieve old-lead-id))

