FROM ubuntu:latest
MAINTAINER KingsFourze
RUN sed -i "s/archive.ubuntu.com/ftp.ubuntu-tw.org/g" /etc/apt/sources.list
RUN apt update 
RUN apt install python3 python3-pip -y
RUN python3 -m pip install python-telegram-bot pymysql
ENV DBHOST=localhost
ENV DBUSER=root
ENV DBNAME=PSMST
ENV DBPASSWORD=PSMSTPassword
ENV TGTOKEN=321:123abc
WORKDIR /app
COPY ./telegram_bot.py .
COPY ./startbot.sh .
RUN chmod 700 startbot.sh
CMD ["bash","startbot.sh"]
