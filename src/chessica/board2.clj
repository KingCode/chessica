(ns chessica.board2
  "Represents and implements a chess board position
  as a two-dimensional, row-column vector")

(def empty-board (->> (repeat 8 nil) vec (repeat 8) vec))

(defn update-board 
  "Puts pieces in squares whose coordinates are in their respective
  locations in rc-idxs. Each piece is a char (`pieces`is a string or
  or sequential of chars, without spaces) and each coordinates is a 
  row-column index vector.
  If :throw-on-conflict? is true then an exception is thrown with a 
  data map reporting conflicting pieces for the same
  square, if applicable."
  [board pieces rc-idxs & {e-on-cfs? :throw-on-conflict? :as opts}]
  (let [[board' cfs]
        (->> rc-idxs (map vector pieces)
             (reduce (fn [[board conflicts] [piece rc]]
                       [(assoc-in board rc piece),
                        (if-let [occupant (get-in board rc)]
                          (update conflicts rc 
                                  (fn [vs]
                                    (conj (or vs #{}) occupant piece)))
                          conflicts)])
                     [board {}]))]
    (if (and e-on-cfs? (not (empty? cfs)))
      (throw (ex-info "Two or more conflicting pieces" 
                      {:pieces pieces :coords rc-idxs
                       :board board'
                       :conflicts cfs}))
      board')))
