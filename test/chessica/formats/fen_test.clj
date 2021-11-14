(ns chessica.formats.fen-test
  (:require [chessica.formats.fen :as fen :refer [populate ->fen]] 
            [clojure.test :refer [is are deftest testing]]
            [clojure.string :refer [split]]))

(defn tail [kw]
  (->> kw str rest (apply str) (#(str % " - - 0 40"))))

(def tail-w (tail :w))
(def tail-b (tail :b))

(deftest populate-test 
  (testing "mostly position and turn accuracy"
    (are [pairs tail-or-kw exp] 
        (let [kw? (keyword? tail-or-kw) 
              act (-> (populate pairs (if kw?
                                        (tail tail-or-kw)
                                        tail-or-kw)))]
          (is (= exp 
                 (if kw?
                   (->> (split act #"\s+")
                        (take 2) (interpose " ") (apply str))
                   act))))
      nil :w 
      "8/8/8/8/8/8/8/8 w"

      [[:P "h2 g2 f2 d5 b2"]
       ["KQBRB" "g1 d1 d3 g4 h6"]
       [:p "g7 f7 d6 b4"]
       ["krqbn" "g8 f8 a8 b6 e5"]] :b
      "q4rk1/5pp1/1b1p3B/3Pn3/1p4R1/3B4/1P3PPP/3Q2K1 b"
      
      [[:p "e6 f7 g7 h7"]
       ["kr" "f6 c3"]
       [:P "h2 g4 f3"]
       ["KR" "h5 b7"]] :w
      "8/1R3ppp/4pk2/7K/6P1/2r2P2/7P/8 w"
      
      [["rnbqkbnr" "a8 b8 c8 d8 e8 f8 g8 h8"]
       [:p "a7 b7 c7 d7 e7 f7 g7 h7"]
       [:P "a2 b2 c2 d2 e2 f2 g2 h2"]
       ["RNBQKBNR" "a1 b1 c1 d1 e1 f1 g1 h1"]]
      "w KQkq - 0 1"
      "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
      
      [[:P "a2 b3 c4 d5 g2"]
       ["RKQN" "f1 h1 e3 c5"]
       [:p "a7 b4 e7 f7 g6"]
       ["qrrk" "b6 c8 f8 g7"]] :w
      "2r2r2/p3ppk1/1q4p1/2NP4/1pP5/1P2Q3/P5P1/5R1K w"
      
      [[:P "a2 b2 c2 g2 h2"]
       ["KRRQNNB" "c1 d1 h1 f2 g1 e3 d3"]
       [:p "a7 b6 d7 f7 g7 h7"]
       ["nnbrrqk" "e4 e5 b7 a8 h8 d8 e8"]]
      "w kq - 0 40"
      "r2qk2r/pb1p1ppp/1p6/4n3/4n3/3BN3/PPP2QPP/2KR2NR w kq - 0 40"
      
      [[:P "a2 b3 c4 e2"]
       [:K "f2"] [:Q "f3"] [:R "h1"] [:B "f6"]
       [:p "a7 b6 c6 d6"]
       [:k "g8"] [:q "g6"] [:r "e4 g4"]] :w
      "6k1/p7/1ppp1Bq1/8/2P1r1r1/1P3Q2/P3PK2/7R w"

      [["QRNBNPPK" "h1 g1 h3 g5 d5 h5 a2 a7"]
       [:p "d3 c4 c7 a5"]
       ["bbqkr" "c2 b2 e6 g8 b8"]] :b
      "1r4k1/K1p5/4q3/p2N2BP/2p5/3p3N/Pbb5/6RQ b"

      [["rnkrqb" "a8 d8 e8 h8 b7 d7"]
       [:p "a5 c7 c6 d6 g7 h7"]
       [:P "a3 b2 c2 f5 g2 h3"]
       ["QRBNKR" "c4 e5 g5 c3 c1 d1"]]
      "b - - 0 25"
      "r2nk2r/1qpb2pp/2pp4/p3RPB1/2Q5/P1N4P/1PP3P1/2KR4 b - - 0 25"
      
      [["rnkrqb" "a8 d8 e7 h8 b7 d7"]
       [:p "a5 c7 c6 d6 f6 g7 h7"]
       [:P "a3 b2 c2 e4 f5 g2 h3"]
       ["QRBNKR" "c4 e1 e3 c3 c1 d1"]]
      "w - - 0 23"
      "r2n3r/1qpbk1pp/2pp1p2/p4P2/2Q1P3/P1N1B2P/1PP3P1/2KRR3 w - - 0 23"
      
      [["rbnnk" "a8 c8 b7 b6 h6"] ;;Hard puzzle!
       [:p "g7 a6 c5 b4 h4"]
       [:P "a2 b3 c4 d5 e3 g2"]
       ["NNRK" "e7 e4 f1 g1"]] :w
      "r1b5/1n2N1p1/pn5k/2pP4/1pP1N2p/1P2P3/P5P1/5RK1 w")))




