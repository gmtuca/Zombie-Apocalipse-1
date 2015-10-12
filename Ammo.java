public class Ammo
{
  public int currentX = 0, currentY = 0, amount;
  public FPSGUI fpsGUI;
  public boolean stillHere = true;
  
  public Ammo(int givenAmount, FPSGUI givenGUI)
  {
    amount = givenAmount;
    fpsGUI = givenGUI;
    spawn();

    fpsGUI.ammos.add(this);
  }//constructor

  public void spawn()
  {
    int randomX = (int)(Math.random() * fpsGUI.size);
    int randomY = (int)(Math.random() * fpsGUI.size);

    if(fpsGUI.gridIsFree(randomX,randomY))
    {
      currentX = randomX;
      currentY = randomY;
      fpsGUI.setAmmo(currentX, currentY);
    }//if
    else
      spawn();
  }//spawn

  public void getsDestroyed()
  {
    currentX = -1;
    currentY = -1;
    stillHere = false;
    fpsGUI.ammos.remove(this);
  }//getsDestroyed

}//class
