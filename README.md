# ZXingMini ZXing开源库的精简版

基于ZXing Android实现的二维码扫描支持库。
包括`生成二维码图片`、`解析二维码图片`和`相机扫描即时解码`三部分功能。

## 与原ZXingMini项目对比

- NextQRCode做了重大架构修改，原ZXingMini项目与当前NextQRCode不兼容;
- 相机扫描即时解码模块，使用Camera2包；

## 依赖


## 生成二维码图片


#### 二维码图片新增特性

###### 1、设置像素单位内边距 - Padding


###### 2、在二维码中间增加小图标 - CenterImage


###### 3、设置背景颜色 - BackgroundColor


###### 4、设置前景颜色 - ForegroundColor


###### 5、增加Bitmap处理处理接口 - bitmapProcessor

## 解析二维码图片


## 相机即时解码
