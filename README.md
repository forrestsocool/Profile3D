# Profile3D
一个酷炫的带3D cloud效果的人员信息展示app，类似Soul App的主界面（其实反编译soul就会发现它的主界面和Profile3D是基于同一个库修改实现的），同时有开源的基于springboot的后端数据管理系统，是一个完整的作品。
后端见： https://github.com/sm1314/hrmsys

## 介绍
人员信息展示app，可作为人事管理系统的Android展示前端使用
Material Design风格，android sdk >=19

### 3D Cloud主界面
<img src="https://raw.githubusercontent.com/sm1314/Profile3D/master/screenshot/1.jpg" width="400px" />

### 搜索界面
<img src="https://raw.githubusercontent.com/sm1314/Profile3D/master/screenshot/2.jpg" width="400px" />

### 个人信息界面
<img src="https://raw.githubusercontent.com/sm1314/Profile3D/master/screenshot/3.jpg" width="400px" />

## 数据接口
后端restful api，url定义在MyApplication中，具体接口定义见后端project

## 特点
- 1.分辨率自适应，在50寸+的大屏终端显示效果较好
- 2.使用了较多自定义view，人机界面友好（强行凑2条）

## 参考
开发过程中使用了多几个优秀的开源项目，在此表示感谢！
- https://github.com/misakuo/3dTagCloudAndroid
- https://github.com/JessYanCoding/AndroidAutoSize
- https://github.com/IntruderShanky/Frisson
