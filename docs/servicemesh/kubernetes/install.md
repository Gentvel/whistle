---
title: 【Kubernetes】 Kubernetes安装
date: 2020-08-23
sidebar: auto
prev: false
next: false
---

## 一、Centos7 升级内核版本
### 1.1 查看当前内核版本
```shell
uname -r
#3.10.0-514.el7.x86_64

uname -a
#Linux k8s-leader 3.10.0-514.el7.x86_64 #1 SMP Tue Nov 22 16:42:41 UTC 2016 x86_64 x86_64 x86_64 GNU/Linux

cat /etc/redhat-release 
#CentOS Linux release 7.3.1611 (Core) 
```
### 1.2 升级内核
更新yum源仓库
```shell
yum -y update
```
启用 ELRepo 仓库
ELRepo 仓库是基于社区的用于企业级 Linux 仓库，提供对 RedHat Enterprise (RHEL) 和 其他基于 RHEL的 Linux 发行版（CentOS、Scientific、Fedora 等）的支持。
ELRepo 聚焦于和硬件相关的软件包，包括文件系统驱动、显卡驱动、网络驱动、声卡驱动和摄像头驱动等。

#导入ELRepo仓库的公共密钥
```shell
rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
#安装ELRepo仓库的yum源

rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-3.el7.elrepo.noarch.rpm
```
### 1.3 查看可用的系统内核包
可以看到4.4和4.18两个版本

```shell
yum --disablerepo="*" --enablerepo="elrepo-kernel" list available
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
 * elrepo-kernel: mirrors.tuna.tsinghua.edu.cn
elrepo-kernel                                                                                                                                                                 | 2.9 kB  00:00:00     
elrepo-kernel/primary_db                                                                                                                                                      | 1.8 MB  00:00:03     
Available Packages
elrepo-release.noarch                                   7.0-5.el7.elrepo                           elrepo-kernel
kernel-lt.x86_64                                        4.4.233-1.el7.elrepo                       elrepo-kernel
kernel-lt-devel.x86_64                                  4.4.233-1.el7.elrepo                       elrepo-kernel
kernel-lt-doc.noarch                                    4.4.233-1.el7.elrepo                       elrepo-kernel
kernel-lt-headers.x86_64                                4.4.233-1.el7.elrepo                       elrepo-kernel
kernel-lt-tools.x86_64                                  4.4.233-1.el7.elrepo                       elrepo-kernel
kernel-lt-tools-libs.x86_64                             4.4.233-1.el7.elrepo                       elrepo-kernel
kernel-lt-tools-libs-devel.x86_64                       4.4.233-1.el7.elrepo                       elrepo-kernel
kernel-ml.x86_64                                        5.8.3-1.el7.elrepo                         elrepo-kernel
kernel-ml-devel.x86_64                                  5.8.3-1.el7.elrepo                         elrepo-kernel
kernel-ml-doc.noarch                                    5.8.3-1.el7.elrepo                         elrepo-kernel
kernel-ml-headers.x86_64                                5.8.3-1.el7.elrepo                         elrepo-kernel
kernel-ml-tools.x86_64                                  5.8.3-1.el7.elrepo                         elrepo-kernel
kernel-ml-tools-libs.x86_64                             5.8.3-1.el7.elrepo                         elrepo-kernel
kernel-ml-tools-libs-devel.x86_64                       5.8.3-1.el7.elrepo                         elrepo-kernel
perf.x86_64                                             5.8.3-1.el7.elrepo                         elrepo-kernel
python-perf.x86_64                                      5.8.3-1.el7.elrepo                         elrepo-kernel
```
### 1.4 安装最新版本内核
```shell
yum --enablerepo=elrepo-kernel install kernel-ml
# --enablerepo 选项开启 CentOS 系统上的指定仓库。默认开启的是 elrepo，这里用 elrepo-kernel 替换。
```
### 1.5 设置 grub2
内核安装好后，需要设置为默认启动选项并重启后才会生效

