package pvz.logic;

import java.util.*;
import pvz.control.*;
import pvz.logic.gameobjects.*;
import pvz.logic.actions.*;
import pvz.view.*;
import utils.*;

public class DeepChecks {
 static int pass,fail;
 static Map<String,int[]> totals=new LinkedHashMap<>();
 interface Check {void run();}
 static void test(String group,String name,Check c){
  int[] n=totals.computeIfAbsent(group,k->new int[2]);
  try{c.run();pass++;n[0]++;}catch(Throwable e){fail++;n[1]++;System.out.println("FAIL | "+group+" | "+name+" | "+e);}
 }
 static void ok(boolean b,String why){if(!b)throw new AssertionError(why);}
 static Game game(){return new Game(25L,Level.EASY);}
 static Position p(int r,int c){return new Position(r,c);}
 static int hp(GameObject o){String s=o.getIcon();return Integer.parseInt(s.substring(s.lastIndexOf('[')+1,s.lastIndexOf(']')));}
 static class View implements GameView {
  int errors,shows,messages;
  public void showGame(){shows++;}public void showError(String s){errors++;}
  public void showMessage(String s){messages++;}public void showEndMessage(){}
  public String[] getPrompt(){return new String[]{"exit"};}
 }
 static void cmd(Game g,View v,String s){Command.parse(s.trim().split("\\s+")).execute(g,v);}
 static String snapshot(Game g){StringBuilder b=new StringBuilder();b.append(g.getCoins()).append('/').append(g.getCycles()).append('/').append(g.generatedSuns()).append('/').append(g.getCaughtSuns()).append('/').append(g.getRemainingZombies());for(int r=0;r<4;r++)for(int c=0;c<8;c++)b.append('|').append(g.positionToString(p(r,c)));return b.toString();}
 static void invariant(Game g){
  ok(g.getCoins()>=0,"negative coins");ok(g.getRemainingZombies()>=0,"negative pending");ok(g.getCaughtSuns()>=0,"negative catches");
  for(int r=0;r<4;r++)for(int c=0;c<8;c++){String s=g.positionToString(p(r,c));StringUtils.centre(s,12);ok(s.chars().filter(x->x=='*').count()<=1,"duplicate sun icon");ok(!s.contains("[00]")&&!s.contains("[-"),"dead icon: "+s);}
 }
 static class Probe extends GameObject {
  int updates,enters,exits;
  Probe(GameWorld g,Position p){super(g,p);health=1;}
  public void update(){updates++;}public void onEnter(){enters++;}public void onExit(){exits++;}
  public String getIcon(){return "?";}public String getDescription(){return "probe";}
 }
 public static void main(String[] args){
  String[] plants={"s","p","wn","c"};String[] zombies={"z","bz","sz","ez"};
  for(int r=0;r<4;r++)for(int c=0;c<8;c++){
   final int rr=r,cc=c;
   test("positions","coordinate "+r+","+c,()->{ok(p(rr,cc).equals(p(rr,cc)),"equal coords");ok(!p(rr,cc).equals(p(rr+1,cc)),"row differs");ok(!p(rr,cc).equals(p(rr,cc+1)),"col differs");ok(!p(rr,cc).equals((Position)null),"null");ok(p(rr,cc).left().column()==cc-1,"left");Set<String> set=new HashSet<>();for(Position n:p(rr,cc).adjacentPositions())set.add(n.toString());ok(set.size()==8&&!set.contains(p(rr,cc).toString()),"neighbors");});
   for(String name:plants)test("plants","buy "+name+" "+r+","+c,()->{Game g=game();Plant a=PlantFactory.spawnPlant(name,g,p(rr,cc));int cost=a.getCost();ok(g.addGameObject(name,p(rr,cc),false),"purchase fails");ok(g.getCoins()==50-cost,"wrong cost");ok(!g.addGameObject(name,p(rr,cc),true),"overlap accepted");ok(!g.positionToString(p(rr,cc)).isEmpty(),"missing icon");});
   for(String name:plants)test("sun coexistence","plant "+name+" "+r+","+c,()->{Game g=game();Sun s=new Sun(g,p(rr,cc));g.addGameObject(s);ok(g.addGameObject(name,p(rr,cc),true),"sun blocks plant");ok(!g.isEmpty(p(rr,cc)),"sun hides blocking plant");ok(g.catchObject(p(rr,cc)),"capture");ok(!g.isEmpty(p(rr,cc)),"dead sun hides plant");ok(g.getCoins()==60&&g.getCaughtSuns()==1,"ledger");invariant(g);});
   for(String name:zombies)test("zombies","create "+name+" "+r+","+c,()->{Game g=game();ok(g.addZombie(name,p(rr,cc)),"spawn fails");ok(!g.addZombie(name,p(rr,cc)),"overlap");ok(g.getRemainingZombies()==3,"manual spawn consumes quota");g.damageZombieAt(p(rr,cc),100);ok(g.isEmpty(p(rr,cc)),"dead blocks");});
   test("suns","stack "+r+","+c,()->{Game g=game();Sun a=new Sun(g,p(rr,cc)),b=new Sun(g,p(rr,cc));a.update();g.addGameObject(a);g.addGameObject(b);ok(g.positionToString(p(rr,cc)).chars().filter(x->x=='*').count()==1,"multiple icons");ok(g.catchObject(p(rr,cc))&&g.getCoins()==70&&g.getCaughtSuns()==2,"capture stack");ok(!a.isAlive()&&!b.isAlive(),"alive after capture");ok(!a.catchObject(p(rr,cc)),"double payout");g.update();ok(!g.catchObject(p(rr,cc)),"recapture");});
  }
  for(int r=0;r<4;r++)for(int left=0;left<8;left++)for(int right=left+1;right<8;right++){
   final int rr=r,ll=left,rc=right;
   test("shooting","row "+r+" from "+left+" to "+right,()->{Game g=game();Zombie z=ZombieFactory.spawnZombie("z",g,p(rr,rc));g.addGameObject(z);new Peashooter(g,p(rr,ll)).update();ok(hp(z)==4,"missed target");});
  }
  test("shooting","nearest and ignore left/other row",()->{Game g=game();Zombie a=ZombieFactory.spawnZombie("z",g,p(0,3)),b=ZombieFactory.spawnZombie("z",g,p(0,5)),l=ZombieFactory.spawnZombie("z",g,p(0,0)),r=ZombieFactory.spawnZombie("z",g,p(1,3));for(Zombie z:new Zombie[]{a,b,l,r})g.addGameObject(z);new Peashooter(g,p(0,1)).update();ok(hp(a)==4&&hp(b)==5&&hp(l)==5&&hp(r)==5,"wrong targets");});
  for(String name:zombies){
   test("movement","speed "+name,()->{Game g=game();Zombie z=ZombieFactory.spawnZombie(name,g,p(0,6));int speed=name.equals("bz")?4:name.equals("sz")?1:2;z.update();for(int i=1;i<speed;i++){z.update();ok(z.isInPosition(p(0,6)),"early move");}z.update();ok(z.isInPosition(p(0,5)),"late move");});
   test("movement","cross sun "+name,()->{Game g=game();g.addGameObject(new Sun(g,p(0,5)));Zombie z=ZombieFactory.spawnZombie(name,g,p(0,6));for(int i=0;i<5;i++)z.update();ok(z.getPosition().column()<=5,"sun blocks");});
   test("movement","blocked by plant "+name,()->{Game g=game();Plant w=PlantFactory.spawnPlant("wn",g,p(0,5));g.addGameObject(w);Zombie z=ZombieFactory.spawnZombie(name,g,p(0,6));z.update();ok(z.isInPosition(p(0,6))&&hp(w)==9,"attack/block");});
   test("movement","blocked by zombie "+name,()->{Game g=game();Zombie a=ZombieFactory.spawnZombie("z",g,p(0,5)),z=ZombieFactory.spawnZombie(name,g,p(0,6));g.addGameObject(a);for(int i=0;i<8;i++)z.update();ok(z.isInPosition(p(0,6))&&hp(a)==5,"zombie blocks/immune");});
   test("rule audit","no moving and attacking same turn "+name,()->{Game g=game();Plant w=PlantFactory.spawnPlant("wn",g,p(0,0));g.addGameObject(w);Zombie z=ZombieFactory.spawnZombie(name,g,p(0,2));while(z.isInPosition(p(0,2)))z.update();ok(hp(w)==10,"moved to col 1 AND attacked col 0; actual health="+hp(w));});
  }
  for(String name:plants)for(String zn:zombies)test("callbacks",name+" vs "+zn,()->{Game g=game();Plant a=PlantFactory.spawnPlant(name,g,p(0,0));Zombie z=ZombieFactory.spawnZombie(zn,g,p(0,2));int h=hp(a),zh=hp(z);ok(!a.receivePlantAttack(1)&&hp(a)==h,"friendly plant damage");ok(!z.receiveZombieAttack(1)&&hp(z)==zh,"friendly zombie damage");ok(a.receiveZombieAttack(1)&&hp(a)==h-1,"plant callback");ok(z.receivePlantAttack(1)&&hp(z)==zh-1,"zombie callback");});
  for(int r=0;r<4;r++)for(int c=0;c<8;c++){
   final int rr=r,cc=c;
   test("explosions","neighbors at "+r+","+c,()->{Game g=game();List<Zombie> zs=new ArrayList<>();for(Position q:p(rr,cc).adjacentPositions())if(g.isInsideBoard(q)){Zombie z=ZombieFactory.spawnZombie("bz",g,q);zs.add(z);g.addGameObject(z);}new ExplosionAction(3,p(rr,cc),true).execute(g);for(Zombie z:zs)ok(hp(z)==5,"neighbor not hit");});
   test("explosions","plants at "+r+","+c,()->{Game g=game();List<Plant> ps=new ArrayList<>();for(Position q:p(rr,cc).adjacentPositions())if(g.isInsideBoard(q)){Plant w=PlantFactory.spawnPlant("wn",g,q);ps.add(w);g.addGameObject(w);}new ExplosionAction(3,p(rr,cc),false).execute(g);for(Plant w:ps)ok(hp(w)==7,"neighbor not hit");});
  }
  test("explosions","cherry delayed once",()->{Game g=game();Plant b=PlantFactory.spawnPlant("c",g,p(0,0));g.addGameObject(b);g.addZombie("bz",p(1,1));g.update();ok(b.isAlive(),"early explosion");g.update();ok(b.isAlive(),"early explosion");g.update();ok(!b.isAlive()&&g.isEmpty(p(1,1)),"missing explosion");});
  test("explosions","explosive death kills adjacent plant",()->{Game g=game();Plant w=PlantFactory.spawnPlant("wn",g,p(0,0));g.addGameObject(w);g.addZombie("ez",p(0,1));g.damageZombieAt(p(0,1),5);g.update();ok(hp(w)==7,"death action missing/double");g.update();ok(hp(w)==7,"death action repeated");});
  test("lifecycle","callbacks once and skip dead",()->{GameObjectContainer c=new GameObjectContainer();Probe a=new Probe(game(),p(0,0));c.add(a);c.update();ok(a.enters==1&&a.updates==1,"callbacks");a.setAlive(false);c.update();c.removeDead();c.removeDead();ok(a.updates==1&&a.exits==1,"dead repeated");});
  test("actions","reset discards pending actions",()->{Game g=game();int[] calls={0};g.pushAction(w->calls[0]++);g.reset();g.update();ok(calls[0]==0,"action survived reset");});
  test("actions","action enqueued by action runs once",()->{Game g=game();int[] calls={0};g.pushAction(w->{calls[0]++;w.pushAction(x->calls[0]++);});g.update();g.update();ok(calls[0]==2,"nested action missing/repeated");});
  test("actions","explosive death during queued cherry damage",()->{Game g=game();Plant w=PlantFactory.spawnPlant("wn",g,p(0,0));g.addGameObject(w);g.addZombie("ez",p(0,1));g.pushAction(new ExplosionAction(10,p(1,1),true));g.update();ok(hp(w)==7&&g.isEmpty(p(0,1)),"death action not resolved same cycle");});
  test("reset","capture flag counters and quit reset",()->{Game g=game();g.addGameObject(new Sun(g,p(0,0)));g.catchObject(p(0,0));g.quit();g.reset();g.addGameObject(new Sun(g,p(0,0)));ok(g.catchObject(p(0,0))&&g.getCoins()==60&&g.getCaughtSuns()==1&&!g.playerQuits(),"reset flags");});
  test("suns","oldest rendered until expiry",()->{Game g=game();GameObjectContainer c=new GameObjectContainer();Sun a=new Sun(g,p(0,0)),b=new Sun(g,p(0,0));for(int i=0;i<9;i++)a.update();c.add(a);c.add(b);ok(c.positionToString(p(0,0)).contains("[02]"),"oldest hidden");c.update();c.update();c.removeDead();ok(c.positionToString(p(0,0)).contains("[09]"),"next sun hidden");});
  for(String name:plants)test("callbacks","lethal damage "+name,()->{Plant a=PlantFactory.spawnPlant(name,game(),p(0,0));a.receiveZombieAttack(100);ok(!a.isAlive(),"lethal hit alive");});
  for(String name:zombies)test("callbacks","lethal damage "+name,()->{Zombie a=ZombieFactory.spawnZombie(name,game(),p(0,0));a.receivePlantAttack(100);ok(!a.isAlive(),"lethal hit alive");});
  test("suns","lifetime",()->{Sun s=new Sun(game(),p(0,0));for(int i=0;i<10;i++)s.update();ok(s.isAlive(),"early expiry");s.update();ok(!s.isAlive()&&!s.catchObject(p(0,0)),"expired capture");});
  test("suns","one successful capture per cycle",()->{Game g=game();g.addGameObject(new Sun(g,p(0,0)));g.addGameObject(new Sun(g,p(0,1)));ok(!g.catchObject(p(3,7)),"empty capture");ok(g.catchObject(p(0,0)),"first");ok(!g.catchObject(p(0,1)),"second allowed");g.update();ok(g.catchObject(p(0,1)),"not reenabled");});
  test("suns","periodic 6 11 16 21 26",()->{Game g=game();SunManager s=new SunManager(new Random(12),g);for(int i=1;i<=30;i++){s.update();int n=i<6?0:1+(i-6)/5;ok(s.generatedSuns()==n,"cycle="+i+" count="+s.generatedSuns());}});
  test("suns","sunflower 4 7 10",()->{Game g=game();Sunflower s=new Sunflower(g,p(0,0));for(int i=1;i<=30;i++){s.update();int n=i<4?0:1+(i-4)/3;ok(g.generatedSuns()==n,"cycle "+i);}});
  String[] invalid={"unknown","add","a p","a p 1","a p 1 2 extra","a p x 2","a p 1 2147483648","a unknown 0 0","a p -1 0","a p 8 0","a p 0 4","cp","cp p x 0","cp p 0 0 extra","az","az unknown 0 0","az z -1 0","az z 8 0","az z x 0","catch","c x 0","c 0","c 0 0 extra","reset easy","reset bad 25","reset easy x","reset easy 9223372036854775808","exit extra","none extra","help extra","list extra","lz extra"};
  for(String s:invalid)test("invalid commands",s,()->{Game g=game();View v=new View();String before=snapshot(g);cmd(g,v,s);ok(v.errors==1&&v.shows==0&&snapshot(g).equals(before),"invalid changed state or missing error");});
  for(String s:new String[]{"h","HELP","l","LIST","lz","listZombies"})test("commands",s,()->{Game g=game();View v=new View();cmd(g,v,s);ok(g.getCycles()==0&&v.messages==1&&v.errors==0,"query side effect");});
  for(String s:new String[]{"e","EXIT","exit"})test("commands",s,()->{Game g=game();cmd(g,new View(),s);ok(g.playerQuits()&&g.getCycles()==0&&g.hasGameFinished(),"exit");});
  for(String s:new String[]{"","n","NONE"})test("commands","advance "+s,()->{Game g=game();View v=new View();cmd(g,v,s);ok(g.getCycles()==1&&v.shows==1,"advance");});
  for(Level lv:Level.values())for(int seed=0;seed<20;seed++){
   final int sd=seed;
   test("random sessions",lv+" seed "+seed,()->{Game g=new Game((long)sd,lv);Random random=new Random(1000+sd);View v=new View();for(int i=0;i<250&&!g.hasGameFinished();i++){int r=random.nextInt(4),c=random.nextInt(8),action=random.nextInt(8);switch(action){case 0:cmd(g,v,"cp "+plants[random.nextInt(4)]+" "+c+" "+r);break;case 1:cmd(g,v,"az "+zombies[random.nextInt(4)]+" "+c+" "+r);break;case 2:cmd(g,v,"a "+plants[random.nextInt(4)]+" "+c+" "+r);break;case 3:cmd(g,v,"c "+c+" "+r);break;case 4:cmd(g,v,"help");break;default:cmd(g,v,"none");}invariant(g);} });
   test("reset replay",lv+" seed "+seed,()->{Game g=new Game((long)sd,lv);for(int i=0;i<12;i++)g.update();String state=snapshot(g);g.reset();ok(g.getCoins()==50&&g.getCycles()==0&&g.generatedSuns()==0&&g.getCaughtSuns()==0&&!g.playerQuits(),"reset initial");for(int i=0;i<12;i++)g.update();ok(snapshot(g).equals(state),"seed replay");});
  }
  for(int r=0;r<4;r++){final int rr=r;test("endgame","loss row "+r,()->{Game g=game();g.addZombie("sz",p(rr,0));g.update();g.update();ok(g.zombiesWin()&&g.hasGameFinished(),"no loss");});}
  test("endgame","all zombies exhausted and killed",()->{Game g=game();for(int i=0;i<1000&&!g.playerWins();i++){for(int r=0;r<4;r++)for(int c=0;c<=8;c++)g.damageZombieAt(p(r,c),100);g.update();}ok(g.playerWins(),"no victory or wrong alive count");});
  totals.forEach((k,n)->System.out.println("GROUP "+k+" pass="+n[0]+" fail="+n[1]));
  System.out.println("TOTAL pass="+pass+" fail="+fail);
 }
}
