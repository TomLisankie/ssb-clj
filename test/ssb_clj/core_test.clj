(ns ssb-clj.core-test
  (:require [clojure.test :refer :all]
            [ssb-clj.core :refer :all]))

(deftest test-show-user-id
  (is (= "@HELLO.ed25519" (show-user-id "HELLO"))))
