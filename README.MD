<h1 align="center">HelpThreadBot</h1>

<p align="center">
<img src="https://img.shields.io/github/forks/BurntRouter/HelpThreadBot">
<img src="https://img.shields.io/github/stars/BurntRouter/HelpThreadBot">
<img src="https://img.shields.io/github/license/BurntRouter/HelpThreadBot">
</p>
<h2 align="center">A bot for managing help threads on Discord.</h2>
<img src="https://i.imgur.com/p4O1hLW.png">
<br>
<br>
<h3>Requirements</h3>
```
Java 17
< Gradle 7.3.1
JDA 5.0.0-alpha.5
MariaDB/MySQL Server
```

<h3>Building</h3>
This is as simple as could be.
Run: `./gradlew uberJar`
The built file will be in `.\build\libs\HelpThreadBot-x.x.x-uber.jar`

<h3>Running</h3>
Simply run the file and it will create a `config.json` file for you.
`java -jar HelpThreadBot-x.x.x.-uber.jar`

<h3>Configuration</h3>
This bot is designed to be as configurable as possible without a modifying the code.
Upon generating a config file you will be greeted with

```
{
 "db_name": "HelpThreadBot",
 "faq_link": "FAQ LINK HERE",
 "db_user": "root",
 "manager_role_id": "ROLE ID",
 "db_port": "3306",
 "db_pass": "password",
 "guildid": "GUILDID HERE",
 "db_hostname": "127.0.0.1",
 "channelid": "CHANNEL ID HERE",
 "token": "DISCORD TOKEN HERE"
}
```

`db_name` - Name of database in MariaDB/MySQL
`faq_link` - Link to FAQ message in Discord (Optional)
`db_user` - Username to access database server
`manager_role_id` - Role ID of trusted members that are allowed to close other peoples' threads (Optional)
`db_port` - Port of your database server
`db_pass` - Password of your database server
`guildid` - Id of the Discord Guild you're using the bot in
`db_hostname` - Hostname/IP of your database server
`channel_id` - Channel for your help threads
`token` - Discord token for the bot`

<h4>Todo</h4>

I'd say I plan on writing better docs but we both know that isn't going to happen...
