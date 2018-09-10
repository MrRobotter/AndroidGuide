# 理解与配置Android studio中的gradle
我们使用AS开发时会有一个Gradle Scripts文件夹，里面都有一个或多个build.gradle文件。写本文是为了看懂AS中Gradle Scripts。

### 1.理解project、tast和action的关系
一个项目至少有一个工程(project),一个工程(project)至少有一个任务(task)，一个任务(task)由一些action组成。在工程构建的过程中，gradle会根据build.gradle中的配置信息生成相应的project和task,Project实质上是一系列task的集合，每一个task执行一些工作，比如编译类文件，解压缩文件，删除文件等。
#### 1.1 构建过程
* 初始化阶段。首先会创建一个Project对象，然后执行build.gradle配置这个对象。如果一个工程中有多个module,那么意味着有多个Project，也就需要多个build.gradle。
* 配置阶段。 配置脚本会被执行，执行过程中，新的task会被创建并且配置给Project对象。
* 执行阶段。配置阶段的task会被执行，执行的顺序取决于启动脚本时传入的参数和当前目录。

#### 1.2 task
 task表示一个逻辑上的执行单元，我们会多次使用。当我们进行重新编译工程的时候会用到一个叫做build的task，清理工程的时候会用到clean的task，gradle已经为我们准备了一系列的task，我们可以使用gradle task来查看：AS界面右侧 Gradle 如下图所示: <br>

 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/gradle_tastk.png )


 此外，还可以自己声明一个task，比如：
 ```
 task testHaha {
    println "这是一个测试！"
}
````
同步之后，我们可以在上图中的task中 other里看到 testHaha<br>

 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/task_test.jpg )

 双击这个task 这这个任务就会被执行，打印结果可以验证：
 ````
 Executing tasks: [testHaha]

Configuration on demand is an incubating feature.
这是一个测试！
Configuration 'compile' in project ':app' is deprecated. Use 'implementation' instead.
Configuration 'provided' in project ':app' is deprecated. Use 'compileOnly' instead.
:app:testHaha UP-TO-DATE

BUILD SUCCESSFUL in 0s

````
还可以用以下方法来定义task：
````
task hello << {
    println "hello world"
}

````
这个和前者的区别是：“<<”的意思是给hello这个task添加一些action，其实就是调用了task的doLast方法，所以他和以下代码是等价的:
````
task hello {
    doLast{
        println "hello world"
    }
}

````
关于testHaha和hello的区别我们还可以这样加深印象：
首先进入工程目录，执行gradle(后面没有任何参数，另外，这个时候，build.gradle中同时有hello和testHaha两个task)
我们随机执行一个task 比如常见的`clean：`结果如下：
````
上午9:44:24: Executing task 'clean'...

Executing tasks: [clean]

Configuration on demand is an incubating feature.
这是一个测试！
The Task.leftShift(Closure) method has been deprecated and is scheduled to be removed in Gradle 5.0. Please use Task.doLast(Action) instead.
	at build_1f7jv092eeyhsvnt04ckuehu.run(/Users/zhangjunyang/learn/AndroidGuide/app/build.gradle:31)
Configuration 'compile' in project ':app' is deprecated. Use 'implementation' instead.
Configuration 'provided' in project ':app' is deprecated. Use 'compileOnly' instead.
:app:clean

BUILD SUCCESSFUL in 1s
1 actionable task: 1 executed
上午9:44:26: Task execution finished 'clean'.

````


我们再执行`hello`任务：


````
上午9:50:57: Executing task 'hello'...

Executing tasks: [hello]

Configuration on demand is an incubating feature.
这是一个测试！
The Task.leftShift(Closure) method has been deprecated and is scheduled to be removed in Gradle 5.0. Please use Task.doLast(Action) instead.
	at build_1f7jv092eeyhsvnt04ckuehu.run(/Users/zhangjunyang/learn/AndroidGuide/app/build.gradle:31)
Configuration 'compile' in project ':app' is deprecated. Use 'implementation' instead.
Configuration 'provided' in project ':app' is deprecated. Use 'compileOnly' instead.
:app:hello
hello world

BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
上午9:50:58: Task execution finished 'hello'.

````
我们发现两次testHaha被执行了，我们执行clean时，testHaha被打印了，而hello没有打印，在这些hello的时候先执行了testHahaha然后执行了hello说明定义testHaha任务的方式在初始化就被执行，而使用hello这种方式只有在执行阶段才会执行。

在AS的顶层build.gradle中有一个这样的task：

````
task clean(type: Delete) {
    delete rootProject.buildDir
}

````
可以看到这个task中有一个类型type，task有很多类型（查一下）
这里用的是delete类型，说明是执行的是删除文件的任务了。

### 2.Closures
#### 2.1 定义闭包
理解gradle需要首先理解闭包的概念，Closure就是一段代码块，代码块一般要用{}包起来，所以闭包的定义可以像以下的样子：
````
def printHello = { println 'Hello' }

printHello()
````

从上面可以看到闭包虽然可以认为是一段代码块，但是它key像函数一样调用，而且它还可以接受参数，比如：

````
def myClosure = { String str -> println str }
myClosure('哈哈')
````
这样这个闭包就有参数了，多个参数只需要在 -> 前面添加就好了。
#### 2.2 委托
另外一个很酷的点是closure的上下文是可以改变的，通过CLosure.setDelegate()。这个特性非常有用：

````
def myClosure2 = { println myVar }
MyClass myClass = new MyClass()
myClosure2.setDelegate(myClass)
myClosure2()

class MyClass {
    def myVar = 'Hello from MyClass!'
}
// output:Hello from MyClass!

````
如上所示，创建closure的时候，myVar并不存在。但是没有关系，因为当执行closure的时候，在closure的上下文中，myVar是存在的。这个例子中，因为在执行closure之前改变了它的上下文为myClas,因此myVar是存在的。
#### 2.3 闭包作为参数
闭包是可以作为参数传递的，以下是闭包作为参数的一些使用规则：
		
		1. 只接收一个参数，且参数是closure的方法：myMethod(myClosure)
		2. 如果方法只接收一个参数，括号可以省略：myMethod myClosure
		3. 可以使用内联的Closure：myMethod{ println 'Hello world'}
		4. 接收两个参数的方法：myMethod(arg1,myClosure)
		5. 和4类似，单数Closure是内联的：myMethod(arg1,{ println 'Hello world'})
		6. 如果最后一个参数是closure,它可以从小括号中拿出来：myMethod(arg1){ println 'Hello world'

### 3 gradle DSL
DSL(Domain Specific Language)，特定领域的语言。gradle DSL就是gradle领域的语言。为了更好理解gradle,学习gradle的脚步虽然非常简短，但它有它的语法，如果不搞懂DSL，即便知道了怎么修改脚本得到想要的结果，也不好理解为什么要这样修改。
#### 3.1 基本概念
首先，gradle script是配置脚本，当脚本被执行的时候，它配置一个特定的对象，比如说，在AS工程中，build.gradle被执行的时候，它会配置一个Project对象，settings.gradle被执行时，它配置一个Settings对象。Project,Settings这种对象就叫做委托对象，下表展示了不同脚本的不同委托对象：

| **Type of script** | **Delegates to instance of** |
| :----:|:----: |
| Build script | Project |
| Init script | Gradle |
| Settings script| Settings |

其次，每一个Gradle script实现了一个Script接口，这意味着S抽屉平台接口中定义的方法和属性都可以在脚本中使用。

#### 3.2构建脚本结构
一个构建脚本由零个或多个statements和script blocks组成。以下是对他们的说明：
		
		A build script is made up of zero or more statements and script blocks. 
		Statements can include method calls, property assignments, and local variable definitions. 
		A script block is a method call which takes a closure as a parameter. 
		The closure is treated as a configuration closure which configures some delegate object as it executes. 
		The top level script blocks are listed below.

大概意思是statments可以包括方法调用，属性分配，本地变量定义；script bolck 则是一个方法，它的参数可以是一个闭包。这个闭包是一个配置闭包，因为当它被执行的时候，它用来分配委托对象。以AS的build.gradle为例：

````
apply plugin: 'com.android.application'

android {
    compileSdkVersion 26
    defaultConfig {
        applicationId "com.joinyon.androidguide"
        minSdkVersion 21
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
repositories {

    maven {
        url "https://jitpack.io"
    }
}


dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    }

````

该片段第一行 `apply plugin: 'com.android.application' 就是一条statements，其中apply是一个方法，后面是它的参数。这行语句其实是个缩写，写全应该是下面的：
````
project.apply([plugin:'com.android.application'])
````
这样就好理解了，project对象调用了apply方法，传入了一个Map作为参数，这个Map的key是plugin,value是com.android.application.

