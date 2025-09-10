#!/bin/bash
# NOTE - CAN RUN AT END

# Add all worker nodes to the hosts (append)
sudo cat "10.0.0.1 cn
10.0.0.2 wn1
10.0.0.3 wn2
10.0.0.4 wn3" >> /etc/hosts

echo "Added worker nodes to /etc/hosts"
echo "Now, testing the functionality now with ssh${WORKER_NUMBER}"

echo "Check that the control node is now connected to the worker"
ssh wn{WORKER_NUMBER}