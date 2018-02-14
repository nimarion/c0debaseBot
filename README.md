[![Build Status](https://travis-ci.org/Biospheere/c0debaseBot.svg?branch=master)](https://travis-ci.org/Biospheere/c0debaseBot)
[![GitHub contributors](https://img.shields.io/github/contributors/biospheere/c0debaseBot.svg)](https://github.com/Biospheere/c0debaseBot/graphs/contributors/)

# c0debaseBot 

c0debaseBot ist ein Discord Bot für den c0debase Discord.

### Installation

1. Lade dir die .env Datei herunter und setze die Daten ein. Den Hostname für den MySQL Server must du auf db setzen
2. Erstelle den Ordner /opt/mysql 
3. sudo docker run --name mysql -v /opt/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=HIER_DEIN_PASSWORT -d mysql
4. sudo docker run --env-file .env --link mysql:db -d biospheere/codebasebot


[![Join c0debase Discord](https://discordapp.com/api/guilds/361448651748540426/embed.png?style=banner2)](https://discord.gg/BDwBeZ3)
[![Docker](http://dockeri.co/image/biospheere/codebasebot)](https://hub.docker.com/r/biospheere/codebasebot/)
