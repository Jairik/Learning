#!/bin/bash
# Master setup script for HPC cluster master node
# This script installs necessary packages and configures the environment

# NOTES:
# Ensure that...
# - The machine has LAN boots disabled in BIOS, and is booted in UEFI mode

# Install scripts - updating apt
sudo apt update && sudo apt upgrade -y
sudo apt install build-essential  # Install necessary compilers

# Installing OpenSSH/MPI
sudo apt install openssh-server
sudo apt install nfs-kernel-server
sudo apt install libopenmpi-dev openmpi-bin

# Generating an ed25519 key for SSH
ssh-keygen -t ed25519 -f ~/.ssh/id_ed25519 -N ""

# Installing vim
sudo apt install vim
sudo update-alternatives --set editor /usr/bin/vim.basic

echo "All installs are complete"

# Creating the MPI directory
mkdir MPI

# Adding line to end of etc/exports
sudo cat "/home/mpi/MPI *(rw,sync,no_root_squash,no_subtree_check)" >> /etc/exports

# Restarting NFS server
sudo exportfs -a
sudo service nfs-kernel-server restart

# NOTE: THIS ONE MIGHT NOW WORK
# Configuring static IP address
sudo tee /etc/netplan/01-netcfg.yaml > /dev/null <<EOL
network:
    version: 2
    ethernets:
        enp0s3:  # Replace with your network interface name
            dhcp4: no
            addresses:
                - 10.0.0.1/24
            nameservers:
                addresses: []
            routes:
                - to: default
                    via: 10.0.0.1
EOL

# Applying the netplan configuration
sudo netplan apply

# Reboot the machine to apply all changes
echo "Setup is complete. Waiting for user input to reboot the machine..."
read -p "Press Enter to reboot the machine..."
sudo systemctl reboot  # Clean reboot
