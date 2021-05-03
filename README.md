# Phone SMS Transfer to Telegram

## Why I Developed It

I have two mobile phones, but most of the time I don't bring both phones out. Sometimes I need to receive SMS from the mobile at home. I have tried using IFTTT, but the response is too slow, so I need a faster response application. Then I make it.

## Setup

### How To Setup Telegram Bot

Before create the bot, You need a **Mysql or MariaDB** database server running. And make sure your database can connect from **Outside Of The Server**. Also, get your telegram bot Token from [BotFather](https://t.me/botfather).

#### Docker

Go to the [psmst Docker Page](https://hub.docker.com/r/kingsfourze/psmst)

#### Ubuntu / Debian

1. Install Python, pip and wget : `sudo apt update && sudo apt install python3 python3-pip wget`
1. Install Library : `python3 -m pip install python-telegram-bot pymysql`
1. Download bot : `wget https://raw.githubusercontent.com/KingsFourze/psmst/main/DockerCreate/telegram_bot.py`
1. Run : `python3 telegram_bot.py [DB Host] [DB User] [DB Password] [DB name] [Telegram Bot Token]`

### How To Use The Telegram Bot

- First time you use the bot. You need the send a `/start` command.
- Using `/help` command can call the command list.
- Using `/getToken` command can get your token. If you havn't use before, it will generate a new token for you.
- Using `/delToken` to delete your token.

### How To Use The App

1. Download and Install from [Release](https://github.com/KingsFourze/psmst/releases)
2. Get your token from telegram bot using /getToken command
3. Open the app
4. Input the database config and Token to tell your app how can it communicate to your database.
5. Press the start button.

If you need to edit the config, you can press save button for saving config after version 1.1. But before version 1.1, you need to press stop button and press start button for saving.
