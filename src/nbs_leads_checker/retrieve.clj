(ns nbs-leads-checker.retrieve
  (:require [table.core :as table]
            [clojure.pprint :as pp])
  (:use [nbs-leads-checker.connect :as c]
        [clj-xpath.core]))


(defn print-notes[url & params]
  (def notes
      (apply c/download-response-body url params))

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
        :STATUS  (c/clean-newline ($x:text? "../STATUS" item))
       })
     ($x "/LEADS/NODE/ID" (notes-xml-doc))))

  (def details
   (map
    (fn [item]
      {:NOTEFORID (c/clean-newline ($x:text? "../NOTEFORID" item))
       :NOTE (c/clean-newline ($x:text? "." item))
       :USERSUBMIT (c/clean-newline ($x:text? "../USERSUBMIT" item))
       :DATE  (c/clean-newline ($x:text? "../DATE" item))
       })
     ($x "/LEADS/NODE/NOTE" (notes-xml-doc))))

  (table/table header :style :github-markdown)
  (table/table details :style :github-markdown))

(defn notes-info[]
  (def command-retrieve {:command "retrieve"})
  (def old-lead-id {:id "12894"})
  (def new-lead-id {:id "1000425"})

  (print-notes c/old-nbs-url c/token-for-merchant-lane command-retrieve old-lead-id)
  (print-notes c/new-nbs-url c/token-for-merchant-lane command-retrieve new-lead-id))

