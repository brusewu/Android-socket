# Android-socket
socket test

What is socket？

Socket也称作“套接字“，是在应用层和传输层之间的一个抽象层，它把TCP/IP层复杂的操作抽象为几个简单的接口供应用层调用以实现进程在网络中通信。它分为流式套接字和数据包套接字，分别对应网络传输控制层的TCP和UDP协议。TCP协议是一种面向连接的、可靠的、基于字节流的传输层通信协议。它使用三次握手协议建立连接，并且提供了超时重传机制，具有很高的稳定性。UDP协议则是是一种无连接的协议，且不对传送数据包进行可靠性保证，适合于一次传输少量数据，UDP传输的可靠性由应用层负责。在网络质量令人十分不满意的环境下，UDP协议数据包丢失会比较严重。但是由于UDP的特性：它不属于连接型协议，因而具有资源消耗小，处理速度快的优点，所以通常音频、视频和普通数据在传送时使用UDP较多。
![image](https://github.com/brusewu/Android-socket/blob/master/VnQOhQ.jpg)

测试的结果：
![image](https://github.com/brusewu/Android-socket/blob/master/image.jpg)




