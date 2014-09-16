(ns nbs-leads-checker.connect
  (:require [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zip-xml]
            [clojure.string :as string]
            )
    (:use [clj-xpath.core])
)

(def old-nbs-url "https://www.nationalbankservices.com/api/")
(def new-nbs-url "http://api-dev.nationalbankservices.com/leads/v1/leads/")
(def token-for-merchant-lane {:token "392jhha93m$!%2"})
(def token-for-drew-sharma {:token "klsjdf$*3790snj"})
(def token-for-checkbyphonepro {:token "ujsfm938la@!as"})

(defn clean-newline[item]
  (if
    (not (string/blank? item))
      (do
        (string/replace item #"\n" ""))))


(defn map-form-post-params [& params]
  {:form-params (apply merge params)})

(defn retrieve-leads [url & params]
  (client/post  url (apply map-form-post-params params)) )

(defn download-response-body[url & params]
  (get-in (apply retrieve-leads url params) [:body]))

(defn asort [amap order]
 (conj {} (select-keys amap order)))

(defn parse [url params]
  (def items
      (apply download-response-body url params))

  (def items-xml
     (memoize (fn [] items)))

  (def items-xml-doc (memoize (fn [] (xml->doc (items-xml)))))

  (items-xml-doc))
