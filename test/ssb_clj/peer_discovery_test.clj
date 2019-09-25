(ns ssb-clj.identity-utils-test
  (:require [clojure.test :refer :all]
            [ssb-clj.peer_discovery :refer :all]))

(deftest test-invalid-multicast-address
  "Tests to make sure an exception is thrown if an invalid multicast IP is given"
  (is (thrown? IllegalArgumentException (ScuttlebuttLocalDiscoveryService.
                                         nil nil 8008 "0.0.0.0" "255.255.255.255")))

