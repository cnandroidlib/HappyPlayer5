# 简介 #
乐乐音乐5.0主要是基于ijkplayer框架开发的Android音乐播放器，它支持多种音频格式和动感歌词，界面高仿酷狗。

# 开发环境 #
- android studio 3.0
- gradle-4.1-milestone-1-all
- 小米2s手机
- android 5.0系统

# 音频格式 #
目前用ijkplayer来测试如下音频格式，均可正常播放：aac，amr，ape，flac，m4r，mmf，mp2，mp3，ogg，wav，wma，wv

# 歌词格式 #
- krc：酷狗歌词
- ksc：卡拉OK歌词
- hrc：happy lyrics歌词，乐乐音乐自定义的动感歌词格式，可准确到歌词每个字。
- hrcx：hrc歌词的优化

# 功能 #
- 底部播放菜单固定，具体参考SlidingMenu和结合Fragment来实现
- 底部播放菜单左右滑动显示/关闭双行歌词
- 歌词界面旋转移动和关闭
- 动感歌词、多行歌词、歌词快进、歌词时间微调、歌词换行、字体大小、歌词颜色切换和歌词透明度上下渐变 
- 多行歌词平滑移动，快速流畅滑动。具体使用Scroller来实现。注：Scroller只做动画，不要用来移动view
- 窗口右滑关闭
- 歌曲边缓存边播放

# 效果图 #
- 主界面

![](https://i.imgur.com/BeTErXg.png)
![](https://i.imgur.com/RC79uMU.png)
![](https://i.imgur.com/vFU57au.png)
![](https://i.imgur.com/t4Lq4J0.png)
![](https://i.imgur.com/W33afTx.png)
![](https://i.imgur.com/U2AdVMC.png)
![](https://i.imgur.com/Y3pUvMQ.png)
![](https://i.imgur.com/TFaCRc3.png)

- 歌手写真界面

![](https://i.imgur.com/NjdUuEp.png)
![](https://i.imgur.com/YjVxcnR.png)
![](https://i.imgur.com/p1Emexh.png)
![](https://i.imgur.com/FL7OiCq.png)
![](https://i.imgur.com/cFqiFf7.png)
![](https://i.imgur.com/CZtMA6F.png)


# 安装包和资源文件 #
链接：[http://pan.baidu.com/s/1qXTd8mg](http://pan.baidu.com/s/1qXTd8mg) 密码：x0yz

# 传送门 #

- [ijkplayer开源框架](https://github.com/Bilibili/ijkplayer "ijkplayer开源框架")
- [Hrc/hrcx歌词制作器（播放器）](https://github.com/zhangliangming/HappyPlayer-PC.git "Hrc/Hrcx歌词制作器（播放器）")
- [Krc、Ksc、Hrc/hrcx歌词解析器](https://github.com/zhangliangming/LyricsAnalyze.git "krc、ksc、hrc/hrcx歌词解析器")
- [浅谈动感歌词](http://zhangliangming.github.io/ "浅谈动感歌词")

# 声明 #
仅用于学习用途

# 联系方式 #
316257874@qq.com
