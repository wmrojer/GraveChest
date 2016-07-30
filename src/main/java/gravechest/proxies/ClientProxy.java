package gravechest.proxies;

import gravechest.GraveChest;
import gravechest.client.renderer.BlockGraveChestRenderer;
import gravechest.client.renderer.TileEntityGraveChestRenderer;
import gravechest.info.ModInfo;
import gravechest.tileentities.TileEntityGraveChest;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy {


	@Override
	public void registerTileEntities() {
		super.registerTileEntities();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGraveChest.class, new TileEntityGraveChestRenderer(new ResourceLocation(ModInfo.MOD_ID + ":textures/entities/graveChest.png")));
	}
	
	@Override
	public void registerRenderers() {
		GraveChest.graveChestRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockGraveChestRenderer());
	}
	
	
}
