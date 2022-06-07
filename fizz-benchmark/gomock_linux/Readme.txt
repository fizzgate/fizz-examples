1. 执行gomock_linux （默认使用./example/mock.json配置文件）

   使用指定的配置文件：./gomock_linux "./config.json"


2. 配置文件说明：

	address: 绑定IP，0.0.0.0表示绑定当前服务器的IP
	port：  mock服务端口
	requestLog: 是否打印访问日志，打印日志会影响性能，压测时关闭

	sleepTime：睡眠时间，用来模拟真实的接口耗时，单位毫秒
	method: 请求方法 GET/POST
	url: 接口的URL路径

	timestampToBody: 是否添加一个时间戳字段到json报文里, 仅用于json报文
	status：接口返回的HTTP状态码,一般用200
	body 和 bodyFile： 接口的返回报文，如果bodyFile指定了文件则bodyFile的内容优先
	