# Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
# Task 13 (Puppet alternative to ansible/provision.yml).
# Apply:  sudo puppet apply reservation.pp       (run twice: second run should change nothing)
$app_dir = '/opt/reservation'
$app_env = 'staging'

package { ['docker.io', 'curl', 'ufw']:
  ensure => installed,
}

group { 'reservation':
  ensure => present,
  system => true,
}

user { 'reservation':
  ensure  => present,
  gid     => 'reservation',
  groups  => ['docker'],
  shell   => '/usr/sbin/nologin',
  system  => true,
  require => [Group['reservation'], Package['docker.io']],
}

file { $app_dir:
  ensure  => directory,
  owner   => 'reservation',
  group   => 'reservation',
  mode    => '0755',
  require => User['reservation'],
}

file { "${app_dir}/app.env":
  ensure  => file,
  owner   => 'reservation',
  group   => 'reservation',
  mode    => '0640',
  content => "APP_ENV=${app_env}\nSERVER_PORT=8080\n",
}

service { 'docker':
  ensure  => running,
  enable  => true,
  require => Package['docker.io'],
}

exec { 'open-port-8080':
  command => '/usr/sbin/ufw allow 8080/tcp',
  unless  => '/usr/sbin/ufw status | /bin/grep -q "8080/tcp"',
  require => Package['ufw'],
}

# Run the container (image tag passed as a fact/variable in real use)
exec { 'run-reservation-container':
  command => '/usr/bin/docker run -d --name table-reservation --restart unless-stopped -p 8080:8080 --env-file /opt/reservation/app.env your-dockerhub-user/table-reservation:latest',
  unless  => '/usr/bin/docker ps --format "{{.Names}}" | /bin/grep -qx table-reservation',
  require => [Service['docker'], File["${app_dir}/app.env"]],
}