查看系统上的所有可用内核：
```shell
sudo awk -F\' '$1=="menuentry " {print i++ " : " $2}' /etc/grub2.cfg
#0 : CentOS Linux (4.18.7-1.el7.elrepo.x86_64) 7 (Core)
#1 : CentOS Linux (3.10.0-862.11.6.el7.x86_64) 7 (Core)
#2 : CentOS Linux (3.10.0-514.el7.x86_64) 7 (Core)
#3 : CentOS Linux (0-rescue-063ec330caa04d4baae54c6902c62e54) 7 (Core)
```
设置新的内核为grub2的默认版本
服务器上存在4 个内核，我们要使用 4.18 这个版本，可以通过 grub2-set-default 0 命令或编辑 /etc/default/grub 文件来设置

#### 方法1、通过 grub2-set-default 0 命令设置
其中 0 是上面查询出来的可用内核
```shell
grub2-set-default 0
```
#### 方法2、编辑 /etc/default/grub 文件
设置 GRUB_DEFAULT=0，通过上面查询显示的编号为 0 的内核作为默认内核：

```shell
vim /etc/default/grub

GRUB_TIMEOUT=5
GRUB_DISTRIBUTOR="$(sed 's, release .*$,,g' /etc/system-release)"
GRUB_DEFAULT=0
GRUB_DISABLE_SUBMENU=true
GRUB_TERMINAL_OUTPUT="console"
GRUB_CMDLINE_LINUX="crashkernel=auto rd.lvm.lv=cl/root rhgb quiet"
GRUB_DISABLE_RECOVERY="true"
```
生成 grub 配置文件并重启
```shell
grub2-mkconfig -o /boot/grub2/grub.cfg
Generating grub configuration file ...
Found linux image: /boot/vmlinuz-4.18.7-1.el7.elrepo.x86_64
Found initrd image: /boot/initramfs-4.18.7-1.el7.elrepo.x86_64.img
Found linux image: /boot/vmlinuz-3.10.0-862.11.6.el7.x86_64
Found initrd image: /boot/initramfs-3.10.0-862.11.6.el7.x86_64.img
Found linux image: /boot/vmlinuz-3.10.0-514.el7.x86_64
Found initrd image: /boot/initramfs-3.10.0-514.el7.x86_64.img
Found linux image: /boot/vmlinuz-0-rescue-063ec330caa04d4baae54c6902c62e54
Found initrd image: /boot/initramfs-0-rescue-063ec330caa04d4baae54c6902c62e54.img
done

reboot
```
### 1.6 验证
reboot后验证
```shell
uname -r
#4.18.7-1.el7.elrepo.x86_64
```
### 1.7 删除旧内核（可选）
查看系统中全部的内核：
```shell
rpm -qa | grep kernel
#kernel-3.10.0-514.el7.x86_64
#kernel-ml-4.18.7-1.el7.elrepo.x86_64
#kernel-tools-libs-3.10.0-862.11.6.el7.x86_64
#kernel-tools-3.10.0-862.11.6.el7.x86_64
#kernel-3.10.0-862.11.6.el7.x86_64
```
#### 方法1、yum remove 删除旧内核的 RPM 包
```shell
yum remove kernel-3.10.0-514.el7.x86_64 \
kernel-tools-libs-3.10.0-862.11.6.el7.x86_64 \
kernel-tools-3.10.0-862.11.6.el7.x86_64 \
kernel-3.10.0-862.11.6.el7.x86_64
```
#### 方法2、yum-utils 工具
如果安装的内核不多于 3 个，yum-utils 工具不会删除任何一个。只有在安装的内核大于 3 个时，才会自动删除旧内核。

安装yum-utils
```shell
yum install yum-utils
#删除旧版本　　
package-cleanup --oldkernels
```
 

## 二、安装 Kubernetes 集群

### 2.1 检查 centos / hostname
```shell
# 在 master 节点和 worker 节点都要执行
cat /etc/redhat-release

# 此处 hostname 的输出将会是该机器在 Kubernetes 集群中的节点名字
# 不能使用 localhost 作为节点的名字
hostname

# 请使用 lscpu 命令，核对 CPU 信息
# Architecture: x86_64    本安装文档不支持 arm 架构
# CPU(s):       2         CPU 内核数量不能低于 2
lscpu
```

