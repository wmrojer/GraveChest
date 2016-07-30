package gravechest.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import gravechest.GraveChest;
import gravechest.blocks.BlockGraveChest;
import gravechest.util.ItemUtil;

import java.util.List;

import static net.minecraft.util.MathHelper.floor_double;

public class PlayerDeathHandler {

	private int[] ox = {0, -1, 0, 1, -1, 1, -1, 0, 1}; // 9 block square starting with center location
	private int[] oz = {0, -1, -1, -1, 0, 0, 1, 1, 1};
	
    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        Entity entity = event.entity;
        if (entity instanceof EntityPlayer) {
            World world = entity.worldObj;
            int x = floor_double(entity.posX);
            int y = floor_double(entity.posY);
            y = y < 1 ? 1 : y;
            int z = floor_double(entity.posZ);
            List<ItemStack> itemStacks = ItemUtil.getItemStacks(event.drops);
            int oy = 0;
            
            while (((y - oy) > 1) && ((y + oy) < (world.getActualHeight() - 1))) {
	            for (int i = 0; i < ox.length; i++ ) {
	            	if ((y + oy) < (world.getActualHeight() - 1)) {
		            	if (spawnGrave(world, x+ox[i], y+oy, z+oz[i], itemStacks)) {
		            		event.setCanceled(true);
		            		return;
		            	}
	            	}
	            	if (oy > 0 && (y - oy > 0)) {
		            	if (spawnGrave(world, x+ox[i], y-oy, z+oz[i], itemStacks)) {
		            		event.setCanceled(true);
		            		return;
		            	}
	            	}
	            }
	            oy++;
            }
        	GraveChest.log.error("Unable to find a safe location to spawn the grave chest in the world. Death pos("+x+","+y+","+z+"). Dropping items as normal!");
        }
    }
    
    private boolean spawnGrave(World world, int x, int y, int z, List<ItemStack> itemStacks) {
    	GraveChest.log.info("Checking location ("+x+","+y+","+z+").");
    	if (world.isAirBlock(x, y, z) && (!world.isAirBlock(x, y-1, z) || y == 1)) {
        	GraveChest.log.info("Location ("+x+","+y+","+z+") looks good trying to spawn grave chest.");
            if (BlockGraveChest.spawnGraveChest(world, x, y, z, itemStacks)) {
            	GraveChest.log.info("Placed grave chest at ("+x+","+y+","+z+").");
            	return true;
            }
    	}
    	return false;
    }
}
