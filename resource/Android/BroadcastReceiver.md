### 接收广播消息
BroadcastReceiver是Android系统的四大组件之一，它的本质就是一个全局监听器，用于监听系统全局的广播消息。
由于BroadcastReceiver是一个全局监听器，因此它可以非常方便地实现系统中不同组件之间的通信。例如，我们
希望客户端程序与startService()方法启动的Service之间通信，就可以借助于BroadCastReceiver来实现。
### BroadcastReceiver简介

