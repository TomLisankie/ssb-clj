(ns ssb-clj.handshake-test
  (:require [clojure.test :refer :all]
            [ssb-clj.identity-utils :refer :all]
            [ssb-clj.handshake :refer :all])
  (:import (org.apache.tuweni.crypto.sodium Signature)))

(deftest test-initial-message
  "Tests to make sure initial message is sent and received correctly."
  (let [server-long-term-key-pair (random-key-pair)
        network-identifier (Bytes32/random)
        client (create-handshake-client (random-key-pair)
                                        network-identifier
                                        (.publicKey server-long-term-key-pair))
        server (create-handshake-server server-long-term-key-pair
                                        network-identifier)
        initial-message (create-hello client)
        (read-hello server initial-message)
        (read-hello client (create-hello server))]
    (is (= (shared-secret client) (shared-secret server)))
    (is (= (shared-secret-2 client) (shared-secret-2 server)))))

(deftest test-initial-message-different-network-identifier
  (let [server-long-term-key-pair (random-key-pair)
        client (create-handshake-client (random-key-pair)
                                        (Bytes32/random)
                                        (.publicKey server-long-term-key-pair))
        server (create-handshake-server server-long-term-key-pair
                                        (Bytes32/random))
        initial-message (create-hello client)]
    (is (thrown? HandshakeException (read-hello server initial-message)))))

(deftest test-identity-message-exchanged
  "Tests whether or not identity message was exchanged properly"
  (let [client-long-term-key-pair (random-key-pair)
        server-long-term-key-pair (random-key-pair)
        network-identifier (Bytes32/random)
        client (create-handshake-client client-long-term-key-pair
                                        network-identifier
                                        (.publicKey server-long-term-key-pair))
        server (create-handshake-server server-long-term-key-pair
                                        network-identifier)
        initial-message (create-hello client)]
    (read-hello server initial-message)
    (read-hello client (create-hello server))
    (read-identity-message server (create-identity-message client))
    (is (= (.publicKey client-long-term-key-pair) (client-long-term-public-key server)))))
