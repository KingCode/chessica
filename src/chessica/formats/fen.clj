(ns chessica.formats.fen

  "Functions for querying and writing Forsyth-Edwards Notation 
  data used in reproducing chess positions.

  See:
  https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation"
  (:require [clojure.string :refer [split join]]))

(defn ->num [s]
  (Integer/parseInt s))

(defn ->int [c]
  (when (-> c int (#(< 48 % 58)))
    (- (int c) 48)))

(defn ->pos-data [fen-pos]
  (let [rows (split fen-pos #"/")]
    (->> rows 
         (map #(->> % 
                    (reduce (fn [cols c]
                              (if-let [empties (->int c)]
                                (into cols (repeat empties nil))
                                (conj cols (-> c str keyword))))
                            []))))))

(defn ->vks [m] (->> m (map reverse) (map vec) (into {})) )
(def col-idxs (zipmap "abcdefgh" (range 8)))
(def col-fen (->vks col-idxs))
(def row-idxs (zipmap "12345678" (reverse (range 8))))
(def row-fen (->vks row-idxs))

(defn ->rc-idxs [[c r]]
  [(row-idxs r) (col-idxs c)])

(defn ->cr [[r-idx c-idx]])

(defn str->kws [s]
  (some->> s seq (mapv (comp keyword str))))

(defn str->ep [s]
  (when (not= "-" s)
    (->rc-idxs s)))

(defn str->castle [s]
  (if (not= "-" s)
    (mapv (comp keyword str) s)
    []))

(defn ->data 
  "Transforms a FEN string into a map with keys 
     :pos, an 8x8 vector of vectors of piece keywords or nil for spaces
           where each piece symbol in FEN is keywordized.
     :turn, the side to move, one of :w or :b
     :castling, a vector of up to 4 keywordized FEN symbols (:K,:Q etc)
                can be empty when no side can castle
     :en-passant, a [row col] vector of the square 'behind' the pawn just
                  having made an initial (2 squares) move if any, or nil
     :clock, an int (0 or positive) for the half-move clock
     :move, the move number in the game.
  The output map can be more easily queried and transformed."
    
[fen]
 (let [[pos turn castling ep clock move] (split fen #"\s+")]
   {:pos (->pos-data pos)
    :turn (keyword turn)
    :castling (if (= "-" castling)
                []
                (mapv (comp keyword str) castling))
    :en-passant (when (not= "-" ep) 
                  (->rc-idxs ep))
    :clock (->num clock)
    :move (->num move)}))

(defn kws->str [kws]
  ;; (println :KWS kws)
  (->> kws (sequence (comp (map str)
                           (map rest)
                           (map #(apply str %))))
       join))

(defn ->row-fen [col]
  (->> col (partition-by keyword?)
       (map (fn [field]
              (if (nil? (first field))
                (-> field count str)
                (kws->str field))))
       join))

(defn ->pos-fen [pos]
  (->> pos
       (reduce #(conj % (->row-fen %2)) [])
       (join "/")))

(defn ->fen
  "Transforms a data map (as per the output of ->data)
   into a FEN string."
[{:keys [pos turn castling en-passant clock move]}]
  ;; (let [pos (->pos-fen pos)])
  (join " " 
        [(->pos-fen pos)
         (kws->str [turn])
         (if (seq castling)
           (kws->str castling)
           "-")
         (if-let  [[r c] en-passant]
           (join [(col-fen c) (row-fen r)])
           "-")
         clock move]))

(def fen-start "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")

(def empty-board (->> (repeat 8 nil) vec (repeat 8) vec))

(defn insert [piece rc board]
  (assoc-in board rc piece))

(defn update-board* 
  ([pieces rc-idxs]
   (update-board* pieces rc-idxs empty-board))
  ([pieces rc-idxs board]
   (->> rc-idxs (map vector pieces)
        (reduce (fn [board [piece rc]]
                  (insert piece rc board))
                board))))

(defn update-board
  "Adds/remove/replace a chess piece to one or more locations on a chess board,
  where p-or-ps is a either a single piece keyword or a string of space-delimited
  piece-letters (in which case each piece is inserted at its respective location
  in locs-str)
  locs-str is a space separated string of chessboard coordinates, e.g. 'a3 b2'
  and board is an 8x8 vector of vectors of either nil or occupied squares.
  The updated board is returned. 'piece can be set to `nil` to remove the content 
  of each square in 'locs-str."
[p-or-ps locs-str board]
  (let [rc-idxs (->> #"\s+" (split locs-str)
                     (map ->rc-idxs))
        pieces (if (keyword? p-or-ps)
                 (vec (repeat (count rc-idxs) p-or-ps))
                 (str->kws p-or-ps))]
    (update-board* pieces rc-idxs board)))

(defn complete-data [other-dat-str board]
  (let [[turn castle ep clock move] (split other-dat-str #"\s+")]
    {:pos board
     :turn (keyword turn)
     :castling (str->castle castle)
     :ep (str->ep ep)
     :clock (->num clock)
     :move (->num move)}))


(defn populate 
  "Creates a FEN string from a starting board - an 8x8 vovs,
  pairs of [piece-or-pieces, locations-string] and tailing
  data (turn to move, castling state etc).
  Each pair is in a user-friendly format representing cumulative 
  board updates - see `update-board`.
  Example:

  (populate [[:p \"a7 g6 f7 e6 d5 c7 b7 a7\"] ;; black pawn structure

             [\"krrnq\" \"g8 f8 a8 e5 c4\"]   ;; black king on g8, rooks on 
                                          ;; f8 and a8, and so forth  

             [:P \"g5 f2 c2 b2 a3\"]        ;; white pawn structure

             [\"QKRRBN\" \"g3 d1 h1 a1 g1 b1\"]] ;; white pieces

             \"b KQ - 0 40\")  ;; FEN string tail: turn to play, etc.

 ;;=> \"r4rk1/ppp2p2/4p1p1/3pn1P1/2q5/P5Q1/1PP2P2/RN1K2BR b KQ - 0 40\"
"
  ([pairs completion-dat]
   (populate empty-board pairs completion-dat))
  ([board pairs completion-dat]
   (->> pairs
        (reduce (fn [board [p-or-ps locs]]
                  (update-board p-or-ps locs board))
                board)
        (complete-data completion-dat)
        ->fen)))
