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
$ ./gradlew run-server
```

### Start a client (player)
In a new terminal, run the following command:
```bash
$ ./gradlew run-client
```
Repeat the above command to start more clients.

## Game play
---

When all clients are connected, the server will start the game automatically. Once the game starts, the goal is to conquer all the territories on the map. The player who conquers all the territories first wins the game. 

Each territory produces a number of food and technology resources.

Each player has the option to move, attack, research, and upgrade. 
- ***Move***: specify the level and the number of armies to move, the source territory, and the destination territory. This order costs food resources. (command + M or click on two territories to make the Move)
- ***Attack***: specify the level and the number of armies to attack, the source territory, and the destination territory. Each attack costs food resources as a function of the number of armies attacking and distance between the two territories. (command + A or click on two territories to make the Attack)
- ***Research***: an order to increase the "technology level" of the player. Each research costs resources. Upgrade will be effective in the next round.
- ***Upgrade***: specify the level and number of armies to upgrade, the target territory. Upgrading a unit increases its combat bonuses in combat resolution. This order costs technology resources. (command + U or click on one territory twice to make the Upgrade)
- ***Manufacture***: specify what to build(bomb or aircraft). (command + K)
- ***Alliance***: specify which player to form alliance with. 

## New feature specification
1. **Airborne:** Users can consume 1000 Technology resources to build one aircraft. Therefore, when doing move orders on units with Airborne Level, they can move to any territory the owner owns no matter if there exists a path. When issuing attack orders on units with Airborne Level, they can attack any territory on the map. 
   * aircraft can only take Airborne units.
   * One aircraft can only be used for 3 times. 
   * One aircraft can only take up to 10 Airborne units.
2. **Ultron:** Ultron units can be equipped with bombs. Bombs can also be built using technology resources. When a user attacks a territory with Ultron units without equipping them with bombs. Ultron units participate in combat with normal combat resolution. If Ultron units are equipped with bombs, it will blow up 5 opponent unit during one-to-one combat when only one side has bomb. If both sides have bombs during one-to-one combat, both sides will lose 3 units. 


![Alt text](image.png)