#### 2.1.1 操作系统兼容性

CentOS 版本	|本文档是否兼容|备注
-|-|-
7.8	|😄	|已验证
7.7	|😄	|已验证
7.6	|😄	|已验证
7.5	|😞	|已证实会出现 kubelet 无法启动的问题
7.4	|😞	|已证实会出现 kubelet 无法启动的问题
7.3	|😞	|已证实会出现 kubelet 无法启动的问题
7.2	|😞	|已证实会出现 kubelet 无法启动的问题




#### 2.1.2 修改 hostname

如果您需要修改 hostname，可执行如下指令：
```shell 
# 修改 hostname
hostnamectl set-hostname your-new-host-name
# 查看修改结果
hostnamectl status
# 设置 hostname 解析
echo "127.0.0.1   $(hostname)" >> /etc/hosts
```
#### 2.1.3 检查网络
在所有节点执行命令
```shell
[root@k8s-leader ~]$ ip route show
#default via 192.168.83.1 dev ens33 proto static metric 100 
#172.17.0.0/16 dev docker0 proto kernel scope link src 172.17.0.1 linkdown 
#192.168.83.0/24 dev ens33 proto kernel scope link src 192.168.83.11 metric 100 

[root@k8s-leader ~]$ ip address
#1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
#    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
#    inet 127.0.0.1/8 scope host lo
#       valid_lft forever preferred_lft forever
#    inet6 ::1/128 scope host 
#       valid_lft forever preferred_lft forever
#2: ens33: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
#    link/ether 00:0c:29:da:6c:65 brd ff:ff:ff:ff:ff:ff
#    inet 192.168.83.11/24 brd 192.168.83.255 scope global noprefixroute ens33
#       valid_lft forever preferred_lft forever
#    inet6 fe80::2402:dfd2:eab2:573/64 scope link noprefixroute 
#       valid_lft forever preferred_lft forever

```

:::tip kubelet使用的IP地址

`ip route show `命令中，可以知道机器的默认网卡，通常是 eth0，如 default via 172.21.0.23 dev eth0 如果vmware环境下一般为ens33  
`ip address` 命令中，可显示默认网卡的 IP 地址，Kubernetes 将使用此 IP 地址与集群内的其他节点通信，如 172.17.216.80  
所有节点上 Kubernetes 所使用的 IP 地址必须可以互通（无需 NAT 映射、无安全组或防火墙隔离）  
:::

###  2.2 安装docker及kubelet

请认真核对如下选项，7个都符合再安装。

😄 我的任意节点 centos 版本为 7.6 / 7.7 或 7.8  
😄 我的任意节点 CPU 内核数量大于等于 2，且内存大于等于 4G  
😄 我的任意节点 hostname 不是 localhost，且不包含下划线、小数点、大写字母  
😄 我的任意节点都有固定的内网 IP 地址  
😄 我的任意节点都只有一个网卡，如果有特殊目的，可以在完成 K8S 安装后再增加新的网卡  
😄 我的任意节点上 Kubelet使用的 IP 地址 可互通（无需 NAT 映射即可相互访问），且没有防火墙、安全组隔离  
😄 我的任意节点不会直接使用 docker run 或 docker-compose 运行容器  


#### 2.2.1 快速安装（推荐）
docker hub 镜像请根据自己网络的情况任选一个

- 第四行为腾讯云 docker hub 镜像
- 第六行为DaoCloud docker hub 镜像
- 第八行为华为云 docker hub 镜像
- 第十行为阿里云 docker hub 镜像
```shell
# 在 leader 节点和 worker 节点都要执行
# 最后一个参数 1.18.6 用于指定 kubenetes 版本，支持所有 1.18.x 版本的安装
# 腾讯云 docker hub 镜像
# export REGISTRY_MIRROR="https://mirror.ccs.tencentyun.com"
# DaoCloud 镜像
# export REGISTRY_MIRROR="http://f1361db2.m.daocloud.io"
# 华为云镜像
# export REGISTRY_MIRROR="https://05f073ad3c0010ea0f4bc00b7105ec20.mirror.swr.myhuaweicloud.com"
# 阿里云 docker hub 镜像
# export REGISTRY_MIRROR=https://registry.cn-hangzhou.aliyuncs.com
curl -sSL https://kuboard.cn/install-script/v1.18.x/install_kubelet.sh | sh -s 1.18.8
```

