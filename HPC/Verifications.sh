#!/bin/bash
# Verification script for HPC cluster nodes

# Verify the ip
echo "Verifying static IP configuration..."
ip a
echo "If you see something like inet 10.0.0.1/24 brd 10.0.0.255 scope global noprefixroute ..., correctly configured"

# Verify ssh
echo "Verifying SSH configuration..."
sudo systemctl status ssh
echo "If you see something like ssh.service - OpenBSD Secure Shell server, it is correctly configured"
echo "if that fails, try sudo systemctl status ssh"

echo "Verifications complete"