package pvz.logic;
import pvz.logic.gameobjects.*;
import pvz.logic.gameobjects.zombieTypes.*;
import pvz.logic.actions.*;
import pvz.control.*;
import pvz.view.*;
import utils.*;

public class ReviewChecks {
    static int passed, failed;
    interface Check { void run(); }
    static void test(String name, Check check) {
        try { check.run(); passed++; System.out.println("PASS " + name); }
        catch (Throwable e) { failed++; System.out.println("FAIL " + name + " | " + e); }
    }
    static void require(boolean b) { if (!b) throw new AssertionError(); }
    static Game game() { return new Game(25L, Level.EASY); }
    static Position p(int row, int col) { return new Position(row,col); }
    static class RecordingGame extends Game {
        int generated;
        GameObject last;
        RecordingGame() { super(25L,Level.EASY); }
        public boolean addGameObject(GameObject o) { generated++; last=o; return super.addGameObject(o); }
    }
    static class View implements GameView {
        int shows, errors, messages;
        public void showGame() { shows++; }
        public void showError(String s) { errors++; }
        public void showMessage(String s) { messages++; }
        public void showEndMessage() {}
        public String[] getPrompt() { return new String[]{"exit"}; }
    }
    static class Probe extends GameObject {
        int updates, exits, enters;
        Probe(GameWorld g,Position p) { super(g,p); health=1; }
        public String getIcon(){return "?";}
        public String getDescription(){return "probe";}
        public void onEnter(){enters++;}
        public void onExit(){exits++;}
        public void update(){updates++;}
    }
    static void command(Game g, View v, String s) { Command.parse(s.split(" ")).execute(g,v); }
    public static void main(String[] args) {
        test("initial state", () -> { Game g=game(); require(g.getCoins()==50 && g.getCycles()==0 && g.getRemainingZombies()==3 && !g.hasGameFinished()); });
        test("board limits", () -> { Game g=game(); require(g.isInsideBoard(p(0,0)) && g.isInsideBoard(p(3,7)) && !g.isInsideBoard(p(-1,0)) && !g.isInsideBoard(p(4,0)) && !g.isInsideBoard(p(0,8))); });
        test("purchase and insufficient coins", () -> { Game g=game(); require(g.addGameObject("p",p(0,0),false)); require(g.getCoins()==0); require(!g.addGameObject("s",p(1,0),false)); });
        test("invalid purchase leaves coins", () -> { Game g=game(); require(!g.addGameObject("unknown",p(0,0),false) && !g.addGameObject("s",p(9,0),false) && g.getCoins()==50); });
        test("cheat purchase", () -> { Game g=game(); require(g.addGameObject("p",p(0,0),true) && g.getCoins()==50); });
        test("occupied position", () -> { Game g=game(); g.addGameObject("p",p(0,0),true); require(!g.addGameObject("s",p(0,0),true)); });
        test("reset state", () -> { Game g=game(); g.addGameObject("p",p(0,0),false); g.update(); g.quit(); g.reset(); require(g.getCoins()==50 && g.getCycles()==0 && g.isEmpty(p(0,0)) && !g.playerQuits()); });
        test("reset difficulty", () -> { Game g=game(); g.reset(Level.INSANE,17); require(g.getRemainingZombies()==10); });
        for(String name : new String[]{"p","s","wn","c","Peashooter","Sunflower","Wall-Nut","Cherry-Bomb"}) {
            test("plant factory "+name, () -> { Plant a=PlantFactory.spawnPlant(name,game(),p(0,0)); Plant b=PlantFactory.spawnPlant(name,game(),p(1,1)); require(a!=null && b!=null && a!=b && a.isInPosition(p(0,0)) && !a.getDescription().isEmpty()); });
        }
        for(String name : new String[]{"Z","Bz","Sz","Ez"}) {
            test("zombie factory "+name, () -> { Zombie a=ZombieFactory.spawnZombie(name,game(),p(0,4)); require(a!=null && a.isAlive() && !a.getDescription().isEmpty()); });
        }
        test("callbacks plant ignores plant attack", () -> { GameItem a=new Sunflower(game(),p(0,0)); require(!a.receivePlantAttack(50) && a.receiveZombieAttack(1)); });
        test("callbacks zombie ignores zombie attack", () -> { Zombie a=new NormalZombie(game(),p(0,4)); GameItem i=a; require(!i.receiveZombieAttack(50)); require(i.receivePlantAttack(5) && !a.isAlive()); });
        test("sun ignores attacks", () -> { Sun s=new Sun(game(),p(0,0)); require(!s.receivePlantAttack(50) && !s.receiveZombieAttack(50) && s.isAlive()); });
        test("catch numeric coordinates", () -> { Game g=game(); g.addGameObject(new Sun(g,p(1,2))); View v=new View(); command(g,v,"catch 2 1"); require(g.getCoins()==60 && g.getCycles()==0 && v.shows==1); });
        test("catch invalid numeric parameter reports error", () -> { View v=new View(); command(game(),v,"catch x 1"); require(v.errors==1); });
        test("catch multiple suns counts objects", () -> { Game g=game(); GameObjectContainer c=new GameObjectContainer(); c.add(new Sun(g,p(0,0))); c.add(new Sun(g,p(0,0))); require(c.catchObject(p(0,0))==2 && g.getCoins()==70); });
        test("caught sun dies immediately", () -> { Game g=game(); Sun s=new Sun(g,p(0,0)); s.catchObject(p(0,0)); require(!s.isAlive()); });
        test("caught sun cannot pay twice", () -> { Game g=game(); Sun s=new Sun(g,p(0,0)); s.catchObject(p(0,0)); require(!s.catchObject(p(0,0)) && g.getCoins()==60); });
        test("caught sun removed by container", () -> { Game g=game(); GameObjectContainer c=new GameObjectContainer(); c.add(new Sun(g,p(0,0))); c.catchObject(p(0,0)); c.update(); c.removeDead(); require(c.isEmpty(p(0,0))); });
        test("one successful catch per cycle", () -> { Game g=game(); g.addGameObject(new Sun(g,p(0,0))); g.addGameObject(new Sun(g,p(1,0))); require(g.catchObject(p(0,0)) && !g.catchObject(p(1,0))); g.update(); require(g.catchObject(p(1,0))); });
        test("sun direct lifetime 11 updates", () -> { Sun s=new Sun(game(),p(0,0)); for(int i=0;i<10;i++)s.update(); require(s.isAlive()); s.update(); require(!s.isAlive()); });
        test("sun and plant render one sun", () -> { Game g=game(); g.addGameObject(new Sun(g,p(0,0))); g.addGameObject(new Sun(g,p(0,0))); g.addGameObject(new Peashooter(g,p(0,0))); String t=g.positionToString(p(0,0)); require(t.contains("P[03]") && t.chars().filter(x->x=='*').count()==1); });
        test("dead objects hidden", () -> { Game g=game(); Sunflower s=new Sunflower(g,p(0,0)); g.addGameObject(s); s.receiveZombieAttack(1); require(g.positionToString(p(0,0)).isEmpty()); });
        test("plant can share position with sun", () -> { Game g=game(); g.addGameObject(new Sun(g,p(0,0))); require(g.addGameObject("s",p(0,0),false)); });
        test("sun does not block zombie movement", () -> { Game g=game(); g.addGameObject(new Sun(g,p(0,1))); Zombie z=new NormalZombie(g,p(0,2)); z.update(); z.update(); require(z.isInPosition(p(0,1))); });
        test("zombie attacks adjacent plant", () -> { Game g=game(); Sunflower s=new Sunflower(g,p(0,0)); g.addGameObject(s); Zombie z=new NormalZombie(g,p(0,1)); z.update(); require(!s.isAlive() && z.isInPosition(p(0,1))); });
        test("peashooter nearest target only", () -> { Game g=game(); Zombie a=new NormalZombie(g,p(0,2)), b=new NormalZombie(g,p(0,3)); g.addGameObject(a); g.addGameObject(b); new Peashooter(g,p(0,0)).update(); require(a.getIcon().contains("04") && b.getIcon().contains("05")); });
        test("peashooter ignores other rows and left", () -> { Game g=game(); Zombie a=new NormalZombie(g,p(1,2)), b=new NormalZombie(g,p(0,0)); g.addGameObject(a); g.addGameObject(b); new Peashooter(g,p(0,1)).update(); require(a.getIcon().contains("05") && b.getIcon().contains("05")); });
        test("all zombie speeds", () -> { for(String n:new String[]{"Z","Bz","Sz","Ez"}) { int speed=n.equals("Bz")?4:n.equals("Sz")?1:2; Zombie z=ZombieFactory.spawnZombie(n,game(),p(0,4)); for(int i=1;i<speed;i++){z.update(); require(z.isInPosition(p(0,4)));} z.update(); require(z.isInPosition(p(0,3))); } });
        test("explosion damages adjacent plants", () -> { Game g=game(); Sunflower s=new Sunflower(g,p(0,0)); g.addGameObject(s); new ExplosionAction(3,p(0,1),false).execute(g); require(!s.isAlive()); });
        test("explosion damages adjacent zombies", () -> { Game g=game(); Zombie z=new NormalZombie(g,p(0,0)); g.addGameObject(z); new ExplosionAction(10,p(0,1),true).execute(g); require(!z.isAlive()); });
        test("help list unknown do not advance", () -> { Game g=game(); View v=new View(); for(String s:new String[]{"help","list","listZombies","unknown","add","add p x 1","none extra"})command(g,v,s); require(g.getCycles()==0 && v.shows==0 && v.errors==4 && v.messages==3); });
        test("empty command advances", () -> { Game g=game(); command(g,new View(),""); require(g.getCycles()==1); });
        test("case insensitive and shortcuts", () -> { Game g=game(); View v=new View(); command(g,v,"A P 0 0"); require(g.getCycles()==1 && g.getCoins()==0); command(g,v,"R"); require(g.getCycles()==0 && g.getCoins()==50); });
        test("stub add overload really inserts", () -> { Game g=game(); require(g.addGameObject("s",p(0,0)) && !g.isEmpty(p(0,0))); });
        test("Position toString useful", () -> { require(!p(1,2).toString().isEmpty()); });
        test("Position equality through Object", () -> { Object a=p(1,2),b=p(1,2); require(a.equals(b)); });
        test("periodic manager generation schedule", () -> { RecordingGame g=new RecordingGame(); SunManager m=new SunManager(new java.util.Random(25),g); for(int i=0;i<4;i++)m.update(); require(g.generated==0); m.update(); require(g.generated==1); for(int i=0;i<4;i++)m.update(); require(g.generated==2); });
        test("sunflower generation", () -> { RecordingGame g=new RecordingGame(); Sunflower s=new Sunflower(g,p(0,0)); s.update(); s.update(); require(g.generated==0); s.update(); require(g.generated==1); });
        test("deterministic reset replay", () -> { Game g=game(); for(int i=0;i<8;i++)g.update(); String before=snapshot(g); g.reset(); for(int i=0;i<8;i++)g.update(); require(before.equals(snapshot(g))); });
        test("container extensible without concrete types", () -> { GameObjectContainer c=new GameObjectContainer(); Probe a=new Probe(game(),p(0,0)); c.add(a); c.update(); require(a.enters==1 && a.updates==1 && c.positionToString(p(0,0)).equals("?")); a.setAlive(false); c.update(); require(a.updates==1); require(c.removeDead() && a.exits==1 && !c.removeDead()); });
        test("dead object does not occupy position", () -> { GameObjectContainer c=new GameObjectContainer(); Probe a=new Probe(game(),p(0,0)); c.add(a); a.setAlive(false); require(c.isEmpty(p(0,0))); });
        test("explosive zombie death action", () -> { Game g=game(); Sunflower s=new Sunflower(g,p(0,0)); g.addGameObject(s); require(g.addZombie("Ez",p(0,1))); g.damageZombieAt(p(0,1),5); g.update(); require(!s.isAlive() && g.positionToString(p(0,1)).isEmpty()); });
        test("cherry explosion executes after countdown", () -> { Game g=game(); Plant b=PlantFactory.spawnPlant("c",g,p(0,0)); g.addGameObject(b); g.addZombie("Bz",p(1,1)); g.update(); g.update(); g.update(); require(!b.isAlive() && g.positionToString(p(1,1)).isEmpty()); });
        test("zombie reaching house loses game", () -> { Game g=game(); g.addZombie("Sz",p(0,0)); g.update(); g.update(); require(g.zombiesWin() && g.hasGameFinished()); });
        test("win after exhausting and killing zombies", () -> { Game g=game(); for(int i=0;i<1000 && !g.playerWins();i++){ for(int r=0;r<4;r++)for(int c=0;c<=8;c++)g.damageZombieAt(p(r,c),100); g.update(); } require(g.playerWins()); });
        test("render three simultaneous icons fits cell", () -> { Game g=game(); g.addGameObject(new Peashooter(g,p(0,0))); g.addGameObject(new NormalZombie(g,p(0,0))); g.addGameObject(new Sun(g,p(0,0))); StringUtils.centre(g.positionToString(p(0,0)),12); });
        test("dead sun cannot be caught", () -> { Game g=game(); Sun s=new Sun(g,p(0,0)); for(int i=0;i<11;i++)s.update(); require(!s.catchObject(p(0,0)) && g.getCoins()==50); });
        test("periodic sun updated in creation cycle per PDF", () -> { RecordingGame g=new RecordingGame(); for(int i=0;i<5;i++)g.update(); require(g.last.health==10); });
        System.out.println("TOTAL passed="+passed+" failed="+failed);
    }
    static String snapshot(Game g) { String s=g.getRemainingZombies()+":"+g.getCoins(); for(int r=0;r<4;r++)for(int c=0;c<8;c++)s+="|"+g.positionToString(p(r,c)); return s; }
}
