#!~/bin/bash
# Install quarto and relevant dependencies
# To run a quarto file, run quarto preview [filename] or quarto render [filename]

# Ensure system is up to date
sudo apt update
sudo apt upgrade -y

# Install prereqs (curl and gpg)
sudo apt install -y curl gpg

# Add the signing key and install
curl -fsSL https://quarto.org/download/latest/quarto-linux-amd64.deb -o /tmp/quarto.deb
sudo apt install -y /tmp/quarto.deb

# Ensure python is installed
sudo apt install -y python3 python3-venv python3-pip

# Install tex for rendering
quarto install tinytex

# Confirm version
quarto --version