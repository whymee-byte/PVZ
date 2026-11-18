import pvz.logic.*;
import pvz.logic.gameobjects.*;
import pvz.control.Level;
import utils.Position;
public class CurrentChecks {
 public static void main(String[] args) {
  System.out.println("equals coordinates="+new Position(1,2).equals(new Position(1,2)));
  Game g=new Game(25L,Level.EASY); Position p=new Position(0,0);
  Sun s=new Sun(g,p); g.addGameObject(s);
  System.out.println("sun icon="+s.getIcon());
  System.out.println("first catch="+g.catchObject(p)+" coins="+g.getCoins()+" caught="+g.getCaughtSuns());
  g.update();
  System.out.println("second catch="+g.catchObject(p)+" coins="+g.getCoins()+" caught="+g.getCaughtSuns());
  g.reset(); System.out.println("caught after reset="+g.getCaughtSuns());
  Game h=new Game(25L,Level.EASY); Sun a=new Sun(h,new Position(1,1)), b=new Sun(h,new Position(1,1)); a.update();
  h.addGameObject(a);h.addGameObject(b);
  System.out.println("two ages rendered="+h.positionToString(new Position(1,1)));
  Sunflower plant=new Sunflower(h,new Position(2,2)); Sun dead=new Sun(h,new Position(2,2));
  h.addGameObject(dead);h.addGameObject(plant);dead.setAlive(false);
  System.out.println("dead first hides live plant="+h.isEmpty(new Position(2,2)));
 }
}
