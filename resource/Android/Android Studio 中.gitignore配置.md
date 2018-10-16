# Android Studio中.gitignore配置

###  问描述

在使用Git进行代码管理过程中，我们会发现后来添加的.gitignore文件，或者中途修改了.gitignore文件后，.gitignore文件并没有生效，需要清除.track缓存和强制.track来解决问题。
如果我们在第一次提交的时候，忘记添加.gitignore文件或者在首次添加了.gitignore文件之后，又对文件进行了修改，你会发现这两种情况下，.gitignore文件是不生效的。

### 原因

git 没有清理cache重点内容

### 解决方案

````
git rm -r --cached .
git add .
git commit -m "commit message"
````