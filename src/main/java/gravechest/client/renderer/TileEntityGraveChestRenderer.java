package gravechest.client.renderer;

import gravechest.tileentities.TileEntityGraveChest;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityGraveChestRenderer extends TileEntitySpecialRenderer {

	private final ResourceLocation texture;

    private ModelChest modelchest = new ModelChest();

	public TileEntityGraveChestRenderer(ResourceLocation texture) {
		this.texture = texture;
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float angle) {
		renderTileEntityAt((TileEntityGraveChest)tile, x, y, z, angle);
	}
		
	public void renderTileEntityAt(TileEntityGraveChest tile, double x, double y, double z, float angle) {
		TileEntityGraveChest graveChest = (TileEntityGraveChest) tile;

		bindTexture(texture);

		GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x, y + 1.0F, z + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslated(0.5F, 0.5F, 0.5F);

		short rotation = 0;
		switch (graveChest.getBlockMetadata()) {
			case 2:
				rotation = 180;
				break;
			case 4:
				rotation = 90;
				break;
			case 5:
				rotation = -90;
				break;
		}

		GL11.glRotatef((float)rotation, 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(-0.5F, -0.5F, -0.5F);
		float lidRotation = 1.0F - (graveChest.prevLidAngle + (graveChest.lidAngle - graveChest.prevLidAngle) * angle);
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		
		modelchest.chestLid.rotateAngleX = -(lidRotation * (float)Math.PI / 2.0F);
		modelchest.renderAll();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}