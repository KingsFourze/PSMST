FROM alpine:latest
MAINTAINER KingsFourze
RUN apk add python3 py3-pip
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
CMD ["sh","startbot.sh"]
