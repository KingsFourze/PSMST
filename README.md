# Phone SMS Transfer

## How To Setup Telegram bot

Before create the bot, You need a Mysql or MariaDB database server Running. And make sure your database can connect from outside of the Server. Also, get your telegram bot Token from [BotFather](https://t.me/botfather).

### Docker

Go to the [psmst Docker Page](https://hub.docker.com/r/kingsfourze/psmst)

### Ubuntu / Debian

1. Install Python, pip and wget : `sudo apt update && sudo apt install python3 python3-pip wget`
1. Install Library : `python3 -m pip install python-telegram-bot pymysql`
1. Download bot : `wget https://raw.githubusercontent.com/KingsFourze/psmst/main/DockerCreate/telegram_bot.py`
1. Run : `python3 telegram_bot.py [DB Host] [DB User] [DB Password] [DB name] [Telegram Bot Token]`

## How to use the Telegram bot

- First time you use the bot. You need the send a `/start` command.
- Using `/help` command can call the command list.
- Using `/getToken` command can get your token. If you havn't use before, it will generate a new token for you.
- Using `/delToken` to delete your token.

## How to use the app

1. Download and Install from [Release](https://github.com/KingsFourze/psmst/releases)
2. Get your token from telegram bot using /getToken command
3. Open the app
4. Input the database config and Token to tell your app how can it communicate to your database.
5. Press the save button.
- If you need to edit the config you need to press the stop button and press start button again for saving config.