#### 2.2.2 手动安装
docker hub 镜像请根据自己网络的情况任选一个

- 第四行为腾讯云 docker hub 镜像
- 第六行为DaoCloud docker hub 镜像
- 第八行为阿里云 docker hub 镜像
```shell
# 在 leader 节点和 worker 节点都要执行
# 最后一个参数 1.18.6 用于指定 kubenetes 版本，支持所有 1.18.x 版本的安装
# 腾讯云 docker hub 镜像
# export REGISTRY_MIRROR="https://mirror.ccs.tencentyun.com"
# DaoCloud 镜像
# export REGISTRY_MIRROR="http://f1361db2.m.daocloud.io"
# 阿里云 docker hub 镜像
export REGISTRY_MIRROR=https://registry.cn-hangzhou.aliyuncs.com
```

```shell
#!/bin/bash

# 在 leader 节点和 worker 节点都要执行

# 安装 docker
# 参考文档如下
# https://docs.docker.com/install/linux/docker-ce/centos/ 
# https://docs.docker.com/install/linux/linux-postinstall/

# 卸载旧版本
yum remove -y docker \
docker-client \
docker-client-latest \
docker-ce-cli \
docker-common \
docker-latest \
docker-latest-logrotate \
docker-logrotate \
docker-selinux \
docker-engine-selinux \
docker-engine

# 设置 yum repository
yum install -y yum-utils \
device-mapper-persistent-data \
lvm2
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 安装并启动 docker
yum install -y docker-ce-19.03.8 docker-ce-cli-19.03.8 containerd.io
systemctl enable docker
systemctl start docker

# 安装 nfs-utils
# 必须先安装 nfs-utils 才能挂载 nfs 网络存储
yum install -y nfs-utils
yum install -y wget

# 关闭 防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭 SeLinux
setenforce 0
sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config

# 关闭 swap
swapoff -a
yes | cp /etc/fstab /etc/fstab_bak
cat /etc/fstab_bak |grep -v swap > /etc/fstab

# 修改 /etc/sysctl.conf
# 如果有配置，则修改
sed -i "s#^net.ipv4.ip_forward.*#net.ipv4.ip_forward=1#g"  /etc/sysctl.conf
sed -i "s#^net.bridge.bridge-nf-call-ip6tables.*#net.bridge.bridge-nf-call-ip6tables=1#g"  /etc/sysctl.conf
sed -i "s#^net.bridge.bridge-nf-call-iptables.*#net.bridge.bridge-nf-call-iptables=1#g"  /etc/sysctl.conf
sed -i "s#^net.ipv6.conf.all.disable_ipv6.*#net.ipv6.conf.all.disable_ipv6=1#g"  /etc/sysctl.conf
sed -i "s#^net.ipv6.conf.default.disable_ipv6.*#net.ipv6.conf.default.disable_ipv6=1#g"  /etc/sysctl.conf
sed -i "s#^net.ipv6.conf.lo.disable_ipv6.*#net.ipv6.conf.lo.disable_ipv6=1#g"  /etc/sysctl.conf
sed -i "s#^net.ipv6.conf.all.forwarding.*#net.ipv6.conf.all.forwarding=1#g"  /etc/sysctl.conf
# 可能没有，追加
echo "net.ipv4.ip_forward = 1" >> /etc/sysctl.conf
echo "net.bridge.bridge-nf-call-ip6tables = 1" >> /etc/sysctl.conf
echo "net.bridge.bridge-nf-call-iptables = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.all.disable_ipv6 = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.default.disable_ipv6 = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.lo.disable_ipv6 = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.all.forwarding = 1"  >> /etc/sysctl.conf
# 执行命令以应用
sysctl -p

# 配置K8S的yum源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
       http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

# 卸载旧版本
yum remove -y kubelet kubeadm kubectl

# 安装kubelet、kubeadm、kubectl
# 将 ${1} 替换为 kubernetes 版本号，例如 1.17.2
yum install -y kubelet-${1} kubeadm-${1} kubectl-${1}

# 修改docker Cgroup Driver为systemd
# # 将/usr/lib/systemd/system/docker.service文件中的这一行 ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
# # 修改为 ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --exec-opt native.cgroupdriver=systemd
# 如果不修改，在添加 worker 节点时可能会碰到如下错误
# [WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recommended driver is "systemd". 
# Please follow the guide at https://kubernetes.io/docs/setup/cri/
sed -i "s#^ExecStart=/usr/bin/dockerd.*#ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --exec-opt native.cgroupdriver=systemd#g" /usr/lib/systemd/system/docker.service

# 设置 docker 镜像，提高 docker 镜像下载速度和稳定性
# 如果您访问 https://hub.docker.io 速度非常稳定，亦可以跳过这个步骤
curl -sSL https://kuboard.cn/install-script/set_mirror.sh | sh -s ${REGISTRY_MIRROR}

# 重启 docker，并启动 kubelet
systemctl daemon-reload
systemctl restart docker
systemctl enable kubelet && systemctl start kubelet

docker version
```

