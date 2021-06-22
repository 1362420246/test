#基于docker images中的java tag为8-jdk的镜像
FROM java:8
#把当前项目下docker-plugin-1.0.0.jar改名为docker-plugin.jar 添加到镜像中
ADD docker-plugin-1.0.0.jar docker-plugin.jar
#指定端口,最好写与项目配置的端口
EXPOSE 8090
#在镜像中运行docker-plugin.jar包,这样在运行镜像的时候就已经启动好了docker-plugin.jar
ENTRYPOINT [ "java",  "-jar", "/docker-plugin.jar" ]