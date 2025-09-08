#!/bin/bash
# NOTE - CAN RUN AT END

# Add all worker nodes to the hosts (append)
sudo cat "10.0.0.1 cn
10.0.0.2 wn1
10.0.0.3 wn2
10.0.0.4 wn3" >> /etc/hosts

