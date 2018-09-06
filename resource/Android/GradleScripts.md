# 理解与配置Android studio中的gradle
我们使用AS开发时会有一个Gradle Scripts文件夹，里面都有一个或多个build.gradle文件。写本文是为了看懂AS中Gradle Scripts。

### 1.理解project、tast和action的关系
一个项目至少有一个工程(project),一个工程(project)至少有一个任务(task)，一个任务(task)由一些action组成。在工程构建的过程中，gradle会根据build.gradle中的配置信息生成相应的project和task,Project实质上是一系列task的集合，每一个task执行一些工作，比如编译类文件，解压缩文件，删除文件等。
#### 1.1 构建过程
* 初始化阶段。首先会创建一个Project对象，然后执行build.gradle配置这个对象。如果一个工程中有多个module,那么意味着有多个Project，也就需要多个build.gradle。
* 配置阶段。 配置脚本会被执行，执行过程中，新的task会被创建并且配置给Project对象。
* 执行阶段。配置阶段的task会被执行，执行的顺序取决于启动脚本时传入的参数和当前目录。

#### 1.2 task
 task表示一个逻辑上的执行单元，我们会多次使用。当我们进行重新编译工程的时候会用到一个叫做build的task，清理工程的时候会用到clean的task，gradle已经为我们准备了一系列的task，我们可以使用gradle task来查看：AS界面右侧 Gradle 如下图所示:

 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/gradle_tastk.png )


 此外，还可以自己声明一个task，比如：
 ```
 task testHaha {
    println "这是一个测试！"
}
````
同步之后，我们可以在上图中的task中 other里看到 testHaha 

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

### gradle DSL
DSL(Domain Specific Language)，特定领域的语言。gradle DSL就是gradle领域的语言。为了更好理解gradle,学习gradle的脚步虽然非常简短，但它有它的语法，如果不搞懂DSL，即便知道了怎么修改脚本得到想要的结果，也不好理解为什么要这样修改。
#### 3.1 基本概念