:::warning 注意

如果此时执行 service status kubelet 命令，将得到 kubelet 启动失败的错误提示，请忽略此错误，因为必须完成后续步骤中 kubeadm init 的操作，kubelet 才能正常启动
:::

验证
```shell
--------请检查下面输出结果中的 Registry Mirrors 是否已经修改过来-------- 
docker info
Client:
 Debug Mode: false

Server:
 Containers: 0
  Running: 0
  Paused: 0
  Stopped: 0
 Images: 0
 Server Version: 19.03.8
 Storage Driver: overlay2
  Backing Filesystem: <unknown>
  Supports d_type: true
  Native Overlay Diff: true
 Logging Driver: json-file
 Cgroup Driver: systemd
 Plugins:
  Volume: local
  Network: bridge host ipvlan macvlan null overlay
  Log: awslogs fluentd gcplogs gelf journald json-file local logentries splunk syslog
 Swarm: inactive
 Runtimes: runc
 Default Runtime: runc
 Init Binary: docker-init
 containerd version: 7ad184331fa3e55e52b890ea95e65ba581ae3429
 runc version: dc9208a3303feef5b3839f4323d9beb36df0a9dd
 init version: fec3683
 Security Options:
  seccomp
   Profile: default
 Kernel Version: 5.8.3-1.el7.elrepo.x86_64
 Operating System: CentOS Linux 7 (Core)
 OSType: linux
 Architecture: x86_64
 CPUs: 2
 Total Memory: 1.915GiB
 Name: k8s-leader
 ID: 2PXC:YUNB:G5YK:ZZWD:SLGB:FAXI:AZES:OP2W:IMYD:UCBW:EEO3:75Z2
 Docker Root Dir: /var/lib/docker
 Debug Mode: false
 Registry: https://index.docker.io/v1/
 Labels:
 Experimental: false
 Insecure Registries:
  127.0.0.0/8
 Registry Mirrors:
  https://registry.cn-hangzhou.aliyuncs.com/
 Live Restore Enabled: false

Created symlink from /etc/systemd/system/multi-user.target.wants/kubelet.service to /usr/lib/systemd/system/kubelet.service.
Client: Docker Engine - Community
 Version:           19.03.8
 API version:       1.40
 Go version:        go1.12.17
 Git commit:        afacb8b
 Built:             Wed Mar 11 01:27:04 2020
 OS/Arch:           linux/amd64
 Experimental:      false

Server: Docker Engine - Community
 Engine:
  Version:          19.03.8
  API version:      1.40 (minimum version 1.12)
  Go version:       go1.12.17
  Git commit:       afacb8b
  Built:            Wed Mar 11 01:25:42 2020
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.2.13
  GitCommit:        7ad184331fa3e55e52b890ea95e65ba581ae3429
 runc:
  Version:          1.0.0-rc10
  GitCommit:        dc9208a3303feef5b3839f4323d9beb36df0a9dd
 docker-init:
  Version:          0.18.0
  GitCommit:        fec3683
```

