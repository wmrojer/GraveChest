package gravechest.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class LockedSlot extends Slot {

	public LockedSlot(IInventory inventory, int slot, int posX, int posY) {
		super(inventory, slot, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return inventory.isItemValidForSlot(getSlotIndex(), stack);
	}
}