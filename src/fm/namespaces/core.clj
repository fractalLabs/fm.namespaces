(ns fm.namespaces.core
  (:use [clojure.contrib.string :only [replace-re join]]))

(defn ns->path [n]
  "de name-space.core a name_space/core.clj"
  (let [onlist (map #(replace-re #"-" "_" %)
                    (re-seq #"[^.]+"  n))]
    (str "src/" (join "/" onlist) ".clj")))

(defn path->ns
  "de name_space/core.clj a name-space.core"
  [p]
  (let [onlist (map #(replace-re #"_" "-" %)
                    (re-seq #"[^/]+" (replace-re #".clj" "" p)))]
    (join "." onlist)))

(defn fromns
  "como el ls -R src regresa mucha basura, primero separaramos los ns por directorios
  suponiendo que list es algo similar a:
  '(\"src:\" \"leiningen\" \"search\" \"search.clj\" \"src/leiningen:\" \"src/search:\" \"aaa\" \"core.clj\" \"read.clj\" \"src/search/aaa:\" \"au.clj\")"
  [list]
  (let [dir (first list)
        files (take-while #(not (re-find #":" %)) (rest list))
        namespace (replace-re #"src/?" "" (replace-re #":" "" dir))
        namespace (if (= "" namespace) "" (str namespace "."))
        namespace (path->ns namespace)
        cljfiles (map path->ns (map #(replace-re #".clj" "" %) (filter #(re-find #".clj" %) files)))]
    (map #(str namespace %) cljfiles)))

(defn separa-nss [list]
  (loop [list list
         result '( )]
    (if (empty? list) result
      (if (re-find #"src" (first list))
        (recur (rest list) (concat result (fromns list)))
        (recur (rest list) result)))))
