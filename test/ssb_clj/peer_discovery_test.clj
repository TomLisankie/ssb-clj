(ns ssb-clj.peer-discovery-test
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

;; This test is going to have to wait until my relevant Tuweni pull request goes through unfortunately.
;; (deftest test-broadcast-and-listen
;;   "Tests to see if packets are being broadcast properly and listened for"
;;   (let [service-1 (create-discovery-service 18008
;;                                             18009
;;                                             "127.0.0.1"
;;                                             "127.0.0.1"
;;                                             false)
;;         service-2 (create-discovery-service 18009
;;                                             18008
;;                                             "127.0.0.1"
;;                                             "127.0.0.1"
;;                                             false)
;;         local-id (make-local-identity "10.0.0.1"
;;                                       10000
;;                                       (generate-new-identity))
;;         reference (AtomicReference.)]
;;     (start-service service-2)
;;     (append-identity-to-broadcast-list local-id)
;;     (add-listener service-2 reference)
;;     (start-service service-1)
;;     (broadcast service-1)
;;     (Thread/sleep 1000)
;;     (is (not (null? reference)))
;;     (is (= local-id (.get reference)))
;;     (stop-service service-1)
;;     (stop-service service-2)))
