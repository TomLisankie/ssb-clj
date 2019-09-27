(ns ssb-clj.peer-discovery-test
  (:import (org.apache.tuweni.scuttlebutt.discovery ScuttlebuttLocalDiscoveryService))
  (:require [clojure.test :refer :all]
            [ssb-clj.identity-utils :refer :all]
            [ssb-clj.peer-discovery :refer :all]))

(deftest test-invalid-multicast-address
  "Tests to make sure an exception is thrown if an invalid multicast IP is given"
  (is (thrown? IllegalArgumentException (create-discovery-service
                                         8008 "0.0.0.0" "255.255.255.255"))))

(deftest test-local-identity-as-string
  "Tests to see if local-identity creation works"
  (let [id (generate-new-identity)
        local-id (make-local-identity "0.0.0.0" 8008 id)
        local-id-string (str "net:" "0.0.0.0" ":" 8008 "~shs:" (.publicKeyAsBase64String id))]
    (is (= local-id-string (local-id-as-string local-id)))))

(deftest test-start-and-stop
  "Tests starting and stopping of a service"
  (let [service (create-discovery-service 8008
                                          "0.0.0.0"
                                          "233.0.10.0")]
    (start-service service)
    (stop-service service)))

(deftest test-start-and-extra-start
  "Starts a service and then starts it again to make sure the second doesn't do anything"
  (let [service (create-discovery-service 8008
                                          "0.0.0.0"
                                          "233.0.10.0")]
    (start-service service)
    (start-service service)
    (stop-service service)))

(deftest test-stop-first
  "Makes sure that running stop before a service started doesn't break"
  (let [service (create-discovery-service 8008
                                          "0.0.0.0"
                                          "233.0.10.0")]
    (stop-service service)
    (start-service service)
    (stop-service service)))
