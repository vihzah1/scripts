package jRunecrafting;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Bank.Amount;

@Manifest(description = "Trains runecrafting", name = "jRunecrafting 0.01")
public class jRunecrafting extends PollingScript<ClientContext>{
	

	public String selectAltar = "air";
	
	//Banks
	
	public int airBankers = 24101;
	
	//Mysterious ruins(outside)
	
	public int mAirA = 14399;
	
    //Altars
	
	public int airAltar = 14897;
	
	//Portal (exit)
	
	public int pAirA = 14841;
	
	//Runes
	public int runeEssID = 1436;
	public int airRuneID = 556;
	
	//Paths
	
	//Air

	public static final Tile[] toAirAltar = 
		{
		  new Tile(3010, 3355, 0), new Tile(3012, 3358, 0),
		  new Tile(3009, 3359, 0), new Tile(3008, 3356, 0),
		  new Tile(3008, 3353, 0), new Tile(3008, 3350, 0), 
		  new Tile(3008, 3347, 0), new Tile(3008, 3344, 0),
		  new Tile(3008, 3341, 0), new Tile(3008, 3338, 0),
		  new Tile(3008, 3335, 0), new Tile(3008, 3332, 0),
		  new Tile(3007, 3329, 0), new Tile(3007, 3326, 0),
		  new Tile(3007, 3323, 0), new Tile(3004, 3321, 0),
		  new Tile(3001, 3318, 0), new Tile(2998, 3317, 0),
		  new Tile(2995, 3314, 0), new Tile(2995, 3311, 0),
		  new Tile(2992, 3309, 0), new Tile(2991, 3306, 0),
		  new Tile(2989, 3303, 0), new Tile(2988, 3300, 0), 
		  new Tile(2987, 3297, 0), new Tile(2985, 3294, 0)
		  };

	
	public static final Tile[] toAirBank = 
		{
				new Tile(2983, 3288, 0), new Tile(2986, 3288, 0),
				new Tile(2989, 3290, 0), new Tile(2989, 3293, 0),
				new Tile(2992, 3295, 0), new Tile(2992, 3298, 0),
				new Tile(2992, 3301, 0), new Tile(2994, 3304, 0),
				new Tile(2997, 3307, 0), new Tile(2999, 3311, 0),
				new Tile(2999, 3314, 0), new Tile(3000, 3317, 0),
				new Tile(3003, 3319, 0), new Tile(3006, 3322, 0),
				new Tile(3006, 3325, 0), new Tile(3006, 3328, 0),
				new Tile(3006, 3331, 0), new Tile(3006, 3334, 0),
				new Tile(3006, 3337, 0), new Tile(3006, 3340, 0),
				new Tile(3006, 3343, 0), new Tile(3006, 3346, 0),
				new Tile(3006, 3349, 0), new Tile(3006, 3352, 0),
				new Tile(3006, 3356, 0), new Tile(3008, 3359, 0),
				new Tile(3011, 3359, 0), new Tile(3011, 3356, 0)
				};

	
	public void onStart(){
		
		ctx.inventory.select().poll();
		
	}
	
	@Override
	public void poll() {
		switch(state()){
		
		case airAltar:
		
            ctx.inventory.select().poll();
			ctx.movement.newTilePath(toAirAltar).traverse();
			Condition.sleep(271);
			
			org.powerbot.script.rt4.GameObject mRair = ctx.objects.select().id(mAirA).poll();
			
			if(mRair.inViewport() &&
					!ctx.players.local().inMotion()){
				
				mRair.interact("Enter");
				Condition.sleep(1271);
				
			}else{
				
				ctx.movement.step(mRair);
				
			}
			
		  org.powerbot.script.rt4.GameObject airAltarr = ctx.objects.select().id(airAltar).poll();
		  
		  if(airAltarr.inViewport()){
			  
			  airAltarr.interact("Craft-Rune");
			  Condition.sleep(1271);
			  
		  }else{
			  
			  ctx.movement.step(airAltarr);
			  
		  }

		case airBank:
			
			 org.powerbot.script.rt4.GameObject pAirAltar = ctx.objects.select().id(pAirA).poll();
			  
			  if(pAirAltar.inViewport()){
				  
				  pAirAltar.interact("Use");
				  Condition.sleep(1271);
				  
			  }else{
				  
				  ctx.movement.step(pAirAltar);
				  
			  }
			  
			  ctx.movement.newTilePath(toAirBank).traverse();
		
		org.powerbot.script.rt4.GameObject airBankerss = ctx.objects.select().id(airBankers).poll();
		
		if(ctx.inventory.select().id(runeEssID).count() == 0 && airBankerss.inViewport()){
			
			airBankerss.interact("Bank");
			Condition.sleep(1271);
			
		}
		
		if(ctx.bank.open()){
			
			ctx.bank.depositInventory();
			Condition.sleep(271);
			break;
			
		}
		case airWithdraw:
			
			if(ctx.bank.open()){
				
				ctx.bank.withdraw(runeEssID, Amount.ALL);
				
				break;
						
			}else{
				
				org.powerbot.script.rt4.GameObject airBankerz = ctx.objects.select().id(airBankers).poll();
				
				if(airBankerz.inViewport()){
					
					airBankerz.interact("Bank");
					Condition.sleep(1271);
		
				}
				
			}
			
		}
	}
	
	private State state(){
		
		if(ctx.inventory.select().id(runeEssID).count() > 1){
			
			return State.airAltar;
			
		}
		
		if(ctx.inventory.select().id(airRuneID).count() == 1){
			
			return State.airBank;
			
		}
		
		if(ctx.inventory.select().count() == 0){
			
			return State.airWithdraw;
			
		}
		return null;
		
	}
	
	private enum State{
		airAltar, airBank, airWithdraw
	}
	
}
