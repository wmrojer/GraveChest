package gravechest.tileentities;

import gravechest.inventory.ContainerGraveChest;

import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGraveChest extends TileEntity implements IInventory {

	protected ItemStack[] inventory;
	private final String invName;
	public float lidAngle;
	public float prevLidAngle;
	public int usingPlayers;

	public TileEntityGraveChest() {
		this(ContainerGraveChest.numSlots);
	}

	public TileEntityGraveChest(int invSize) {
		this(invSize, "graveChest");
	}

	public TileEntityGraveChest(int invSize, String invName) {
		this.invName = invName;
		inventory = new ItemStack[invSize];
	}

	// Used on player death to add all dropped items into the chest.
    public boolean addAllItemStacks(List<ItemStack> drops) {
    	try {
	    	if (drops.size() > getSizeInventory()) { // Extend array to hold all items
	    		inventory = new ItemStack[drops.size()];
	    	}
	    	for (int i = 0; i < drops.size(); i++ ) {
	    		inventory[i] = drops.get(i).copy();
	    	}
    	} catch (Exception e) {
    		return false;
    	}
    	return true;
    }

    // Returns true if there is no items stored in the tile.
	public boolean isEmpty() {
		for (int i = 0; i < getSizeInventory(); i++ ) {
			if (inventory[i] != null) {
				return false;
			}
		}
		return true;
	}

	public void sortInventory() {
		int i = 0;
		while (i < inventory.length) {
			if (inventory[i] == null) {
				int j = i + 1;
				while (j <  inventory.length) {
					if (inventory[j] != null) {
						inventory[i] = inventory[j];
						inventory[j] = null;
						break;
					}
					j++;
				}
			}
			i++;
		}
	}
	
	@Override
	public void updateEntity() {
		prevLidAngle = lidAngle;

		if (usingPlayers > 0 && lidAngle == 0.0F)
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);

		if (usingPlayers <= 0 && lidAngle > 0.0F || usingPlayers > 0 && lidAngle < 1.0F) {
			float oldLidAngle = lidAngle;

			if (usingPlayers > 0)
				lidAngle += 0.1F;
			else
				lidAngle -= 0.1F;

			if (lidAngle > 1.0F)
				lidAngle = 1.0F;
			else if (lidAngle < 0.0F)
				lidAngle = 0.0F;

			if (lidAngle < 0.5F && oldLidAngle >= 0.5F)
				worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	@Override
	public boolean receiveClientEvent(int eventID, int value) {
		if (eventID == 1) {
			usingPlayers = value;
			if (usingPlayers == 0) { // Remove the chest after all players have closed it and it is empty
				Side side = FMLCommonHandler.instance().getEffectiveSide();
				if ( side == Side.SERVER) {
					if (isEmpty()) {
						this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
					} else {
						sortInventory();
					}
				}
			}
			return true;
		} else {
			return super.receiveClientEvent(eventID, value);
		}
	}

	@Override
	public void openInventory() {
		if (usingPlayers < 0)
			usingPlayers = 0;

		usingPlayers++;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, usingPlayers);
	}

	@Override
	public void closeInventory() {
		usingPlayers--;
		if (usingPlayers < 0)
			usingPlayers = 0;

		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, usingPlayers);
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		try {
			return inventory[slot];
		} finally {
			markDirty();
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		try {
			if (inventory[slot] != null) {
				ItemStack itemstack;
				if (inventory[slot].stackSize <= size) {
					itemstack = inventory[slot];
					inventory[slot] = null;
					return itemstack;
				} else {
					itemstack = inventory[slot].splitStack(size);
					if (inventory[slot].stackSize == 0)
						inventory[slot] = null;
					return itemstack;
				}
			} else
				return null;
		} finally {
			markDirty();
		}
	}

	/**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		try {
			if (inventory[slot] != null) {
				ItemStack itemstack = inventory[slot];
				inventory[slot] = null;
				return itemstack;
			} else
				return null;
		} finally {
			markDirty();
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
			stack.stackSize = getInventoryStackLimit();

		markDirty();
	}

	@Override
	public String getInventoryName() {
		return "container.graveChest." + invName;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false; // Prevent insertion
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList tags = nbt.getTagList("Items", 10);
		inventory = new ItemStack[tags.tagCount() > getSizeInventory() ? tags.tagCount() : getSizeInventory()]; // Make sure all items fit in the array, but at least size of the slots in the container

		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound data = tags.getCompoundTagAt(i);
			//int j = data.getByte("Slot") & 255;
			//if (j >= 0 && j < inventory.length)
			//	inventory[j] = ItemStack.loadItemStackFromNBT(data);
			inventory[i] = ItemStack.loadItemStackFromNBT(data);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList tags = new NBTTagList();

		for (int i = 0; i < inventory.length; i++)
			if (inventory[i] != null) {
				NBTTagCompound data = new NBTTagCompound();
				//data.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(data);
				tags.appendTag(data);
			}

		nbt.setTag("Items", tags);
	}
}
