# chessica

Utilities for assisting with chess games and position data. 

This was initially motivated by an interest in transforming a chess diagram
(without access to other data other than the side to move, as in a typical chess puzzle) into something that can be fed into a chess engine.

Assembling by hand a position/game in the [Forsyth-Edwards Notation (FEN)](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation) syntax or worse,
the [Portable Game Notation (PGN)](https://en.wikipedia.org/wiki/Portable_Game_Notation), can be extremely tedious and error prone, something utilities here are intended to help with. 

## Installing

Add the following to your dependencies:

### Leiningen/Boot

[org.clojars.kingnuscodus/chessica "0.1.0-SNAPSHOT"]

### Clojure CLI/deps.edn

org.clojars.kingnuscodus/chessica {:mvn/version "0.1.0-SNAPSHOT"}

## Usage

From a diagram position, one can enter pieces and occupied squares into 
the `chessica.formats.fen/populate` function like so:

```clojure
(require '[chessica.formats.fen :as fen])
(fen/populate [["rbnnk" "a8 c8 b7 b6 h6"]
              [:p "g7 a6 c5 b4 h4"]
              [:P "a2 b3 c4 d5 e3 g2"]
              ["NNRK" "e7 e4 f1 g1"]]
              "w - - 0 40")
;;=> "r1b5/1n2N1p1/pn5k/2pP4/1pP1N2p/1P2P3/P5P1/5RK1 w - - 0 40"
```

## Documentation

[Cljdoc API](https://cljdoc.org/d/org.clojars.kingnuscodus/chessica/0.1.0-SNAPSHOT/api/chessica.formats.fen)

## TODO

- PGN utility
- Command-line application from file input with a file format. 
- Example project interacting with a chess engine.

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
