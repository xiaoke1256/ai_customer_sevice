在CentOS7下安装python3.9
---
检查python2的位置
```shell
whereis python
```
安装依赖包。
```shell
yum -y install zlib-devel bzip2-devel openssl-devel libffi-devel ncurses-devel sqlite-devel readline-devel tk-devel zlib gcc make python-devel mysql-devel mysql-lib epel-release python-pip
```
安装pip
```shell
yum -y install epel-release
yum -y install python-pip
```
下载python3.9的源码包
```shell
wget https://www.python.org/ftp/python/3.9.6/Python-3.9.6.tar.xz
```
编译python3源码包
```shell
xz -d Python-3.9.6.tar.xz
tar -xf Python-3.9.6.tar
cd Python-3.9.6
./configure prefix=/usr/local/python39
make && make install
```
出现Successfully installed代表安装成功.


添加python3的软链接：
```shell
ln -s /usr/local/python39/bin/python3.9 /usr/bin/python39
ln -s /usr/local/python39/bin/pip3.9 /usr/bin/pip39

ln -s /usr/local/python39/bin/python3.9 /usr/bin/python3
ln -s /usr/local/python39/bin/pip3.9 /usr/bin/pip3
```
测试是否安装成功：
```shell
python39 -V
```
配置pip国内镜像地址
```shell
mkdir ~/.pip
vim ~/.pip/pip.conf
```
添加内容：
```
[global]
index-url=https://mirrors.aliyun.com/pypi/simple
[install]
trusted-host=mirrors.aliyun.com
```
保存后，尝试安装packages，验证下是否已更换为阿里源
```shell
pip39 install virtualenv virtualenvwrapper
```


