package arekkuusu.implom.common.handler.data.capability;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.common.block.BlockNeutronBattery;
import arekkuusu.implom.common.item.ItemQuartz;
import arekkuusu.implom.common.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InventoryNeutronCapability implements IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {

	private BlockNeutronBattery.BatteryCapacitor capacitor;
	private ItemStack stack = ItemStack.EMPTY;

	@Nullable
	public BlockNeutronBattery.BatteryCapacitor getCapacitor() {
		return capacitor;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return stack.getItem() == ModItems.QUARTZ && NBTHelper.getEnum(ItemQuartz.Quartz.class, NBTHelper.fixNBT(stack), ItemQuartz.Constants.NBT_QUARTZ)
				.map(q -> q.size == ItemQuartz.Quartz.Size.MEDIUM)
				.orElse(false);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		this.stack = stack;
		if(!stack.isEmpty()) {
			ItemQuartz.Quartz quartz = NBTHelper.getEnum(ItemQuartz.Quartz.class, NBTHelper.fixNBT(stack), ItemQuartz.Constants.NBT_QUARTZ).orElse(null);
			capacitor = BlockNeutronBattery.Conversions.QUARTZ_TO_BATTERY.get(quartz);
		} else capacitor = null;
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return stack;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if(stack.isEmpty()) return ItemStack.EMPTY;
		ItemStack existing = getStackInSlot(slot);
		if(!existing.isEmpty() || !isItemValid(slot, stack)) return stack;
		if(!simulate) {
			existing = stack.copy();
			existing.setCount(1);
			setStackInSlot(slot, existing);
			onChange();
		}
		return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(amount == 0) return ItemStack.EMPTY;
		ItemStack existing = getStackInSlot(slot);
		if(existing.isEmpty()) return ItemStack.EMPTY;
		if(!simulate) {
			setStackInSlot(slot, ItemStack.EMPTY);
			onChange();
		}
		return existing;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	protected void onChange() {
		//For Rent
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return stack.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		ItemStack stack = new ItemStack(nbt);
		setStackInSlot(0, stack);
	}
}
