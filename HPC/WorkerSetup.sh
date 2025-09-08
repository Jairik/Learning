#!/bin/bash
# Setup script for HPC cluster worker nodes
# Ensure that automatic wakes is disabled, and restart on fail to power off
# Ensure that the system is set for keyboard-less operation. Also boot in UEFI mode

echo "Ensure that:"
echo "  All welcome screens are disabled"
echo "  Turn off all power savers and screen savers. Turn off sleep"
echo "  Set power button to shutdown down the computer"

# Install scripts - updating apt
sudo apt update && sudo apt upgrade -y
sudo apt install build-essential  # Install necessary compilers

# Installing OpenSSH/MPI
sudo apt install openssh-server
sudo apt install nfs-common
sudo apt install libopenmpi-dev openmpi-bin

# Generating MPI directory
mkdir MPI

# Getting the worker number
read -p "Enter worker number (e.g., 1 for wn1): " WORKER_NUMBER

# Configure static IP settings for worker nodes
echo "Configuring static IP settings..."
cat <<EOF | sudo tee /etc/netplan/01-netcfg.yaml > /dev/null
network:
    version: 2
    ethernets:
        eth0:
            dhcp4: no
            addresses:
                - 10.0.0.${WORKER_NUMBER}/24
            gateway4: 10.0.0.1
            nameservers:
                addresses: []
EOF

sudo netplan apply
echo "Static IP configuration applied. Please replace 'x' with the appropriate value for each worker node."

echo "Setup is complete. Waiting for user input to reboot the machine..."
read -p "Press Enter to reboot the machine..."
sudo systemctl reboot  # Clean reboot