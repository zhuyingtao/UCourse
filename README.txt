
   本程序分服务器端和客户端两部分，如需上传下载课件，则需先运行UCourse-Server,否则会连接失败。

   1.由于Client端依赖SlidingMenu，导入时应先将SlidingMenu导入，然后设置为Client端的lib。

   2.由于服务器端没有域名，实际是根据IP来连接的，因此应在com.ustc.gcsj.doc.CONSTANT.java里修改实际的IP地址。

   3.服务器端的根文件目录需要在Server.java里面修改。

   4.如自身SDK版本较低，可在project.properties里修改target属性，默认为android-19