(ns ssb-clj.peer-discovery-test
  (:import (org.apache.tuweni.scuttlebutt.discovery ScuttlebuttLocalDiscoveryService))
  (:require [clojure.test :refer :all]
            [ssb-clj.identity-utils :refer :all]
            [ssb-clj.peer-discovery :refer :all]))

(deftest test-invalid-multicast-address
  "Tests to make sure an exception is thrown if an invalid multicast IP is given"
  (is (thrown? IllegalArgumentException (ScuttlebuttLocalDiscoveryService.
                                         nil nil 8008 "0.0.0.0" "255.255.255.255"))))

(deftest test-local-identity-as-string
  "Tests to see if local-identity creation works"
  (let [id (generate-new-identity)
        local-id (make-local-identity "0.0.0.0" 8008 id)
        local-id-string (str "net:" "0.0.0.0" ":" 8008 "~shs:" (.publicKeyAsBase64String id))]
    (is (= local-id-string (local-id-as-string local-id)))))
