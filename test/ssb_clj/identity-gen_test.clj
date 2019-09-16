(ns ssb-clj.identity-gen-test
  (:require [clojure.test :refer :all]
            [ssb-clj.identity-gen :refer :all]))

(deftest test-show-user-id
  (is (= "@HELLO.ed25519" (show-user-id "HELLO"))))
