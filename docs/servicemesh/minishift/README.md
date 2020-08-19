---
title: Minishift
date: 2020-08-19
sidebar: auto
prev: false
next: false
---

## 安装
1. 设置Minishift管理程序驱动程序
KVM用户需要手动安装驱动程序插件。以下安装KVM驱动程序（适用于KVM用户）。
```shell
sudo yum install libvirt qemu-kvm
sudo usermod -a -G libvirt $(whoami)
sudo sed -ri 's/.?group\s?=\s?".+"/group = "kvm"/1' /etc/libvirt/qemu.conf
newgrp libvirt || newgrp libvirtd
systemctl is-active libvirtd
sudo systemctl start libvirtd
sudo virsh net-list --all
#Name                 State      Autostart     Persistent
#---------------------------------------------------------
#default              active     yes           yes
sudo virsh net-start default
sudo virsh net-autostart default
#以下命令在root用户下执行
curl -L https://github.com/dhiltgen/docker-machine-kvm/releases/download/v0.10.0/docker-machine-driver-kvm-centos7 -o /usr/local/bin/docker-machine-driver-kvm
chmod +x /usr/local/bin/docker-machine-driver-kvm
```
:::tip 
如果kvm不能启动的话可以使用virtual box
:::
2. 下载openshift二进制文件
[github](https://github.com/minishift/minishift/releases/download/v1.34.2/minishift-1.34.2-linux-amd64.tgz)
得到minishift 二进制程序，下载完成后[配置minishift环境变量](../../foundation/linux#_1-6-设置环境变量)

```shell
install minishift /usr/local/bin
```

3. 下载oc程序
[github](https://github.com/openshift/origin/releases/download/v3.11.0/openshift-origin-client-tools-v3.11.0-0cbc58b-linux-64bit.tar.gz)
得到oc二进制程序，存放到下面位置
```shell
cp openshift-origin-client-tools-v3.11.0-0cbc58b-linux-64bit.tar.gz ~/.minishift/cache/oc/v3.11.0/linux/oc
```

4. 下载minishift-centos7.iso文件
[github](https://github.com/minishift/minishift-centos-iso/releases/download/v1.16.0/minishift-centos7.iso)
放到下面位置
```shell
cp minishift-centos7.iso ~/.minishift/cache/iso/centos/v1.16.0/minishift-centos7.iso 
```

:::warning 
如果以上文件夹没有创建，请自行创建
:::
5. 运行命令启动
```shell
minishift start #KVM
minishift --vm-driver=virtulbox # virtual box
```

参考[Redhat-OKD](https://docs.okd.io/3.11/minishift/using/basic-usage.html)

:::warning
如果使用的是Vmware首先要开启CPUVT虚拟化技术
:::

## 可能出现的问题
1. Openshift 版本校验
```shell
-- Checking if requested OpenShift version 'v3.11.0' is valid ... v3.11.0 is not a valid OpenShift versionFAIL
```
使用命令
```shell
minishift config set skip-check-openshift-release true
```

2. 网络切换或镜像下载失败
请使用科学网络去下载openshift需要的镜像，不然kvm是下载不了那些images的，在下载的时候也不要随意关闭科学上网工具，不然网络切换会造成镜像无法下载的情况，如果镜像拉取超时，请进入kvm自行下载
```shell
minishift ssh
docker pull ....
```

