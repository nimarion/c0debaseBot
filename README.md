[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e09f05b8e67c4156accc633d72e8a75d)](https://www.codacy.com/app/biosphere.dev/c0debaseBot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Biospheere/c0debaseBot&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/Biospheere/c0debaseBot.svg?branch=master)](https://travis-ci.org/Biospheere/c0debaseBot)
[![GitHub contributors](https://img.shields.io/github/contributors/biospheere/c0debaseBot.svg)](https://github.com/Biospheere/c0debaseBot/graphs/contributors/)
[![c0debase Discord](https://discordapp.com/api/guilds/361448651748540426/embed.png)](https://discord.gg/BDwBeZ3)


# c0debaseBot 

<table>
<tr>
<td>
  A Discord bot for the c0debase Discord server which provides different commands
</td>
<img align="right" src="https://i.imgur.com/Hg1Eekk.png">

</tr>
</table>

## Cloning and first build

```
git clone git@github.com:Biospheere/c0debaseBot.git # or https://github.com/Biospheere/c0debaseBot.git
cd c0debaseBot
mvn package
```

## Start 
 ```docker build -t . biospheere/codebasebot ```
 
  The application requires certain environment variables to be set in a .env file (instead of command line arguments).
  
  ``` 
  DISCORD_TOKEN=
  BOTCHANNEL=
  MONGO_HOST=db
  ```
  
  You need [Docker Compose](https://docs.docker.com/compose/) for the next step!
  
  ```docker-compose up```
  
  There you go ;)
  
  
## Built with 

- [JDA](https://github.com/DV8FromTheWorld/JDA) - Java wrapper for the popular chat & VOIP service  [@discordapp](https://github.com/discordapp)
- [Maven](https://maven.apache.org/) 
- [Sentry](https://sentry.io/)
- [MongoDB](https://www.mongodb.com/)


## [License](https://github.com/Biospheere/c0debaseBot/blob/master/LICENSE)

MIT © [Niklas](https://github.com/Biospheere/)
 