接下来看下一个代码段：
````
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    }
````
这是一条 script block,这个也是简写的。dependencies完整的写法为：
````
project.dependencies({
	add('compile','com.android.tools.build:gradle:2.0',{

	//Configuration statements

	})
})
````
我们知道block是一个闭包，这里首先调用project下的dependencies方法，这个方法的参数是一个闭包，这个闭包是被传递给DependencyHandler,DependenchHandler有一个方法：add,这个add有三个参数，分别是'compile'、'……'和一个闭包
gradle中有以下顶层build script block

| **Block** | **Description** |
| :----:|:----: |
| allProjects{} | 配置本工程和子工程 Configures this project and each of its sub-project |
| artifacts{} | Configures the published artifacts for this project. |
| buildScript{}| Configures the build script classpath for this project |
| configuration{} | Configures the dependency configurations for this project|
| dependencies{}|Configures the dependencies for this project.|
| repositories{}| Configures the repositories for this project|
| sourceSet{} | Configures the source sets of this project|
| subProject{}| Configures the sub-projects of this project|
| publishing{}| Configures the Publishing Extension added by the publishing plugin.|
这里再以allProject为例，说一下script block 是怎么工作的：
````
   allprojects{
    repositiories{
        jcenter()
    }
   }
````
allprojects{}一般是顶层build.gradle中的一个script block，它就是一个方法，这个方法接受一个闭包作为参数。
gradle工具会先创建一个Project对象，它是一个委托对象（delegate object）,它创建以后，build.gradle被执行，执行的过程中,
allproject{}方法被调用，这个方法的参数是一个闭包，然后闭包会被执行，用来配置Project对象。

