1. 先编译，执行以下命令：
```
npm i && npx next build
```

2. out 文件夹下会有编译出来的静态文件。将这些静态文件拷贝到nginx的某个目录下，（如“ai_chat\out”）。

3. nginx的配置文件，增加以下段落：
```
	server {
        listen       80;
        server_name  chat.orders.com;


        location / {
            root   ai_chat/out;
            index  index.html index.htm;
			try_files $uri $uri.html $uri/ =404;
        }
    }
```
其中`try_files $uri $uri.html $uri/ =404;`的意思是，所有的路由加上“.html”后缀。