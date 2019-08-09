FROM centos:latest
LABEL maintainer "price clark <price@eucleo.com>"

#ARG GRAAL_VM_TAR_GZ=graalvm-ce-1.0.0-rc4-linux-amd64.tar.gz
ARG GRAAL_VM_TAR_GZ=graalvm-ce-linux-amd64-19.1.1.tar.gz
#ARG GRAAL_VM_DIR_NAME=graalvm-ce-1.0.0-rc4
ARG GRAAL_VM_DIR_NAME=graalvm-ce-19.1.1
#ARG GRAAL_URL=https://github.com/oracle/graal/releases/download/vm-1.0.0-rc4
ARG GRAAL_URL=https://github.com/oracle/graal/releases/download/vm-19.1.1
ARG USER_NAME=clj
ARG TARGET_PROJ=testgraal
ARG TARGET_JAR=testgraal-0.1.0-SNAPSHOT-standalone.jar
USER root

# install tools
RUN yum -y update
#RUN yum -y install zlib-devel java-1.8.0-openjdk-devel wget gcc nss
RUN yum -y install zlib-devel java-1.8.0-openjdk-devel wget gcc nss git
RUN yum -y update nss
RUN useradd -ms /bin/bash $USER_NAME; exit 0

#install clojure
RUN curl -O https://download.clojure.org/install/linux-install-1.10.1.447.sh
RUN chmod +x linux-install-1.10.1.447.sh
RUN ./linux-install-1.10.1.447.sh

#install lein
RUN mkdir -p /home/$USER_NAME/bin
WORKDIR /home/$USER_NAME/bin
RUN wget https://raw.github.com/technomancy/leiningen/stable/bin/lein
RUN chmod a+x /home/$USER_NAME/bin/lein

#install graal
RUN mkdir -p /opt/
WORKDIR /opt/
RUN wget $GRAAL_URL/$GRAAL_VM_TAR_GZ
RUN tar -zxvf $GRAAL_VM_TAR_GZ
RUN chown root:root /opt
RUN chmod 0755 /opt

#install boot
#RUN wget -O /home/$USER_NAME/bin/boot https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh
#RUN chmod +x /home/$USER_NAME/bin/boot
RUN bash -c "cd /usr/local/bin && curl -fsSLo boot https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh && chmod 755 boot"

#add app dir
RUN mkdir -p /home/$USER_NAME/app
COPY $TARGET_PROJ /home/$USER_NAME/app

#fix permissions and change to user $USER_NAME.
RUN chown -R $USER_NAME:$USER_NAME /home/$USER_NAME

USER $USER_NAME
ENV PATH=/home/$USER_NAME/bin:${PATH}
WORKDIR /home/$USER_NAME/app
RUN date
RUN export BOOT_JVM_OPTIONS="-Djavax.net.debug=SSL"
RUN boot uberjar
RUN find .
#RUN lein compile && lein uberjar
#RUN cp target/$TARGET_JAR .
#ENV PATH=/opt/$GRAAL_VM_DIR_NAME/bin:${PATH}
#RUN native-image \
		#-H:+ReportUnsupportedElementsAtRuntime \
		#-J-Xmx3G -J-Xms3G --no-server \
		#-jar $TARGET_JAR

RUN java -agentlib:native-image-agent=config-output-dir=config-output-dir/ -jar closh-zero.jar && check_times.clj && exit

RUN native-image \
	--no-server \
	--report-unsupported-elements-at-runtime \
	-H:ReflectionConfigurationFiles=reflectconfig \
	-H:ConfigurationResourceRoots=config-output-dir \
	-jar closh-zero.jar \
	 -H:Name=./target/closh-zero
		#-cp closh-zero.jar closh.zero.frontend.rebel \
		#-H:+ReportUnsupportedElementsAtRuntime \
		#-J-Xmx3G -J-Xms3G --no-server \
