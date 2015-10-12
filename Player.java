import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Player
{
  public FPSGUI fpsGUI;
  public int playerNumber;
  public int currentX, currentY;
  public boolean dead = false;
  public int score;
  public String currentDirection;
  public String lastDirection;
  public int ammo;

  public Player(int givenPlayerNumber, int playerOutOf,
                int spawnX, int spawnY, String givenInitialDirection, FPSGUI givenfpsGUI) throws Exception
  {
    fpsGUI = givenfpsGUI;
    playerNumber = givenPlayerNumber;
    currentX = spawnX;
    currentY = spawnY;

    lastDirection = givenInitialDirection;
    score = 0;
    ammo = 20;

    fpsGUI.setPlayer(currentX, currentY, lastDirection);
  }//constructor

  public void respawn(int x, int y, String givenDirection)
  {
    dead = false;
    currentX = x;
    currentY = y;
    currentDirection = givenDirection;

    fpsGUI.setPlayer(x, y, givenDirection);
  }//teleport

  public void move(String direction)
  {
    int moveX = 0, moveY = 0;

    if      (direction == null)     { moveX =  0; moveY =  0; }
    else if (direction == "UP")     { moveX =  0; moveY = -1; }
    else if (direction == "DOWN")   { moveX =  0; moveY =  1; }
    else if (direction == "RIGHT")  { moveX =  1; moveY =  0; }
    else if (direction == "LEFT")   { moveX = -1; moveY =  0; }

    currentDirection = direction;
    
    int nextX = currentX + moveX;
    int nextY = currentY + moveY;

    if(nextY > 0 && fpsGUI.gridIsFree(nextX, nextY - 1))
      fpsGUI.imageLabels[nextY - 1][nextX].setText("| " + ammo + " |");

    if(currentY > 0 && direction != null)
      fpsGUI.imageLabels[currentY - 1][currentX].setText("");

    if(!fpsGUI.thereIsPlayerAt(nextX,nextY) && fpsGUI.isInsideTheField(nextX,nextY))
    {
      if(fpsGUI.thereIsAmmoAt(nextX,nextY))
      {
        Ammo ammoFound = fpsGUI.getAmmoAt(nextX,nextY);
        ammo += ammoFound.amount;
        ammoFound.getsDestroyed();
      }//if

      fpsGUI.setPlayer(nextX, nextY, currentDirection);
      currentX = nextX;
      currentY = nextY;
    }//if
    else
      fpsGUI.setPlayer(currentX, currentY, currentDirection);

    if(direction != null)
      fpsGUI.setFree(currentX - moveX, currentY - moveY);


  }//moveTo

  public Bullet shoot(String direction)
  {
    if(ammo > 0)
    {
      ammo--;

      int moveX = 0, moveY = 0;

      if      (direction == null)     { moveX =  0; moveY =  0; }
      else if (direction == "UP")     { moveX =  0; moveY = -1; }
      else if (direction == "DOWN")   { moveX =  0; moveY =  1; }
      else if (direction == "RIGHT")  { moveX =  1; moveY =  0; }
      else if (direction == "LEFT")   { moveX = -1; moveY =  0; }

      return new Bullet(this, direction, currentX + moveX, currentY + moveY, fpsGUI);
    }//if
    return null;
  }//shoot

  public void kill()
  {
    dead = true;
    fpsGUI.setFree(currentX, currentY);
    if(fpsGUI.gridIsFree(currentX, currentY - 1))
      fpsGUI.setFree(currentX, currentY - 1);
  }//kill

  public void resetScore()
  {
    score = 0;
  }//resetScore

  public void teleportTo(int x, int y)
  {
    currentX = x;
    currentY = y;
  }//teleportTo

  @Override
  public String toString()
  {
    return "Player " + playerNumber;
  }//toString

}//class
