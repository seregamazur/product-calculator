#!/bin/bash

consul agent -dev -client=0.0.0.0 -config-file=/consul/config/bootstrap.json &

sleep 5

consul kv put config/product-calculator/policies/discount/amount-based '{"2": 2, "5": 3, "10": 5}'
consul kv put config/product-calculator/policies/discount/percentage-based '{"2": 1.5, "5": 3, "10": 5}'

wait
