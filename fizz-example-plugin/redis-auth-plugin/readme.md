example演示如何开发一个插件，即工程中的RedisAuthPlugin



1、调整application.yml的  

aggregate:  （fizz要求的）

​    redis:  

​        host: 1.2.3.4   

​        port: 6379   

​        password: 123456   

​        database: 10   

redis:  （例子的，可以指向aggregate，即用同一个）

​    host: 1.2.3.4  

​    port: 6379  

​    password: 123456  

​    database: 10  

为你的redis     



2、运行RedisAuthPluginApplication，程序会往上面的例子redis插入token0->user0这样的key->value，user0在系统中有token0，表明其是合法的



3、访问： 
http://127.0.0.1:8600/proxy/xservice/ypath?token=token0 

响应 "response done" 

http://127.0.0.1:8600/proxy/xservice/ypath?token=token1 

响应 "不存在 token token1" 拒绝当前请求

