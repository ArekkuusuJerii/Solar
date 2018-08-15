/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.entanglement.energy.data.LumenStackProvider;
import arekkuusu.solar.api.entanglement.energy.data.LumenStackWrapper;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.block.tile.TileNeutronBattery.Capacity;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 21/03/2018.
 * It's distributed as part of Solar.
 */
public class ItemNeutronBattery extends ItemBaseBlock implements IEntangledStack {

	public ItemNeutronBattery() {
		super(ModBlocks.NEUTRON_BATTERY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		getKey(stack).ifPresent(uuid -> Tooltip.inline()
				.condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(builder -> getInfo(builder, uuid)).apply()
				.build(tooltip));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return LumenStackProvider.create(new LumenStackWrapper<ItemNeutronBattery>(this, stack, 0) {
			@Override
			public int getMax() {
				return NBTHelper.getEnum(Capacity.class, stack, "capacity").orElse(Capacity.BLUE).max;
			}
		});
	}
}
