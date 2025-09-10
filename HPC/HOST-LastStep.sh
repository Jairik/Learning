#!/bin/bash

touch /mpi/allhosts

echo "Copying mpirun hostname to allhosts file"

# Get the name and slots and append to allhosts file
mpirun hostname >> /mpi/allhosts

echo "Running test..."
mpirun -hostfile ~/allhosts out

echo "We should now see all cores on the nodes"

exit 0