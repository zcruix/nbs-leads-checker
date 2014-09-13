(ns nbs-leads-checker.retrieve
  (:require
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.string :as string]
            [table.core :as table]
            [clojure.data.zip.xml :as zip-xml]
            )
  (:use [nbs-leads-checker.connect :as connect]))

(defn lead-info[]
  (def command-retrieve {:command "retrieve"})
  (def old-lead-id {:id "12894"})
  (def zoho-lead-id {:id "1000285"})

  (println (apply connect/download-response-body connect/new-nbs-url connect/token-for-merchant-lane command-retrieve zoho-lead-id))
  (println (apply connect/download-response-body connect/old-nbs-url connect/token-for-merchant-lane command-retrieve old-lead-id)))

