public class Zombie
{
  public int currentX, currentY;
  public String currentDirection;
  public FPSGUI fpsGUI;
  public double distanceFromNearestPlayer = 0;
  public boolean dead = false; //even though they are always dead

  public Zombie(FPSGUI givenGUI)
  {    
    fpsGUI = givenGUI;
    fpsGUI.zombies.add(this);

    spawn();

    fpsGUI.setZombie(currentX, currentY, currentDirection);
  }//constructor

  public void move()
  {
    currentDirection = directionToGo();

    int moveX = 0, moveY = 0;

    if      (currentDirection == null)     { moveX =  0; moveY =  0; }
    else if (currentDirection == "UP")     { moveX =  0; moveY = -1; }
    else if (currentDirection == "DOWN")   { moveX =  0; moveY =  1; }
    else if (currentDirection == "RIGHT")  { moveX =  1; moveY =  0; }
    else if (currentDirection == "LEFT")   { moveX = -1; moveY =  0; }
    
    int nextX = currentX + moveX;
    int nextY = currentY + moveY;

    if(fpsGUI.isInsideTheField(nextX,nextY))
    {
      if(fpsGUI.thereIsZombieAt(nextX,nextY) || fpsGUI.thereIsAmmoAt(nextX,nextY))
        currentDirection = swapDirection(currentDirection);
      else
      {
        if(fpsGUI.thereIsPlayerAt(nextX,nextY))
          fpsGUI.getPlayerAt(nextX,nextY).kill();

        currentX = nextX;
        currentY = nextY;
        fpsGUI.setZombie(currentX, currentY, currentDirection);

        fpsGUI.setFree(currentX - moveX, currentY - moveY);
      }//else
    }//if

  }//move

  public Player chasingPlayer()
  {
    Player playerBeingChased = fpsGUI.players[0];
    distanceFromNearestPlayer = distance(fpsGUI.players[0]);

    for(Player player : fpsGUI.players)
      if(!player.dead && distance(player) <= distanceFromNearestPlayer)
      {
        distanceFromNearestPlayer = distance(player);
        playerBeingChased = player;
      }//if

    return playerBeingChased;
  }//chasingPlayer

  public void kill()
  {
    dead = true;
    fpsGUI.setFree(currentX, currentY);
  }//kill

  public String swapDirection(String direction)
  {
    if(direction == "UP")    return "RIGHT";
    if(direction == "RIGHT") return "DOWN";
    if(direction == "DOWN")  return "LEFT";
    if(direction == "LEFT")  return "UP";

    return "UP";
  }//swapDirection

  public double distance(Player player)
  {
    return Math.sqrt(
            (currentX - player.currentX) * (currentX - player.currentX) + 
            (currentY - player.currentY) * (currentX - player.currentY));
  }//distance

  public String directionToGo()
  {
    Player chased = chasingPlayer();

    double random = Math.random();

    if(random <= 0.5)
    {   
       if(currentX > chased.currentX)
        return "LEFT";
      else if(currentX < chased.currentX)
        return "RIGHT";
      else
      {
        if(currentY < chased.currentY)
          return "DOWN";
        else
          return "UP";
      }//else
    }//if
    else
    {
      if(currentY < chased.currentY)
        return "DOWN";
      else if (currentY > chased.currentY)
        return "UP";
      else
      {
        if(currentX > chased.currentX)
          return "LEFT";
        else if(currentX < chased.currentX)
          return "RIGHT";
      }//else
    }//else

    return "UP";
  }//directionToGo

  public void spawn()
  {
    double random = Math.random();

    String wallToSpawn = "down";
    if      (random < 0.25) wallToSpawn = "left";
    else if (random < 0.50) wallToSpawn = "up";
    else if (random < 0.75) wallToSpawn = "right";
    else                    wallToSpawn = "down";

    int randomPoint = (int)(Math.random() * (fpsGUI.size - 1));

    if(wallToSpawn == "left")
    {
      if(!fpsGUI.thereIsZombieAt(0,randomPoint))
      {
        currentX = 0;
        currentY = randomPoint;
        currentDirection = "RIGHT";
      }//if
      else
        spawn();
    }//if
    else if(wallToSpawn == "up")
    {
      if(!fpsGUI.thereIsZombieAt(randomPoint,0))
      {
        currentX = randomPoint;
        currentY = 0;
        currentDirection = "DOWN";
      }//if
      else
        spawn();
    }//else if
    else if(wallToSpawn == "right")
    {
      if(!fpsGUI.thereIsZombieAt(fpsGUI.size - 1,randomPoint))
      {
        currentX = fpsGUI.size - 1;
        currentY = randomPoint;
        currentDirection = "LEFT";
      }//if
      else
        spawn();
    }//else if
    else if (wallToSpawn == "down")
    {
      if(!fpsGUI.thereIsZombieAt(randomPoint,fpsGUI.size - 1))
      {
      currentX = randomPoint;
      currentY = fpsGUI.size - 1;
      currentDirection = "UP";
      }//if
      else
        spawn();
    }//else if
    else
    {
      currentX = -1;
      currentY = -1;
      currentDirection = "UP";
    }//else
  }//spawn

}//class
