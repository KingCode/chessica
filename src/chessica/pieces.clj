(ns chessica.pieces
  "Canonical representations of pieces on the board"
  (:require [clojure.string :refer [upper-case]]))

(def pieces "kqrbnp") ;; 6 pieces

(def black-pieces pieces)
(def white-pieces (upper-case pieces) )

(def piece-idxs {:pawn 5,
                 :king 0,
                 :queen 1,
                 :rook 2,
                 :bishop 3,
                 :knight 4})

(defn build-key [piece-kw color-kw]
  (let [pfx (if (= :w color-kw)
              "white-" 
              "black-")]
    (->> piece-kw str rest (apply str) (str pfx) keyword)))

(defn build-keywords [alphabet color-kw]
  (let [color-fn (if (= :w color-kw) upper-case identity)]
    (->> piece-idxs
         (into {} (map (fn [[kw i]]
                         [(build-key kw color-kw),
                          (->> (nth alphabet i) str 
                                 color-fn 
                                 keyword)]))))))

(def piece-keywords
  (merge (build-keywords black-pieces :b)
         (build-keywords white-pieces :w)))


(def piece-strings
  (->> piece-keywords
       (into {} (map (fn [[piece kw]]
                       [piece (->> kw str rest (apply str))])))))

(def piece-chars
  (->> piece-strings
       (into {} (map (fn [[piece s]]
                       [piece (first s)])))))

(def pieces-no-color (keys piece-idxs))

(def pieces-with-color
  (keys piece-keywords))

(def catalogue
  "A map of canonical representations for all chess pieces"
  (->> pieces-with-color
       (into {} 
             (map #(vector % 
                           ((juxt piece-keywords piece-strings piece-chars) %))))))

(def piece->word
  {\p "pawn" \k "king" \q "queen" \r "rook" \b "bishop" \n "knight"
   \P "pawn" \K "king" \Q "queen" \R "rook" \B "bishop" \N "knight"

   :p "pawn" :k "king" :q "queen" :r "rook" :b "bishop" :n "knight"
   :P "pawn" :K "king" :Q "queen" :R "rook" :B "bishop" :N "knight"

   "p" "pawn" "k" "king" "q" "queen" "r" "rook" "b" "bishop" "n" "knight"
   "P" "pawn" "K" "king" "Q" "queen" "R" "rook" "B" "bishop" "N" "knight"})

