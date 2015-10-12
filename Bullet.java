public class Bullet
{
  public String direction;
  public int currentX, currentY;
  public int moveX, moveY;
  public FPSGUI fpsGUI;
  public Player shooter;

  public Bullet(Player shootingPlayer, String givenDirection, int givenX, int givenY, FPSGUI givenGUI)
  {
    shooter = shootingPlayer;
    direction = givenDirection;
    currentX = givenX;
    currentY = givenY;
    fpsGUI = givenGUI;

    if      (direction == "UP")     { moveX =  0; moveY = -1; }
    else if (direction == "DOWN")   { moveX =  0; moveY =  1; }
    else if (direction == "RIGHT")  { moveX =  1; moveY =  0; }
    else if (direction == "LEFT")   { moveX = -1; moveY =  0; }

  }//constructor

  public void move()
  {
    currentX += moveX;
    currentY += moveY;

    fpsGUI.setBullet(currentX, currentY, direction);
   
    fpsGUI.setFree(currentX - moveX, currentY - moveY);

    if(fpsGUI.thereIsPlayerAt(currentX,currentY))
    {
      fpsGUI.getPlayerAt(currentX, currentY).kill();
      getsDestroyed();
    }//if    

    if(fpsGUI.thereIsZombieAt(currentX,currentY))
    {     
      fpsGUI.getZombieAt(currentX, currentY).kill();
      getsDestroyed();
      shooter.score++;
    }//if
  }//move

  public boolean isOutOfTheField()
  {
    return !fpsGUI.isInsideTheField(currentX, currentY);
  }//outOfTheField

  public void getsDestroyed()
  {
    currentX = -1;
    currentY = -1;
  }//getsDestroyed
}//class