### 4.Understanding the Gradle files
理解了Project、task和action的概念以后，就可以理解gradle的配置文件了。在Android studio的工程中一般会有三个配置文件，他们各有各的功能。这三个文件的位置应该是这样的：<br>
 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/gradle文件位置示意图.jpg )
 <center> sss </center >
简化之后：
````
MyApp
|--build.gradle
|--settings.gradle
|--app
    |--build.gradle
````

构建一个工程的时候，会有以下的顺序：
1. 创建一个Settings对象。
2. 检查settings.gradle是否存在，不存在就什么也不做，存在就用它来配置settings对象。
3. 使用Settings对象创建Project对象，多个Module工程中，会创建一系列的Project对象。
4. 检查build.gradle是不是存在，存在的话就用它来配置Project对象。
#### 4.1 settings.gradle
如果一个新的工程只包含一个Android app 那么settings.gradle 应该是这样的：
````
include ':app'
```` 
如果你的工程里只有一个app，那么settings.gradle文件可以不要。include':app'中的app指明你要构建的模块名，Android studio 默认的模块名是 app ，你可以把app目录的名字改掉，比如改成hello，那么这个时候，你就必须把settings.gradle中的 app也改成hello.这会是你非常有意义的一次尝试，因为有了这次尝试，以后你就可以按你所愿修改这个文件了。下面我们把`app`改成`guide`,再来看工程目录结构：
 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/guide.jpg )
 接下来我们就可以一次性构建多个app了
 第一步：在工程上右键，选择新建mudole
 第二步：成功了
 我们再看工程的样子：






