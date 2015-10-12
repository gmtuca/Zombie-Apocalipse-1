import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class GameManager
{
  int n = 0;
  int numberOfRealPlayers;
  FPSGUI fpsGUI;
  Player[] players;
  Set<Player> playersAlive = new HashSet<Player>();
  Set<Bullet> allBullets = new HashSet<Bullet>();
  Set<Zombie> allZombies = new HashSet<Zombie>();
  int delay;

  public GameManager(FPSGUI givenFPSGUI, int givenPlayers, int speedDelay) throws Exception
  {
    if(givenPlayers <= 0) throw new IllegalArgumentException(
                     "Zero/Negative number of Players (" + givenPlayers + ")");

    if(speedDelay <= 0) throw new IllegalArgumentException("Zero/negative speed delay: " + speedDelay);
    if(givenFPSGUI == null) throw new NullPointerException("Invalid (null) interface");

    numberOfRealPlayers = givenPlayers;
    players = new Player[numberOfRealPlayers];

    fpsGUI = givenFPSGUI;

    fpsGUI.players = new Player[numberOfRealPlayers];

    delay = speedDelay;

    while(addPlayer()){};

    startGame();
  }//constructor

  int zombieMovementConstant = 0;
  int zombieSpawnConstant = 0;

  int ammoSpawnConstant = 0;

  public void startGame() throws Exception
  {

    for(Player player : players)
      playersAlive.add(player);

    while(!fpsGUI.start)
      Thread.currentThread().sleep(delay);

    while(true)
    {
      if(gg()) break;

     //if(fpsGUI.start)

      handlePlayers();
      handleBullets();
      handleZombies(5,8);
      handleAmmo(80);

      fpsGUI.ARROWS = null;
      fpsGUI.WASD   = null;

      for(int i = 0; i < numberOfRealPlayers; i++)
        fpsGUI.shootConstants[i] = false;

      fpsGUI.YGHJ   = null;
      fpsGUI.PLXX   = null;

      Thread.currentThread().sleep(delay);
    }//while

  }//startGame

  public void handlePlayers()
  {
    for(int i = 1; i <= numberOfRealPlayers; i++)
      if(getPlayer(i) != null && !getPlayer(i).dead)
      {
        getPlayer(i).move(movementConversion(i));
        if(getPlayer(i).currentDirection != null)
          getPlayer(i).lastDirection = getPlayer(i).currentDirection;
 
         getPlayer(i).currentDirection = null;

         if(fpsGUI.shootConstants[i - 1])
         {
           Bullet bulletShot = getPlayer(i).shoot(getPlayer(i).lastDirection);
           if(bulletShot != null)
             allBullets.add(bulletShot);
         }//if
       }//if
       else
         playersAlive.remove(getPlayer(i));

  }//handlePlayers

  public void handleBullets()
  {
    Iterator<Bullet> bulletsIterator = allBullets.iterator();
    Set<Bullet> removalSet = new HashSet<Bullet>();
    while(bulletsIterator.hasNext())
    {
      Bullet foundBullet = bulletsIterator.next();
      if(foundBullet.isOutOfTheField())
        removalSet.add(foundBullet);
      else
        foundBullet.move();
    }//while
    allBullets.removeAll(removalSet);
  }//handleBullets

  public void handleZombies(int zombieMovement, int zombieSpawn)
  {
    if(zombieMovementConstant++ == zombieMovement)
    {
      Set<Zombie> removalSetZ = new HashSet<Zombie>();
      Iterator<Zombie> zombieIterator = allZombies.iterator();
      while(zombieIterator.hasNext())
      {
        Zombie zombieFound = zombieIterator.next();
        if(zombieFound.dead)
          removalSetZ.add(zombieFound);
        else
          zombieFound.move();
      }//while
      allZombies.removeAll(removalSetZ);
      fpsGUI.zombies.removeAll(removalSetZ);
      zombieMovementConstant = 0;
    }//if

    if(zombieSpawnConstant++ == zombieSpawn)
    {
      zombieSpawnConstant = 0;
      allZombies.add(new Zombie(fpsGUI));
    }//zombieConstant
  }//handleZombies

  public void handleAmmo(int ammoSpawn)
  {
    if(ammoSpawnConstant++ == ammoSpawn)
    {
      ammoSpawnConstant = 0;
      fpsGUI.ammos.add(new Ammo(10,fpsGUI));
    }//
  }//handleAmmo


  public void restart() throws Exception
  {
    fpsGUI.clearAll();
    fpsGUI.start = false;

    allZombies.removeAll(allZombies);
    allBullets.removeAll(allBullets);

    for(int p = 1; p <= numberOfRealPlayers; p++)
      getPlayer(p).respawn(getSpawnX(p), getSpawnY(p), getInitialDirection(p));

    startGame();
  }//restart

  public boolean addPlayer() throws Exception
  {
    if(n == numberOfRealPlayers)
      return false;

    n++;

    players[n - 1] = new Player(n, numberOfRealPlayers, getSpawnX(n),
                                getSpawnY(n), getInitialDirection(n), fpsGUI);

    fpsGUI.players[n - 1] = players[n - 1];
    return true;
  }//add

  public boolean gg()
  {
    if(playersAlive.size() == 0)
    {
      JOptionPane.showMessageDialog(new JFrame(){}, " GAME" + scoreScreen());
      return true;
    }//if
    else
      return false;
  }//allDead

  public String scoreScreen()
  {
    String scoreScreen = "\n\n Zombie Kills \n";
    for(Player player : players)
      scoreScreen += player + ": " + player.score + "\n";

    return scoreScreen;     
  }//scoreScreen

  public String movementConversion(int playerNumber)
  {
    if(playerNumber == 1)
    {
      if     (fpsGUI.ARROWS == "^") return "UP";
      else if(fpsGUI.ARROWS == "<") return "LEFT";
      else if(fpsGUI.ARROWS == "v") return "DOWN";
      else if(fpsGUI.ARROWS == ">") return "RIGHT";
      else  return getPlayer(1).currentDirection;
    }//if
    else if(playerNumber == 2)
    {
      if     (fpsGUI.WASD == "w") return "UP";
      else if(fpsGUI.WASD == "a") return "LEFT";
      else if(fpsGUI.WASD == "s") return "DOWN";
      else if(fpsGUI.WASD == "d") return "RIGHT";
      else  return getPlayer(2).currentDirection;
    }//if
    else if(playerNumber == 3)
    {
      if     (fpsGUI.YGHJ == "y") return "UP";
      else if(fpsGUI.YGHJ == "g") return "LEFT";
      else if(fpsGUI.YGHJ == "h") return "DOWN";
      else if(fpsGUI.YGHJ == "j") return "RIGHT";
      else  return getPlayer(3).currentDirection;
    }//if
    else if(playerNumber == 4)
    {
      if     (fpsGUI.PLXX == "p") return "UP";
      else if(fpsGUI.PLXX == "l") return "LEFT";
      else if(fpsGUI.PLXX == ";") return "DOWN";
      else if(fpsGUI.PLXX == "'") return "RIGHT";
      else  return getPlayer(4).currentDirection;
    }//if

    return "UP";
  }//movementConversion

  public String getInitialDirection(int playerNumber)
  {
    if(playerNumber == 1) 
      return "RIGHT";
    
    if(playerNumber == 2)
      if(numberOfRealPlayers == 2)
        return "LEFT";
      else if(numberOfRealPlayers == 3)
        return "UP";
      else if(numberOfRealPlayers == 4)
        return "DOWN";

    if(playerNumber == 3)
      if(numberOfRealPlayers == 3)
        return "LEFT";
      else if(numberOfRealPlayers == 4)
        return "UP";

    if(playerNumber == 4)
      return "LEFT";

    return "RIGHT";
  }//getInitialDirection

  public int getSpawnX(int playerNumber)
  {
    int gridSize = fpsGUI.size;
 
    if(numberOfRealPlayers == 1)
      return gridSize / 2;
    else if(numberOfRealPlayers == 2)
    {
      if(playerNumber == 1)
        return gridSize / 4;
      else
        return 3 * gridSize / 4;
    }//else if
    else if(numberOfRealPlayers == 3)
    {
      if(playerNumber == 1)
        return gridSize / 2;
      else if(playerNumber == 2)
        return gridSize / 4;
      else if(playerNumber == 3)
        return 3 * gridSize / 4;
    }//else if
    else if(numberOfRealPlayers == 4)
    {
      if(playerNumber == 1 || playerNumber == 3)
        return gridSize / 4;
      else if(playerNumber == 2 || playerNumber == 4)
        return 3 * gridSize / 4;
    }//else if

    return 0;
  }//getSpawnX

  public int getSpawnY(int playerNumber)
  {
    int gridSize = fpsGUI.size;

    if(numberOfRealPlayers == 1 || numberOfRealPlayers == 2)
      return gridSize / 2;
    else if(numberOfRealPlayers == 3)
    {
      if(playerNumber == 1)
        return gridSize / 4;
      else if(playerNumber == 2 || playerNumber == 3)
        return 3 * gridSize / 4; 
    }//else if
    else if(numberOfRealPlayers == 4)
    {
      if(playerNumber == 1 || playerNumber == 2)
        return gridSize / 4;
      else if(playerNumber == 3 || playerNumber == 4)
        return 3 * gridSize / 4;
    }//else if

    return 0;
  }//getSpawnY

  public Player getPlayer(int playerNumber)
  {
    return players[playerNumber - 1];
  }//getPlayer

}//GameManager
