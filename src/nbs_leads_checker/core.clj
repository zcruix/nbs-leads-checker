(ns nbs-leads-checker.core
  (:use [nbs-leads-checker.listall :as listall]
        [nbs-leads-checker.listall_status_only :as listallstatus]
        [nbs-leads-checker.retrieve :as retrieve])
)

(defn -main[]
  (retrieve/notes-info)
  (listall/leads-info)
  (listallstatus/status-info)
  (println "done!")
 )
