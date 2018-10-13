## 支付宝
### 接入前准备
#### 第一步:创建应用并获取APPID
* 首先入驻开放平台。
* 然后在蚂蚁金服开放平台开发者中心创建自己的应用。
* 创建成功后会生成唯一标识**APPID**
#### 第二步:配置应用
1. [添加app支付功能](https://docs.open.alipay.com/common/105366)

        创建应用完成后，需要添加App支付功能，此时是开发环境，智能在沙箱环境下进行调试。应用开发完成后需要进行验收和安全检查，验收完成后可申请上下，上线后才能调用生产环境的接口。

2. 签约

        在开放平台进行签约

3. 配置秘钥

        为了保证交易安全，需要配置双方秘钥，对交易数据进行双方校验。

* 支付宝秘钥处理体系:

![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/alipay_two.png )
* 秘钥包含:

**应用公钥：** 由商户自己生成的RSA公钥（与应用私钥必须匹配），商户需上传应用公钥到支付宝开放平台，以便支付宝使用该公钥验证该交易是否是商户发起的。

**应用私钥：** 由商户自己生成的RSA私钥（与应用公钥必须匹配），商户开发者使用应用私钥对请求字符串进行加签。

**支付宝公钥：** 支付宝的RSA公钥，商户使用该公钥验证该结果是否是支付宝返回的。

* 配置[生成秘钥](https://docs.open.alipay.com/291/105971)等配置信息

#### 第三步：集成和开发

        接入移动支付需要集成两个SDK，客户端需要集成在商户自己的APP中，用于唤起支付宝APP并发送交易数据，
        并在从支付宝APP返回商户APP时获得支付结果。服务端SDK需要商户集成在服务端系统中，用于协助解析并验证
        客户端同步返回的支付结果和异步通知。

**接入方式和架构建议**

![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/alipay_one.png )

**安全手段**

    1. 采用HTTPS协议传输交易数据，防止数据被截获，解密。
    2. 采用RSA非对称秘钥，明确交易双方的身份，保证交易主体的正确性和唯一性。

**集成客户端SDK**

### Android集成流程
#### 导入开发资源
* 导入jar包
	
		将alipaySDK-***.jar放到应用工程的libs目录下。

* 进入项目工程的`Project Structure`在`app module`下选择`File dependency`将alipaySDK-***.jar导入。
或者在`app module`下的`build.gradle`下手动添加依赖，如下：
````
dependencies {
    ......
    compile files('libs/alipaySdk-20170725.jar')
    ......

````

#### 修改Manifest

添加activity一下声明
````
<activity
    android:name="com.alipay.sdk.app.H5PayActivity"
    android:configChanges="orientation|keyboardHidden|navigation|screenSize"
    android:exported="false"
    android:screenOrientation="behind"
    android:windowSoftInputMode="adjustResize|stateHidden" >
</activity>
 <activity
    android:name="com.alipay.sdk.app.H5AuthActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="false"
    android:screenOrientation="behind"
    android:windowSoftInputMode="adjustResize|stateHidden" >
</activity>
````
权限声明
````
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

````
#### 添加混淆规则

在项目的proguard-project.txt添加一下规则
````
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
````
到此，集成完毕

### 支付接口调用PayTask
`PayTask`对象主要为商户提供**订单支付**、**查询功能**、**获取当前开发包版本号

一定要在新的线程(非UI线程)中调用支付接口

代码示例：
````
final String orderInfo = info;   // 订单信息

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(DemoActivity.this);
				String result = alipay.payV2(orderInfo,true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
	     // 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
````

更多说明[查看官方文档](https://docs.open.alipay.com/204/105296/)

## 微信
[微信支付开发文档](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5)

### PayReq

## 银联
