# task1
Marble-CORE>
en
conf t
vlan 110
  name PLANNING
  exit
vlan 111
  name FINANCE 
  exit
exit
show vlan brief

# task2
Chalk-CORE>
en
conf t
interface gi0/1
  switchport mode trunk
  no sh
  exit

interface range fa0/1-9
  switchport mode access
  switchport access vlan 110
  no sh
  exit
interface range fa0/10-19
  switchport mode access
  switchport access vlan 110
  no sh
  exit
exit
show vlan brief

# task3
Sofia-GW>
en
conf t
interface gi0/0/1
  no sh
  exit
interface gi0/0/1.110
  encapsulation dot1Q 110
  ip address 172.29.130.250 255.255.255.0
  no sh
  exit
interface gi0/0/1.111
  encapsulation dot1Q 111
  ip address 172.29.60.250 255.255.255.0
  no sh
  exit

-> перевірка: пінг з компа на компа(VLAN) і на роутер(172.29.***.250)

# task4
Sofia-GW>
en
conf t
ip dhcp excluded-address 172.29.130.250
ip dhcp excluded-address 172.29.60.250
ip dhcp pool 110
  network 172.29.130.0 255.255.255.0
  default-router 172.29.130.250
  exit
ip dhcp pool 111
  network 172.29.60.0 255.255.255.0
  default-router 172.29.60.250
  exit

-> перевірка: поставити на компʼютерах DHCP

# task5
Chalk-CORE>
en
conf t
interface range fa0/1-24
  switchport port-security
  switchport port-security maximum 2
  switchport port-security mac-address sticky
  switchport port-security violation restrict
  no sh
  exit
exit
show port-security interface fa0/1 

# task6
Sofia-GW>
en
conf t
// show cdp neighbors details (для того шоби перевірити який айпі підключений до порту)
ip route 0.0.0.0 0.0.0.0 144.171.14.157

