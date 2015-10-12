public class FPS
{
  public static void main(String[] args) throws Exception
  {
    int numberOfRealPlayers = 1;
    int pixels = 700;
    int squares = 15;
    int speedDelay = 100;

    if(args.length >= 1)
      numberOfRealPlayers = Integer.parseInt(args[0]);
    if(args.length >= 2)
      squares = Integer.parseInt(args[1]);
    if(args.length >= 3)
      pixels = Integer.parseInt(args[2]);
    if(args.length == 4)
      speedDelay = Integer.parseInt(args[3]);

    FPSGUI fpsGUI = new FPSGUI(squares, pixels, pixels);
    GameManager gameManager = new GameManager(fpsGUI, numberOfRealPlayers, speedDelay);

  }//main
}//class
