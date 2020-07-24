---
title: 【nginx】Nginx
date: 2020-07-24
sidebar: auto
categories:
 - 基础
tags:
- nginx
---
*更多详情请参考[Nginx官方文档](http://nginx.org/en/docs/ "NGINX")*
## 一. 常用命令
```shell
nginx -s quit          #优雅停止nginx，有连接时会等连接请求完成再杀死worker进程  
nginx -s stop          #快速停止或关闭Nginx
nginx -s reload        #优雅重启，并重新载入配置文件nginx.conf
nginx -s reopen        #重新打开日志文件，一般用于切割日志
nginx -v               #查看版本  
nginx -t               #检查nginx的配置文件
nginx -h               #查看帮助信息
nginx -V               #详细版本信息，包括编译参数 
nginx  -c filename     #指定配置文件
```
## 二. Nginx配置文件

配置文件默认为安装目录下的conf/nginx.conf，如果有使用到其他子配置文件，可以在nginx.conf中使用include 文件路径;的方式加载使用，比如server段，就可以单独写成一个配置文件，在http段下面使用include加载使用。
如果使用yum类型的方式安装,那么配置文件在`/etc/nginx/`文件夹下，其中`conf.d`文件夹下为默认添加的conf。资源文件夹在`/usr/share/nginx/html/`目录下

```conf
...              #全局块
 
events {         #events块
   ...
}
 
http      #http块
{
    ...   #http全局块
    server        #server块
    { 
        ...       #server全局块
        location [PATTERN]   #location块
        {
            ...
        }
        location [PATTERN] 
        {
            ...
        }
    }
    server
    {
      ...
    }
    ...     #http全局块
}
```

- 全局块：配置影响nginx全局的指令。一般有运行nginx服务器的用户组，nginx进程pid存放路径，日志存放路径，配置文件引入，允许生成worker process数等。
- events块：配置影响nginx服务器或与用户的网络连接。有每个进程的最大连接数，选取哪种事件驱动模型处理连接请求，是否允许同时接受多个网路连接，开启多个网络连接序列化等。
- http块：可以嵌套多个server，配置代理，缓存，日志定义等绝大多数功能和第三方模块的配置。如文件引入，mime-type定义，日志自定义，是否使用sendfile传输文件，连接超时时间，单连接请求数等。
- server块：配置虚拟主机的相关参数，一个http中可以有多个server。
- location块：配置请求的路由，以及各种页面的处理情况。
配置示例:
```conf
#user nginx;  #配置用户或者组，默认为nobody nobody。
#worker_processes 2;  #允许生成的进程数，默认为1 可设置为auto，系统根据请求自动判断
#pid /nginx/pid/nginx.pid;   #指定nginx进程运行文件存放地址
error_log log/error.log debug;  #制定日志路径，级别。这个设置可以放入全局块，http块，server块，级别以此为：debug|info|notice|warn|error|crit|alert|emerg
events {
    accept_mutex on;   #设置网路连接序列化，防止惊群现象发生，默认为on
    multi_accept on;  #设置一个进程是否同时接受多个网络连接，默认为off
    #use epoll;      #事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
    worker_connections  1024;    #最大连接数，默认为512
}
http {
    include       mime.types;   #文件扩展名与文件类型映射表
    default_type  application/octet-stream; #默认文件类型，默认为text/plain
    #access_log off; #取消服务日志   
    log_format myFormat '$remote_addr–$remote_user [$time_local] $request $status $body_bytes_sent $http_referer $http_user_agent $http_x_forwarded_for'; #自定义日志格式
    access_log log/access.log myFormat;  #combined为日志格式的默认值
    sendfile on;   #允许sendfile方式传输文件，默认为off，可以在http块，server块，location块。
    sendfile_max_chunk 100k;  #每个进程每次调用传输数量不能大于设定的值，默认为0，即不设上限。
    keepalive_timeout 65;  #连接超时时间，默认为75s，可以在http，server，location块。
    # 隐藏nginx版本号，不再浏览显示
    server_tokens off;

    #gzip加速
    gzip  on;
    gzip_http_version 1.0;    # 指定http的版本
    gzip_disable 'MSIE [1-6]';    # 禁止IE的1~6版本使用该功能
    gzip_types application/javascript text/css image/jpeg image/png; 

    # 禁止ip访问，当有匹配时，就不会在向下匹配
    # deny all;        # 拒绝所有
    # allow 192.168.211.1;         # 允许192.168.211.1

    # 用户访问限制
    # auth_basic 'pls login:';        # 指定提示语"pls login:"
    # auth_basic_user_file /usr/local/nginx/conf/userlist;        # 指定授权用户所在文件
    
    upstream mysvr {  
      server 127.0.0.1:7878;
      server 192.168.10.121:3333 backup;  #热备
    }
    error_page 404 https://www.baidu.com; #错误页
    server {
        keepalive_requests 120; #单连接请求上限次数。
        listen       4545;   #监听端口
        server_name  127.0.0.1;   #监听地址      
        location  ~*^.+$ {       #请求的url过滤，正则匹配，~为区分大小写，~*为不区分大小写。释义为：不区分大小写，以1个以上的 非换行符的字符 开始并结束
           #root path;  #根目录
           #index vv.txt;  #设置默认页
           proxy_pass  http://mysvr;  #请求转向mysvr 定义的服务器列表
           deny 127.0.0.1;  #拒绝的ip
           allow 172.18.5.54; #允许的ip  
           # 资源重定向，如访问http://shop.devops.com/index.html后会被重写为访问http://shop.devops.com/index.php，permanent表示永久重定向
            rewrite /index.html /index.php permanent; 
            # expires 设置客户端缓存
            expires 1h;
            # 资源重定向，$request_filename为nginx的内置变量，表示资源文件路径
            if (!-e $request_filename) {
                rewrite ^(.*)$ /index.php?s=/$1 last;
                break;
            }
            # 防盗链实现，所有不是从shop.devops.com跳转过去访问js|css|jpg|png文件的都被拦截，返回404
            valid_referers shop.devops.com;
            if ($invalid_referer) {
                return 404;
            }    
        }
    }
}
```
:::warning
**注：每个指令必须有分号结束**
:::

## 三、配置文件详解

### 2.1 全局块
- #### 配置运行Nginx服务器用户（组）
指令格式：`user user [group];`  
&ensp;user：指定可以运行Nginx服务器的用户  
&ensp;group：可选项，可以运行Nginx服务器的用户组
:::tip
**注：如果user指令不配置或者配置为 user nobody nobody ，则默认所有用户都可以启动Nginx进程**
:::
------
- #### worker process数配置
Nginx服务器实现并发处理服务的关键，指令格式：`worker_processes number | auto;`  
&ensp;number：Nginx进程最多可以产生的worker process数  
&ensp;auto：Nginx进程将自动检测
原作者的话：
```
As a general rule you need the only worker with large number of
worker_connections, say 10,000 or 20,000.
However, if nginx does CPU-intensive work as SSL or gzipping and
you have 2 or more CPU, then you may set worker_processes to be equal
to CPU number.
Besides, if you serve many static files and the total size of the files
is bigger than memory, then you may increase worker_processes to
utilize a full disk bandwidth.
Igor Sysoev
```
一般一个进程足够了，可以把连接数设得很大。
如果有SSL、gzip这些比较消耗CPU的工作，而且是多核CPU的话，可以设为和CPU的数量一样。
或者要处理很多很多的小文件，而且文件总大小比内存大很多的时候，也可以把进程数增加，
以充分利用IO带宽（主要似乎是IO操作有block）
:::tip
**注：服务器是“多个CPU＋gzip+网站总文件大小大于内存”的环境，worker_processes设置为CPU个数的两倍比较好**
:::
------
- #### Nginx进程PID存放路径
Nginx进程作为系统的守护进程运行，我们需要在某个文件中保存当前运行程序的主进程号，Nginx支持定义pid的存放路径，其指令为：pid，语法格式如下
`pid file;`  
&ensp;file：存放路径和文件名称  
默认存放在nginx安装目录logs下，名字为：nginx.pid

:::tip
**注：一般情况下，pid文件，都是在编译安装nginx的时候，通过 --pid-path=PATH，该PATH一定要使用绝对路径**
:::
------
- #### 定义MIME类型
在常用的浏览器中，可以显示的内容有html、xml、gif、flash等种类繁多的文本、媒体等资源，浏览器为区分这些资料，引用了MIME type，换言之，MIME type是网络资源的媒体类型  
默认nginx配置文件
```conf
include mime.types;
default_type application/octet-stream;
```
```shell
cat /etc/nginx/mime.types
```
default_type配置了用来处理前端请求的MIME类型，其语法如下  
`default_type mime-type;`  
其中mime-type一定要是types块中定义过，如果不设置这个，默认为text/plain

------

- #### 自定义nginx的访问日志

- ##### access_log指令
语法：
```conf
access_log path [format [buffer=size] [gzip[=level]] [flush=time] [if=condition]];
access_log off;
```
说明：  
path： 配置服务日志的文件存放的路径和名称  
format，可选项，自定义服务日志的格式字符串  
size：配置临时存放日志的内存缓存区大小  
该指令可以配置在http块，server块，location块，默认如下:  
#access_log logs/access.log main;
- ##### log_format指令
语法如下：
`log_format name [escape=default|json] string ...`  
说明：  
name：格式字符串的名字，默认为combined  
string： 服务日志的格式字符串，在定义过程中，nginx配置预设一些变量获取相关内容 
```
# log_format main '$remote_addr - $remote_user [$time_local] "$request" '
# '$status $body_bytes_sent "$http_referer" '
# '"$http_user_agent" "$http_x_forwarded_for"';
```
参数具体说明如下：  
参数|说明|示例
-|-|-
$remote_addr|客户端地址|10.2.18.231
$remote_user|客户端用户名称|- -
$time_local |访问时间和时区|07/Dec/2017:16:39:09 +0800
$request |请求的URI和HTTP协议| "GET / HTTP/1.1"
$http_host |请求的地址| www.a.com
$status |HTTP响应状态码 |200
$body_bytes_sent| 发送给客户端文件内容大小| 612
$http_referer| url跳转来源 |-
$http_user_agent| 用户终端浏览器等信息 |"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
$upstream_addr| 后台upstream的地址|
$request_time |整个请求的时间|
$upstream_response_time |请求过程中，upstream响应时间| 

### 2.2 Event块
```conf
events {
  accept_mutex on;             #设置网路连接序列化，防止惊群现象发生，默认为on
  multi_accept on;             #设置一个进程是否同时接受多个网络连接，默认为off
  #use epoll;                  #事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
  worker_connections  1024;    #最大连接数，默认为512
}
```
:::tip
**注：惊群现象**  
对于操作系统来说，多个进程/线程在等待同一资源时，其结果就是每当资源可用，所有的进程/线程都来竞争资源，会造成以下后果：
- 系统对用户进程/线程频繁的做无效的调度、上下文切换，系统性能大打折扣。
- 为了确保只有一个线程得到资源，用户必须对资源操作进行加锁保护，进一步加大了系统开销。
:::
更多详情请参考[Linux惊群效应之Nginx解决方案](https://zhuanlan.zhihu.com/p/51251700 "滴滴云")

### 2.3 http块
```
http{
  include       mime.types;   #文件扩展名与文件类型映射表
  default_type  application/octet-stream; #默认文件类型，默认为text/plain
  #access_log off; #取消服务日志   
  log_format myFormat '$remote_addr–$remote_user [$time_local] $request $status $body_bytes_sent $http_referer $http_user_agent $http_x_forwarded_for'; #自定义格式
  access_log log/access.log myFormat;  #combined为日志格式的默认值
  sendfile on;   #允许sendfile方式传输文件，默认为off，可以在http块，server块，location块。
  sendfile_max_chunk 100k;  #每个进程每次调用传输数量不能大于设定的值，默认为0，即不设上限。
  keepalive_timeout 65;  #连接超时时间，默认为75s，可以在http，server，location块。
  # 隐藏nginx版本号，不再浏览显示
  server_tokens off;
  #gzip加速
  #gzip  on;

  upstream mysvr {  
    server 127.0.0.1:7878;
    server 192.168.10.121:3333 backup;  #热备
  }
  error_page 404 https://www.baidu.com; #错误页
}
```
### 2.4 server块
```
server {
  keepalive_requests 120; #单连接请求上限次数。
  listen       4545;   #监听端口
  server_name  127.0.0.1;   #监听地址      
}
```

- #### 2.4.1 listen
功能：指定监听端口
案例：
```conf
listen 127.0.0.1:8000;
listen 127.0.0.1;
listen 8000;
listen *:8000;
listen localhost:8000;
listen 443 ssl;
```
- #### 2.4.2 server_name
功能：设置虚拟主机的名字（域名）
案例：  
`server_name 51pet.comwww.51pet.comm.51pet.com`

匹配法则：    
- 1）精确匹配
- 2）左侧通配符匹配 .test.com
- 3）右侧通配符匹配 mail.
- 4）正则表达式匹配检测

- #### 2.4.3 root
功能：为请求设置根目录
案例
```conf 
root /usr/share/nginx/html
location /i/ {
  root /data/w3;
}
```

- #### 2.4.4 alias
语法：
```
alias path;
location /i/ {
alias /data/w3/images/;
}
```
on request of “/i/top.gif”, the file /data/w3/images/top.gif will be sent.
```
location ~ ^/users/(.+.(?:gif|jpe?g|png))$ {
alias /data/w3/images/$1;
}
```

:::tip
**alias和root区别**  
nginx是通过alias设置虚拟目录，在nginx的配置中，alias目录和root目录是有区别的：  
1）alias指定的目录是准确的，即location匹配访问的path目录下的文件直接是在alias目录下查找的；  
2）root指定的目录是location匹配访问的path目录的上一级目录,这个path目录一定要是真实存在root指定目录下的；  
3）使用alias标签的目录块中不能使用rewrite的break（具体原因不明）；另外，alias指定的目录后面必须要加上"/"符号！！  
4）alias虚拟目录配置中，location匹配的path目录如果后面不带"/"，那么访问的url地址中这个path目录后面加不加"/"不影响访问，访问时它会自动加上"/"；但是如果location匹配的path目录后面加上"/"，那么访问的url地址中这个path目录必须要加上"/"，访问时它不会自动加上"/"。如果不加上"/"，访问就会失败！  
5）root目录配置中，location匹配的path目录后面带不带"/"，都不会影响访问。  
:::

- #### 2.4.5 index

功能：指定默认主页面

案例：

`index index.php index.html index.htm;`

- #### 2.4.6 error_page
语法：
`error_page code ... [=[response]] uri;`

案例：
```conf
error_page 404 /404.html;
error_page 500 502 503 504 /50x.html;
error_page 404 =302 /404.html;
```
### 2.5 location块
`location [=|~|~*|^~] /uri/ { … }`  
语法规则： 
- = 开头表示精确匹配
- ^~ 开头表示uri以某个常规字符串开头，理解为匹配 url路径即可。nginx不对url做编码，因此请求为/static/20%/aa，可以被规则^~ /static/ /aa匹配到（注意是空格）。以xx开头
- ~ 开头表示区分大小写的正则匹配                     以xx结尾
- ~* 开头表示不区分大小写的正则匹配                以xx结尾
- !~和!~*分别为区分大小写不匹配及不区分大小写不匹配 的正则
- / 通用匹配，任何请求都会匹配到。
多个location配置的情况下匹配顺序为:  
首先精确匹配 =-》其次以xx开头匹配^~-》然后是按文件中顺序的正则匹配-》最后是交给 / 通用匹配。
当有匹配成功时候，停止匹配，按当前匹配规则处理请求。
:::tip
**注：文件中顺序的正则匹配从前缀最长的开始找**
:::
- #### 2.5.1 ReWrite语法
- last – 基本上都用这个Flag。
- break – 中止Rewirte，不在继续匹配
- redirect – 返回临时重定向的HTTP状态302
- permanent – 返回永久重定向的HTTP状态301
- #### 2.5.2 Try_files

```
location /images/ {
    root /opt/html/;
    try_files $uri   $uri/  /images/default.gif; 
}
```
比如 请求 127.0.0.1/images/test.gif 会依次查找 
1. 文件/opt/html/images/test.gif   
2. 文件夹 /opt/html/images/test.gif/下的index文件  
3. 请求127.0.0.1/images/default.gif
:::tip
**如果不写上 $uri/，当直接访问一个目录路径时，并不会去匹配目录下的索引页  即 访问127.0.0.1/images/ 不会去访问  127.0.0.1/images/index.html**
:::

*其他配置或调优请参考[nginx手册](https://www.kancloud.cn/louis1986/nginx-web "看云")*
 
