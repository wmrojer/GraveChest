package gravechest.blocks;

import gravechest.GraveChest;
import gravechest.info.ModInfo;
import gravechest.tileentities.TileEntityGraveChest;
import gravechest.util.BlockUtil;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGraveChest extends BlockContainer {

	public BlockGraveChest() {
		super(Material.rock);
		setHardness(1.5F);
		setStepSound(soundTypeStone);
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		setResistance(6000000.0F);
		setBlockName("gravechest.graveChest");
	}

    public static boolean spawnGraveChest(World world, int x, int y, int z, List<ItemStack> drops) {
        if (BlockUtil.safeSetBlock(world, x, y, z, GraveChest.blockGraveChest)) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity != null && tileEntity instanceof TileEntityGraveChest) {
                if (((TileEntityGraveChest) tileEntity).addAllItemStacks(drops)) {;
                	return true;
                } else {
                	GraveChest.log.error("Failed to add all items to the chest!");
                }
            } else {
            	GraveChest.log.error("Unexpected or no TileEntityGraveChest when spawning chest!");
            }
        }
        return false;
    }

    @Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon("planks_oak"); // Dummy icon. Not actually used since it is the tileEntity that is rendered.
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.blockIcon;
	}

	@Override
	public int getRenderType() {
		return GraveChest.graveChestRenderID;
	}

	@Override
	public Item getItemDropped(int id, Random rand, int fortune) {
		return null; // Don't drop the chest.
	}
	
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return null; // Prevent chest from being "picked" in creative mode.
    }

    @Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		byte b0 = 0;
		int l1 = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (l1 == 0)
			b0 = 2;
		if (l1 == 1)
			b0 = 5;
		if (l1 == 2)
			b0 = 3;
		if (l1 == 3)
			b0 = 4;

		world.setBlockMetadataWithNotify(x, y, z, b0, 3);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		if (player.isSneaking())
			return false;
		else {
			TileEntityGraveChest tile = getTileEntity(world, x, y, z, TileEntityGraveChest.class);
			if (tile != null)
				player.openGui(GraveChest.instance, ModInfo.GRAVE_CHEST_GUI, world, x, y, z);
			return true;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityGraveChest();
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getTileEntity(IBlockAccess world, int x, int y, int z, Class<T> cls) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (!cls.isInstance(tile))
			return null;
		return (T) tile;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
		return Container.calcRedstoneFromInventory(getTileEntity(world, x, y, z, IInventory.class));
	}

	public static void dropStack(World world, int x, int y, int z, ItemStack stack) {
		if (!world.isRemote && stack != null && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			float f = 0.7F;
			double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			EntityItem entityItem = new EntityItem(world, x + d0, y + d1, z + d2, stack);
			entityItem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityItem);
		}
	}

	public static void dropInventoryContents(TileEntity tile) {
		if (tile == null || !(tile instanceof IInventory))
			return;
		IInventory iinventory = (IInventory) tile;
		for (int i = 0; i < iinventory.getSizeInventory(); i++) {
			ItemStack stack = iinventory.getStackInSlot(i);
			if (stack != null && stack.getItem() != null && stack.stackSize > 0) {
				dropStack(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, stack.copy());
				iinventory.setInventorySlotContents(i, null);
			}
		}
		tile.markDirty();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		dropInventoryContents(world.getTileEntity(x, y, z));
		super.breakBlock(world, x, y, z, block, meta);
	}

}
