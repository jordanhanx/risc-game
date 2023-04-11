ECE 651 Team 7: World conquering game (Risk)
=======================================

![pipeline](https://gitlab.oit.duke.edu/xh123/ece651-sp23-team7-risk/badges/master/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/xh123/ece651-sp23-team7-risk/badges/master/coverage.svg?job=test)

 ## Coverage
[Detailed coverage](https://xh123.pages.oit.duke.edu/ece651-sp23-team7-risk/dashboard.html)


## How to run
---
### Start the server
In a terminal, run the following command:
```bash
$ ./gradlew :server:run --args "<port number><number of players><initial number of armies>"
```

### Start a client (player)
In a new terminal, run the following command:
```bash
$ ./gradlew :client:run --args "<player name> <port number>"
```
Repeat the above command to start more clients.

## Game play
---

When all clients are connected, the server will start the game automatically. Once the game starts, the goal is to conquer all the territories on the map. The player who conquers all the territories first wins the game. 

Each territory produces a number of food and technology resources.

Each player has the option to move, attach, research, and upgrade. 
- ***Move***: specify the number of armies to move, the source territory, and the destination territory. This order costs food resources.
- ***Attack***: specify the number of armies to attack, the source territory, and the destination territory. Each attack costs food resources as a function of the number of armies attacking and distance between the two territories.
- ***Research***: an order to increase the "technology level" of the player. Each research costs resources.
- ***Upgrade***: upgrading a unit increases its combat bonuses in combat resolution. This order costs technology resources.


![Alt text](https://cdn2.inkarnate.com/cdn-cgi/image/width=1800,height=1200/https://inkarnate-api-as-production.s3.amazonaws.com/LoR7Kpr6BBB7TfdEYFdcLr)