#!/bin/bash
# Run this after all worker nodes have been added

for WORKER_NUMBER in {1..3}
do
    ssh-copy-id ${WORKER_NUMBER}
done

echo "All SSH keys have been copied"
