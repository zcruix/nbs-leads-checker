(ns nbs-leads-checker.connect
  (:require [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zip-xml]
            [clojure.string :as string]
            )
)

(def old-nbs-url "https://www.nationalbankservices.com/api/")
(def new-nbs-url "http://api-dev.nationalbankservices.com/leads/v1/leads/")
(def token-for-merchant-lane {:token "392jhha93m$!%2"})
(def token-for-drew-sharma {:token "klsjdf$*3790snj"})
(def token-for-checkbyphonepro {:token "ujsfm938la@!as"})

(defn clean-newline[item]
    (string/replace item #"\n" ""))

(defn debomify
  [^String line]
  (let [bom "\uFEFF"]
    (if (.startsWith line bom)
      (.substring line 1)
      line)))

(defn parse [s]
   (clojure.xml/parse
     (java.io.ByteArrayInputStream. (.getBytes s))))

(defn map-form-post-params [& params]
  {:form-params (apply merge params)})

(defn retrieve-leads [url & params]
  (client/post  url (apply map-form-post-params params)) )

(defn download-response-body[url & params]
  (get-in (apply retrieve-leads url params) [:body]))

(defn parsed-body [url & params]
  (zip/xml-zip (parse (debomify (apply download-response-body url params)))))

(defn reorder [amap & args]
 (conj {}
       (apply select-keys amap args)))

(defn item-map[item-node & ordered-tags]
   (apply reorder (into {}
              (for [x item-node]
                [(:tag x)  (clean-newline (str (first (:content x))))]))
                  ordered-tags))

