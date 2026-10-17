# Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
# Task 14 - a CLEAN target environment for Ansible. Requires VirtualBox + Vagrant.
#   vagrant up        (creates a fresh Ubuntu node at 192.168.56.10)
#   cd ansible && ansible-playbook -i inventory-vagrant.ini site.yml -e image_repo=<user>/table-reservation -e image_tag=1
#   vagrant destroy -f   (throw it away and repeat for a truly clean run)
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  config.vm.hostname = "reservation-node"
  config.vm.network "private_network", ip: "192.168.56.10"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = 2048
    vb.cpus = 2
  end
end
