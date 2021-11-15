(ns chessica.coordinates
  "Translates chess square coordinates between cartesian, row-column indexes 
  and chess board rank-file formats.
  
  Note the convention of using the 8th rank of the chess board as the first
  row of the cartesian representation.")


(defn ->vks [m] (->> m (map reverse) (map vec) (into {})) )

(defn flip [m]
  (-> {} (into (comp (map reverse) 
                     (map vec))
               m)))

(def ->col-idx  ;; formerly col-idxs
  "Function translating a chess file letter character 
  into a cartesian, zero-based column index, 
  e.g. \\a -> 0"
  (zipmap "abcdefgh" (range 8)))

(def ->file ;; formerly col-fen 
  "Function translating a cartesian column index into a 
  chess file letter character, e.g. 0 -> \\a"
  (flip ->col-idx))


(defn ->file-str
  "Function translating a cartesian column index into a 
  chess file letter character as a string, 
  e.g. 0 -> \"a\"."
 [col-idx]
  (str (->file col-idx)))


(def ->row-idx ;;formerly row-idxs 
  "Function translating a (bottom up) chess rank into 
  a top down, zero-based row index,
  e.g. rank 8 -> 0."
  (zipmap "12345678" (reverse (range 8))))


(def ->rank-char ;; formerly row-fen 
  "Function translating a top-down, zero-based 
  cartesian index into a chess rank character, 
  e.g. row 7 -> \\1."
  (flip ->row-idx))


(defn ->rank-str
  "Function translating a top-down, zero-based 
  cartesian index into a chess rank character 
  as a string, e.g. row 7 -> \"1\"."
  [row]
  (-> row ->rank-char str))


(def char-zero-ascii 48)

(defn ->rank 
  "Function translating a top-down, zero-based 
  cartesian index into a chess rank integer, 
  e.g. row 7 -> 1."
[row-idx]
  (-> row-idx ->rank-char int (- char-zero-ascii)))


(defn ->row-col-idxs  ;; formerly ->rc-idxs 
  "Converts a rank-file string or char tuple, into
  a cartiesian [row column] vector of indexes."
  [[file rank]]
  [(->row-idx rank), (->col-idx file)])


