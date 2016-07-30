package gravechest.client.renderer;

import gravechest.GraveChest;
import gravechest.info.ModInfo;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockGraveChestRenderer implements ISimpleBlockRenderingHandler {

	private final ModelChest chest = new ModelChest();
	private final ResourceLocation graveChestTexture = new ResourceLocation(ModInfo.MOD_ID+":textures/entities/graveChest.png");

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glRotatef(90, 0, 1, 0);

		//renderer.setRenderBounds(1F / 16F, 0, 1F / 16F, 15F / 16F, 10F / 16F, 15F / 16F);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(graveChestTexture);
		GL11.glScalef(1, -1, -1);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		chest.renderAll();

		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		// The tile entity handles the rendering
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return GraveChest.graveChestRenderID;
	}
}
