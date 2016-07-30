package gravechest.proxies;

import gravechest.client.gui.GuiGraveChest;
import gravechest.event.PlayerDeathHandler;
import gravechest.info.ModInfo;
import gravechest.inventory.ContainerGraveChest;
import gravechest.tileentities.TileEntityGraveChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler  {

	public void registerEvents() {
		 MinecraftForge.EVENT_BUS.register(new PlayerDeathHandler());
	}
	
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityGraveChest.class, "graveChest");
	}
	
	public void registerRenderers() {
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		switch (ID) {
			case ModInfo.GRAVE_CHEST_GUI:
				return new ContainerGraveChest(player.inventory, (TileEntityGraveChest) tile);
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		switch (ID) {
			case ModInfo.GRAVE_CHEST_GUI:
				return new GuiGraveChest(player.inventory, (TileEntityGraveChest) tile);
			default:
				return null;
		}
	}

}
