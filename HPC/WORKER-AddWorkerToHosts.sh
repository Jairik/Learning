#~/bin/bash
# On worker nodes, add to /etc/hosts
echo "Enter worker number (e.g., 1 for wn1): "
read WORKER_NUMBER

sudo cat "10.0.0.1 cn
10.0.0.${WORKER_NUMBER} wn${WORKER_NUMBER}" >> /etc/hosts

echo "Added worker node wn${WORKER_NUMBER} to /etc/hosts"

# Now, edit fstab file so it can see the MPI directory
echo "Editing /etc/fstab to include MPI directory from control node..."
sudo cat "cn:/home/mpi/MPI /home/mpi/MPI nfs" >> /etc/fstab

echo "All Done! Restarting now..."
sudo shutdown -r now
