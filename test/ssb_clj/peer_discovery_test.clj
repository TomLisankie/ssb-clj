(ns ssb-clj.peer-discovery-test
  (:require [clojure.test :refer :all]
            [ssb-clj.peer-discovery :refer :all]))

(deftest test-generate-udp-packet
  (let [test-packet (generate-udp-packet "127.0.0.1" 8008 public-key)]
    (is (= java.net.DatagramPacket (type test-packet)))
    (is (= (ip-string test-packet) "127.0.0.1"))
    (is (= (port test-packet) 8008))
    (is (= (public-key-in-packet test-packet) public-key))))
