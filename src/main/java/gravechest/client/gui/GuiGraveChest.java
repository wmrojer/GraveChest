package gravechest.client.gui;

import gravechest.info.ModInfo;
import gravechest.inventory.ContainerGraveChest;
import gravechest.tileentities.TileEntityGraveChest;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiGraveChest extends GuiContainer {

	protected final int BLACK = 0;
	private static final ResourceLocation TEXTURE = new ResourceLocation(ModInfo.MOD_ID + ":textures/gui/container/graveChest.png");
//	private final TileEntityGraveChest graveChest;

	public GuiGraveChest(InventoryPlayer inventory, TileEntityGraveChest tileGraveChest) {
		super(new ContainerGraveChest(inventory, tileGraveChest));
		xSize = 238;
		ySize = 256;
//		graveChest = tileGraveChest;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		//String name = StatCollector.translateToLocal(graveChest.getInventoryName());
		//fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, BLACK);
		//fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 92, BLACK);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
