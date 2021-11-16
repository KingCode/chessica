(ns chessica.board2-test
  (:require [chessica.board2 :refer [update-board empty-board]]
            [clojure.test :refer [is are deftest testing]]))

(defn some-squares [pred] (for [i (range 0 8)
                           j (range 0 8)
                           :when (pred [i j])]
                            [i j]))

(deftest update-board-test
  (testing "basic updates"
    (are [pieces rc-idxs exp] (let [bd (update-board empty-board pieces rc-idxs)]
                                (is (->> exp
                                         (every? (fn [[p [r c]]]
                                                   (= p (get-in bd [r c]))))))
                                (is (->> (complement (set rc-idxs)) some-squares
                                         (every? #(nil? (get-in bd %))))))
      "pkrPKB" [[1 0] [0 4] [0 7]
                [7 6] [7 4] [7 5]]
      [[\p [1 0]] [\k [0 4]] [\r [0 7]]
       [\P [7 6]] [\K [7 4]] [\B [7 5]]]))

  (testing "conflict detection"
    (are [pieces rc-idxs conflicts]
        (try (update-board empty-board pieces rc-idxs 
                           :throw-on-conflict? true)
             false
             (catch Exception e
               (-> e ex-data :conflicts
                   (= conflicts))))
      "pK" [[4 4] [4 4]]
      {[4 4] #{\p \K}})))


