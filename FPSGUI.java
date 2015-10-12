import javax.swing.ImageIcon;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class FPSGUI extends JFrame implements KeyListener
{
  public int size;
  public JPanel[][] grid;
  public JLabel[][] imageLabels;

  public boolean start = true;

  public boolean[] shootConstants = new boolean[4];

  public Player[] players;
  public Set<Zombie> zombies = new HashSet<Zombie>();
  public Set<Ammo> ammos = new HashSet<Ammo>();

  public String ARROWS = null;
  public String WASD   = null;
  public String YGHJ   = null;
  public String PLXX   = null;

  public Color freeSpaceColor = Color.BLACK;
 
  public Container contents;

  public FPSGUI(int gridSize, int dimensionX, int dimensionY) throws Exception
  {
    if(gridSize <= 0) throw new IllegalArgumentException("Zero/Negative grid size");
    if(dimensionX <= 0 || dimensionY <= 0) throw new IllegalArgumentException("Zero/Negative grid pixel dimensions");
  
    size = gridSize;
    grid = new JPanel[size][size];

    imageLabels = new JLabel[size][size];

    addKeyListener(this);
    setTitle("FPS");
    contents = getContentPane();
    contents.setLayout(new GridLayout(size , size));
    contents.setPreferredSize(new Dimension(dimensionX, dimensionY));

    for(int y = 0; y < size; y++)
      for(int x = 0; x < size; x++)
      {
        grid[y][x] = new JPanel();
        grid[y][x].setSize(30,30);
        grid[y][x].setBackground(freeSpaceColor);
        
        imageLabels[y][x] = new JLabel();
        grid[y][x].add(imageLabels[y][x]);
      }//for

    for(int y = 0; y < size; y++)
       for(int x = 0; x < size; x++)
         contents.add(grid[y][x]);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }//constructor

  public void setPlayer(int x, int y, String direction)
  {
    if(isInsideTheField(x,y))
      if(direction == "UP" || direction == "DOWN" || direction == "RIGHT" || direction == "LEFT" )
        imageLabels[y][x].setIcon(new ImageIcon("images/marineSprites/marine" + direction + ".png"));
  }//setPlayer

  public void setZombie(int x, int y, String direction)
  {
    if(isInsideTheField(x,y))
      if(direction == "UP" || direction == "DOWN" || direction == "RIGHT" || direction == "LEFT" )
        imageLabels[y][x].setIcon(new ImageIcon("images/zombieSprites/zombie1/zombie" + direction + ".png"));
  }//setPlayer

  public boolean thereIsPlayerAt(int x, int y)
  {
    if(!isInsideTheField(x,y)) return false;

    for(Player player : players)
      if(player.currentX == x && player.currentY == y) return true;

    return false;
  }//ifPlayer

  public boolean thereIsZombieAt(int x, int y)
  {
    if(!isInsideTheField(x,y)) return false;

    for(Zombie zombie : zombies)
      if(zombie.currentX == x && zombie.currentY == y) return true;

    return false;
  }//ifPlayer

  public boolean isInsideTheField(int x, int y)
  {
    return y >= 0 && x >= 0 && y < size && x < size;
  }//isInsideTheField

  public Player getPlayerAt(int x, int y)
  {
    for(Player player : players)
      if(player.currentX == x && player.currentY == y) return player;

    return null;
  }//getPlayerAt

  public Zombie getZombieAt(int x, int y)
  {
    for(Zombie zombie : zombies)
      if(zombie.currentX == x && zombie.currentY == y) return zombie;

    return null;
  }//getPlayerAt

  public ImageIcon udBullet = new ImageIcon("images/lineSprites/lineUPDOWN.png");
  public ImageIcon lrBullet = new ImageIcon("images/lineSprites/lineLEFTRIGHT.png");

  public void setBullet(int x, int y, String direction)
  {
    if(isInsideTheField(x,y))
    { 
      if(direction == "UP" || direction == "DOWN")
        imageLabels[y][x].setIcon(udBullet);
      else if(direction == "LEFT" || direction == "RIGHT")
        imageLabels[y][x].setIcon(lrBullet);
    }//if
  }

  public void setAmmo(int x, int y)
  {
    if(isInsideTheField(x,y))
      imageLabels[y][x].setIcon(new ImageIcon("images/ammo.png"));
  }//

  public Ammo getAmmoAt(int x, int y)
  {
    for(Ammo ammo : ammos)
      if(ammo.currentX == x && ammo.currentY == y) return ammo;

    return null;
  }//

  public boolean thereIsAmmoAt(int x, int y)
  {
    if(!isInsideTheField(x,y)) return false;

    for(Ammo ammo : ammos)
      if(ammo.currentX == x && ammo.currentY == y) return true;

    return false;
  }//

  public void clearAll()
  {
    for(int x = 0; x < size; x++)
      for(int y = 0; y < size; y++)
        setFree(x,y);

    zombies.removeAll(zombies);
    ammos.removeAll(ammos);

    ARROWS = null;
    WASD   = null;
    YGHJ   = null;
    PLXX   = null;
  }//clearAll

  public boolean gridIsFree(int x, int y)  {
    if(y < 0 || x < 0 || y >= size || x >= size)
      return false;

    return !thereIsPlayerAt(x,y) && !thereIsZombieAt(x,y);
    //return grid[y][x].getBackground() == freeSpaceColor;
  }

  public void setFree(int x, int y) {
   if(y >= 0 && x >= 0 && y < size && x < size)
   {
     if(thereIsPlayerAt(x,y))
       getPlayerAt(x,y).teleportTo(-1,-1);

    grid[y][x].setBackground(freeSpaceColor);
    imageLabels[y][x].setIcon(null);
    imageLabels[y][x].setText("");
   }
  }//gridIsFree

  public void keyTyped(KeyEvent keyEvent) {
    //System.out.println("KEY TYPED");
  }//keyTyped

  public void keyPressed(KeyEvent keyEvent) {
    if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE)
      start = !start;
    else 
    {
      if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
        System.exit(0);

      if(keyEvent.getKeyCode() == KeyEvent.VK_UP)
        ARROWS = "^";
      if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN)
        ARROWS = "v";
      if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT)
        ARROWS = "<";
      if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
        ARROWS = ">";
      if(keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD0)
        shootConstants[0] = true;

      if(keyEvent.getKeyCode() == KeyEvent.VK_W)
        WASD = "w";
      if(keyEvent.getKeyCode() == KeyEvent.VK_A)
        WASD = "a";
      if(keyEvent.getKeyCode() == KeyEvent.VK_S)
        WASD = "s";
      if(keyEvent.getKeyCode() == KeyEvent.VK_D)
        WASD = "d";
      if(keyEvent.getKeyCode() == KeyEvent.VK_F)
        shootConstants[1] = true;


      if(keyEvent.getKeyCode() == KeyEvent.VK_Y)
        YGHJ = "y";
      if(keyEvent.getKeyCode() == KeyEvent.VK_G)
        YGHJ = "g";
      if(keyEvent.getKeyCode() == KeyEvent.VK_H)
        YGHJ = "h";
      if(keyEvent.getKeyCode() == KeyEvent.VK_J)
        YGHJ = "j";
      if(keyEvent.getKeyCode() == KeyEvent.VK_K)
        shootConstants[2] = true;

      if(keyEvent.getKeyCode() == KeyEvent.VK_P)
        PLXX = "p";
      if(keyEvent.getKeyCode() == KeyEvent.VK_L)
        PLXX = "l";
      if(keyEvent.getKeyCode() == KeyEvent.VK_SEMICOLON)
        PLXX = ";";
      if(keyEvent.getKeyCode() == KeyEvent.VK_QUOTE)
        PLXX = "'";
      if(keyEvent.getKeyCode() == KeyEvent.VK_BACK_SLASH)
        shootConstants[3] = true;

    }//else
  }//keyPressed

  public void keyReleased(KeyEvent keyEvent) {
    //System.out.print("KEY RELEASED");
  }
}//class
