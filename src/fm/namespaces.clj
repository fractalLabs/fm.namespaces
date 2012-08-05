(ns fm.namespaces 
  (:use
    fm.namespaces.core
    cj.shell))

(let [lssrc (ls "-R src")]
    (def *namespaces* (separa-nss lssrc)))

(def *filesclj* (map ns->path *namespaces*))

(defn exists-ns? 
  [name]
  (some #(= name %) *namespaces*))

(defn load-ns
  ([name]
   (do
     (load-file  (ns->path name))
     (use (symbol name))))
  ([name & others]
   (let [allns (conj others name)]
     (map load-ns allns)))
  ([]
   (map load-ns *namespaces*)))


