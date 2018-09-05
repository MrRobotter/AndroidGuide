# 理解与配置Android studio中的gradle
我们使用AS开发时会有一个Gradle Scripts文件夹，里面都有一个或多个build.gradle文件。写本文是为了看懂AS中Gradle Scripts。

### 1.理解project、tast和action的关系
一个项目至少有一个工程(project),一个工程(project)至少有一个任务(task)，一个任务(task)由一些action组成。在工程构建的过程中，gradle会根据build.gradle中的配置信息生成相应的project和task,Project实质上是一系列task的集合，每一个task执行一些工作，比如编译类文件，解压缩文件，删除文件等。
#### 1.1 构建过程
* 初始化阶段。首先会创建一个Project对象，然后执行build.gradle配置这个对象。如果一个工程中有多个module,那么意味着有多个Project，也就需要多个build.gradle。
* 配置阶段。 配置脚本会被执行，执行过程中，新的task会被创建并且配置给Project对象。
* 执行阶段。配置阶段的task会被执行，执行的顺序取决于启动脚本时传入的参数和当前目录。

#### 1.2task
 task表示一个逻辑上的执行单元，我们会多次使用。当我们进行重新编译工程的时候会用到一个叫做build的task，清理工程的时候会用到clean的task，gradle已经为我们准备了一系列的task，我们可以使用gradle task来查看：AS界面右侧 Gradle
 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/gradle_tastk.png)