### 2.3 初始化 leader 节点
:::danger 关于初始化时用到的环境变量
- APISERVER_NAME 不能是 leader 的 hostname
- APISERVER_NAME 必须全为小写字母、数字、小数点，不能包含减号
- POD_SUBNET 所使用的网段不能与 master节点/worker节点 所在的网段重叠。该字段的取值为一个 CIDR 值，如果您对 CIDR 这个概念还不熟悉，请仍然执行 export POD_SUBNET=10.100.0.1/16 命令，不做修改
:::
```shell
# 只在 leader 节点执行
# 替换 x.x.x.x 为 leader 节点实际 IP（请使用内网 IP）
# export 命令只在当前 shell 会话中有效，开启新的 shell 窗口后，如果要继续安装过程，请重新执行此处的 export 命令
export MASTER_IP=x.x.x.x   # 192.168.83.11
# 替换 apiserver.demo 为 您想要的 dnsName
export APISERVER_NAME=k8s.vm
# Kubernetes 容器组所在的网段，该网段安装完成后，由 kubernetes 创建，事先并不存在于您的物理网络中
export POD_SUBNET=10.100.0.1/16
echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts
curl -sSL https://kuboard.cn/install-script/v1.18.x/init_master.sh | sh -s 1.18.8
```

### 2.4 初始化worker
#### 2.4.1 获得 join命令参数

在 master 节点上执行
```shell
# 只在 master 节点执行
kubeadm token create --print-join-command
```
可获取kubeadm join 命令及参数，如下所示
```shell
# kubeadm token create 命令的输出
kubeadm join apiserver.demo:6443 --token mpfjma.4vjjg8flqihor4vt     --discovery-token-ca-cert-hash sha256:6f7a8e40a810323672de5eee6f4d19aa2dbdb38411845a1bf5dd63485c43d303
```
    
:::tip 有效时间

该 token 的有效时间为 2 个小时，2小时内，您可以使用此 token 初始化任意数量的 worker 节点。

:::

#### 2.4.2 针对所有的 worker 节点执行
```shell
# 只在 worker 节点执行
# 替换 x.x.x.x 为 master 节点的内网 IP
export MASTER_IP=192.168.83.11
# 替换 apiserver.demo 为初始化 master 节点时所使用的 APISERVER_NAME
export APISERVER_NAME=k8s.vm
echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts

# 替换为 master 节点上 kubeadm token create 命令的输出
kubeadm join apiserver.demo:6443 --token mpfjma.4vjjg8flqihor4vt     --discovery-token-ca-cert-hash sha256:6f7a8e40a810323672de5eee6f4d19aa2dbdb38411845a1bf5dd63485c43d303

```
### 2.5 检查初始化结果
在 master 节点上执行
```shell
# 只在 master 节点执行
kubectl get nodes -o wide
# 输出结果如下所示：
[root@k8s-leader ~]# kubectl get nodes -o wide
NAME          STATUS   ROLES    AGE   VERSION   INTERNAL-IP     EXTERNAL-IP   OS-IMAGE                KERNEL-VERSION              CONTAINER-RUNTIME
k8s-leader    Ready    master   98m   v1.18.8   192.168.83.11   <none>        CentOS Linux 7 (Core)   5.8.3-1.el7.elrepo.x86_64   docker://19.3.8
k8s-worker1   Ready    <none>   23m   v1.18.8   192.168.83.12   <none>        CentOS Linux 7 (Core)   5.8.3-1.el7.elrepo.x86_64   docker://19.3.8
k8s-worker2   Ready    <none>   73m   v1.18.8   192.168.83.13   <none>        CentOS Linux 7 (Core)   5.8.3-1.el7.elrepo.x86_64   docker://19.3.8
```

### 2.6 设置节点role
```shell
kubectl label node k8s-worker2 node-role.kubernetes.io/worker=worker
```
