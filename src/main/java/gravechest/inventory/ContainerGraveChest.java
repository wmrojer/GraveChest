package gravechest.inventory;

import gravechest.client.gui.LockedSlot;
import gravechest.tileentities.TileEntityGraveChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGraveChest extends Container {

	private final IInventory inventory;
	private final TileEntityGraveChest tile;
	public static final int numRows = 9;
	public static final int numCols = 12;
	public static final int numSlots = numRows * numCols;

	public ContainerGraveChest(InventoryPlayer inventory, TileEntityGraveChest tile) {
		this.inventory = inventory;
		this.tile = tile;
		tile.openInventory();
		
		// Use the same layout as Diamond Chest from IronChests
		// Containers inventory
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				addSlotToContainer(new LockedSlot(tile, j + i * numCols, 12 + j * 18, 8 + i * 18)); 

		// Player inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 39 + j * 18, 12 + numRows * 18 + i * 18));  
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventory, i, 39 + i * 18, 70 + numRows * 18));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotItemStack = slot.getStack();
			itemStack = slotItemStack.copy();

			if (slotIndex < numSlots) {
				if (!mergeItemStack(slotItemStack, numSlots, inventorySlots.size(), false)) // Only allow merge from chest to inventory
					return null;
			} else
				return null;	// Don't allow merge from inventory to chest

			if (slotItemStack.stackSize == 0)
				slot.putStack((ItemStack) null);
			else
				slot.onSlotChanged();
		}
		return itemStack;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile.closeInventory();
	}
	
	@Override
	public final boolean canInteractWith(EntityPlayer player) {
		return inventory == null || inventory.isUseableByPlayer(player);
	}
}