from telegram.ext import Updater, CommandHandler, CallbackContext
import random, time, pymysql, threading, sys

def holdSQL():
    while True:
        cursor.execute("SELECT VERSION()")
        time.sleep(60)

def WaitingLock():
    global sqlLock
    while sqlLock:
        time.sleep(0.01)
    sqlLock = 1
    return 0

def messageTransfer():
    mTdb = pymysql.connect(host=sys.argv[1],user=sys.argv[2],password=sys.argv[3],database=sys.argv[4])
    mTcurcor = mTdb.cursor()
    while True:
        mTcurcor.execute("SELECT * FROM sms")
        data = mTcurcor.fetchall()
        for i in data:
            if mTcurcor.execute("SELECT chatID FROM userdata WHERE userToken = '" + i[0] + "'") == 1:
                chatID = mTcurcor.fetchone()[0]
                updater.bot.send_message(chat_id = chatID, text= i[1])
            mTcurcor.execute("DELETE FROM sms WHERE userToken = '" + i[0] + "' AND smsText = '" + i[1] + "'")
        mTdb.commit()
        time.sleep(5)


def randomGenToken():
    while True:
        token = ""
        random_str_seq = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for i in range(16):
            token += random_str_seq[random.randrange(len(random_str_seq))]
        if cursor.execute("SELECT * FROM userdata WHERE userToken = '"+token+"'") == 0:
            break
    return token

def startMessage(update, context):
    update.message.reply_text("Welcome for using Phone SMS Tranfer bot.\nUsing /help to get your help menu.")
def helpMessage(update, context):
    update.message.reply_text("=======  HELP  MENU  =======\n/getToken : Get Your Token.\n/delToken : Delete Your Token.")

def getToken(update, context):
    if cursor.execute("SELECT userToken FROM userdata WHERE chatID = " + str(update.message.chat.id)) > 0:
        data = cursor.fetchone()[0]
    else:
        data = randomGenToken()
        cursor.execute("INSERT INTO userdata (chatID, userToken) VALUES ("+ str(update.message.chat.id)+", '"+ data +"')")
        db.commit()
    update.message.reply_text("Your Token is: " + data)

def delToken(update, context):
    cursor.execute("DELETE FROM userdata WHERE chatID = " + str(update.message.chat.id))
    db.commit()
    update.message.reply_text("Your Token has deleted")

def hello(update, context):
    global targetID
    targetID = update.message.chat.id
    print("chatID:",update.message.chat.id,", say hello!")
    print(type(update.message.chat.id))
    context.bot.send_message(chat_id = update.message.chat.id, text="hello, {0}. Your ChatID is {1}".format(update.message.from_user.first_name, update.message.chat.id))
    
def setupDatabase():
    db = pymysql.connect(host=sys.argv[1],user=sys.argv[2],password=sys.argv[3])
    cursor = db.cursor()
    cursor.execute("CREATE DATABASE IF NOT EXISTS "+sys.argv[4]+" CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    cursor.execute("CREATE TABLE IF NOT EXISTS `"+sys.argv[4]+"`.`userdata` ( `chatID` INT NOT NULL , `userToken` VARCHAR(16) NOT NULL , PRIMARY KEY (`chatID`)) ENGINE = InnoDB;")
    cursor.execute("CREATE TABLE IF NOT EXISTS `"+sys.argv[4]+"`.`sms` ( `userToken` VARCHAR(16) NOT NULL , `smsText` VARCHAR(4096) NOT NULL) ENGINE = InnoDB;")
    db.commit()
    db.close()
    
def main():
    setupDatabase()

    global db, cursor
    #Setup MySQL Client
    db = pymysql.connect(host=sys.argv[1],user=sys.argv[2],password=sys.argv[3],database=sys.argv[4])
    cursor = db.cursor()
    threading.Thread(target=holdSQL).start()

    global updater
    #Setup Telegram Bot Listener
    updater = Updater(sys.argv[5])

    #Setup Transfer Thread
    threading.Thread(target=messageTransfer).start()

    #Setup Handler
    updater.dispatcher.add_handler(CommandHandler("start",startMessage))
    updater.dispatcher.add_handler(CommandHandler("getToken",getToken))
    updater.dispatcher.add_handler(CommandHandler("delToken",delToken))
    updater.dispatcher.add_handler(CommandHandler("help",helpMessage))

    #Start Waiting
    updater.start_polling()
    updater.idle()

main()
