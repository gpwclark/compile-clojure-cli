FROM oracle/graalvm-ce:19.2.0
RUN yum -y update
RUN yum -y install java-1.8.0-openjdk wget
RUN gu install native-image

RUN mkdir -p /home/clj/bin
RUN mkdir -p /home/clj/testgraal

#install lein
WORKDIR /home/clj/bin
RUN wget https://raw.github.com/technomancy/leiningen/stable/bin/lein
RUN chmod +x lein

WORKDIR /home/clj/testgraal
COPY . .
USER root
RUN PATH=$PATH:/home/clj/bin lein native-image

ENTRYPOINT ["./target/testgraal.bin"]
CMD []
