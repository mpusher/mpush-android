# mpush-android
* 该项目包含Android SDK和 一个简单demo两部分
* 配置建议直接copy工程中的`AndroidManifest.xml`文件
* 特别说明该工程依赖的`mpush-client-0.0.2.jar`源码地址为[mpush-client-java](https://github.com/mpusher/mpush-client-java)工程

## 系统结构图

![](https://mpusher.github.io/docs/AndroidSDK架构图.png)

## 说明
1. 整个图非常简单清晰的分为Server、SDK、BIZ三部分。
2. MpushClient负责和server通信，屏蔽网络，协议，断线重连等所有和长链接相关的东西。
3. MpushService是常驻服务，持有MpushClient,并把自身作为MpushClient的ClientListener，监听MpushClient的变化事件。
4. MpushReceiver主要负责监听网络变化和AlarmManager，以便暂停和恢复推送服务以及健康检查。
5. 线1表示上行的请求，比如握手，心跳，绑定用户，业务HTTP代理请求等。
6. 线2表示下行响应或推送，比如握手成功，心跳响应，HTTP代理响应等。
7. 线3表示Client下发的事件，主要有：链接建立/断开，握手成功，收到PUSH，设备被踢下线等事件，其中PUSH和KICK_USER事件会广播出去，由业务(MyReceiver)接收；其他事件会通知给MpushReceiver以便其能更好的控制MpushClient的起停，而MpushService就比较轻量基本没有什么业务逻辑，只负责维持后台服务。
8. 线4表示由MpushService广播出去的PUSH消息，由于采用的是广播的形式，所有也可以分进程。
9. 线5表示消息有MyReceiver过滤处理后，转交给业务去显示或存DB等。
10. 线6表示业务可以直接调用MpushClient提供的接口发送消息，目前支持的有绑定usreId，发送Http请求等。
11. 线7表示一些不需要业务处理的消息都交由MpuhReceiver处理，比如握手成功后启动AlarmManager，当链接断开后取消AlarmManager。
12. 线8表示MpushReceiver接收到AlarmManager的提醒后去调用MpshClient的healthCheck方法发送心跳。
13. 线9表示MpushReceiver接收到网络变化后暂停或恢复MpushClient，这样做主要是为了省电，因为在网络断开后，MpushClient会去尝试重连而这时候去重连是没有意义的，因为没有网络。
