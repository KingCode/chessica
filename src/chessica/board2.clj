(ns chessica.board2
  "Represents and implements a chess board position
  as a two-dimensional, row-column vector")


(def empty-board (->> (repeat 8 nil) vec (repeat 8) vec))

(defn update-board 
  ([pieces rc-idxs]
   (update-board pieces rc-idxs empty-board))
  ([pieces rc-idxs board]
   (->> rc-idxs (map vector pieces)
        (reduce (fn [board [piece rc]]
                  (assoc-in board rc piece))
                board))))
