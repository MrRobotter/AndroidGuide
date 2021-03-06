# HTTP协议
* 默认端口：80
## Http协议的主要特点
1. 支持客户端/服务器端模式
2. 简单快速：客户端向服务端请求服务时，只需传送请求方式和路径。
3. 灵活：允许传送任意类型的数据对象。由Content-Type加以标记。
4. 无连接：每次响应一个请求，响应完成以后就断开连接。
5. 无状态：服务器不保存浏览器的任何信息。每次提交的请求之间没有关联。

### 非连续性和持续性
* HTTP1.0默认非持续性；浏览器和服务器建立TCP连接后，只能请求一个对象。

* HTTP1.1默认持续性；浏览器和服务器建立TCP连接后，可以请求多个对象。
### 非流水线和流水线
类似组成里面的流水操作
* 流水线：不必等到收到服务器的回应就发送下一个报文。
* 非流水线：发送一个报文，等到响应，再发下一个报文。类似TCP。

### POST和GET的区别
|**POST一般用于更新或者添加资源信息**| **GET一般用于查询操作，而且应该是安全和幂等的**|
| :----| :----|
|POST更加安全|GET会把请求的信息放到URL的后面|
|POST传输量一般无大小限制|GET不能大于2KB|
|POST执行效率低|GET执行效率高|

### 为什么POST效率低，GET效率高
* GET将参数拼成URL放到header消息头里传递。
* POST直接以键值对的形式放到消息体重传递。
* 但两者的效率差距很小很小。
# HTTPS
* 端口号：444
* 是由HTTP+SSL协议构建的可进行加密传输、身份认证的网络协议。
