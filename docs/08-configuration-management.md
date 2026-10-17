# Tasks 13 and 14 - Configuration Management, Provisioning and Reliability

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

Tool chosen: **Ansible** (agentless, YAML, idempotent modules).

## Task 13 - Configuration specification
| Category | Requirement | Ansible task (`ansible/provision.yml`) |
|---|---|---|
| Packages | `docker.io`, `python3-docker`, `curl`, `ufw` | `apt` |
| Group / user | `reservation` system user, member of `docker` | `group`, `user` |
| Folders | `/opt/reservation` owned by `reservation` | `file` |
| Files | `/opt/reservation/app.env` (`APP_ENV`, `SERVER_PORT`) mode 0640 | `copy` |
| Ports | TCP 8080 open | `community.general.ufw` |
| Services | `docker` started and enabled | `service` |

Variables live in `ansible/group_vars_all.yml` (environment, image repo/tag, ports).

First run:
```bash
cd ansible
ansible-galaxy collection install -r requirements.yml
ansible -i inventory.ini all -m ping
ansible-playbook -i inventory.ini provision.yml --check --diff    # dry run
ansible-playbook -i inventory.ini provision.yml | tee first-run.log
```

## Task 14 - Provision, idempotency, health check, rollback
**Provision a clean node and deploy** (a throw-away Ubuntu VM is defined in `Vagrantfile`: `vagrant up`, then use `ansible/inventory-vagrant.ini`; `vagrant destroy -f` gives a truly clean node again) (use a fresh VM, a new Vagrant box, or a new WSL/Ubuntu instance):
```bash
ansible-playbook -i inventory.ini site.yml -e image_repo=<user>/table-reservation -e image_tag=1
```
**Idempotency evidence** - run the same command again; the recap must show `changed=0`:
```
PLAY RECAP ****  node1 : ok=NN changed=0 unreachable=0 failed=0
```
**Health check** - `deploy.yml` polls `http://localhost:8080/api/health` up to 15 times until it returns `UP`. Also run `curl http://<node>:8080/api/health`.

**Rollback / recovery demonstration**
1. Deploy a good release: `-e image_tag=1` (records `current_tag=1`).
2. Deploy a bad release - an image that fails its health check, e.g. build one with `SERVER_PORT` misconfigured or a tag that starts but returns an error: `-e image_tag=2-broken`.
3. The `block/rescue` in `deploy.yml` detects the failed health check, restarts the previous tag (`1`) and fails the play with a clear message. Verify with `curl .../api/health` -> `version` of the stable release.
4. Manual recovery any time: `ansible-playbook -i inventory.ini rollback.yml` (reads `/opt/reservation/previous_tag`).

## Puppet alternative (if your course requires Puppet)
```puppet
package { ['docker.io','curl']: ensure => installed }
group   { 'reservation': ensure => present }
user    { 'reservation': ensure => present, gid => 'reservation', groups => ['docker'], shell => '/usr/sbin/nologin', system => true }
file    { '/opt/reservation': ensure => directory, owner => 'reservation', group => 'reservation', mode => '0755' }
file    { '/opt/reservation/app.env': ensure => file, owner => 'reservation', mode => '0640', content => "APP_ENV=staging\nSERVER_PORT=8080\n" }
service { 'docker': ensure => running, enable => true, require => Package['docker.io'] }
```
Apply with `sudo puppet apply reservation.pp` (run twice to show idempotency).
