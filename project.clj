(defproject org.clojars.kingnuscodus/chessica "0.1.2-SNAPSHOT"
  :description "Utilities for assisting with chess games, position data."
  :url "https://github.com/KingCode/chessica"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :repl-options {:init-ns chessica.core}
  :deploy-repositories [["releases"  
                         {:sign-releases false :url "https://clojars.org"
                          }]
                        ["snapshots" 
                         {:sign-releases false :url "https://clojars.org"
                          :username [:env/clojars_username]
                          :password [:env/clojars_token]}]])
