(ns nbs-leads-checker.core
  (:use [nbs-leads-checker.listall :as listall]
        [nbs-leads-checker.retrieve :as retrieve])
)

(defn -main[]
  (retrieve/lead-info)
  (listall/first-leads)
 )
