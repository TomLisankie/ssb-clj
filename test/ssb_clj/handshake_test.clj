(ns ssb-clj.handshake-test
  (:require [clojure.test :refer :all]
            [ssb-clj.identity-utils :refer :all]
            [ssb-clj.handshake :refer :all])
  (:import (org.apache.tuweni.crypto.sodium Signature)))

(deftest test-initial-message
  "Tests to make sure initial message is sent and received correctly."
  (let [server-long-term-key-pair (Signature$KeyPair/random)
        network-identifier (Bytes32/random)
        client (create-handshake-client (Signature$KeyPair/random)
                                        network-identifier
                                        (.publicKey server-long-term-key-pair))
        server (create-handshake-server server-long-term-key-pair
                                        network-identifier)
        initial-message (create-hello client)
        (read-hello server initial-message)
        (read-hello client (create-hello server))]
    (is (= (shared-secret client) (shared-secret server)))
    (is (= (shared-secret-2 client) (shared-secret-2 server)))))
