/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.network.PacketHelper;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 08/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TilePhenomena extends TileBase implements ITickable {

	private static final ResourceLocation ASM = new ResourceLocation(LibMod.MOD_ID, "armatures/phenomena_asm.json");

	private final IAnimationStateMachine animation;
	private boolean powered;
	private boolean inverse;
	public int timer;

	public TilePhenomena() {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			animation = ModelLoaderRegistry.loadASM(ASM, ImmutableMap.of());
		} else {
			animation = null;
		}
	}

	@Override
	public void update() {
		if(hasCooldown()) {
			--timer;
			if(world.isRemote) {
				if(timer <= 0) {
					animation.transition("default");
				}
			} else {
				if(inverse && timer == 5) inverse();
				if(timer == 10) propagate();
			}
			markDirty();
		}
	}

	public void makePhenomenon() {
		if(!hasCooldown()) {
			if(world.isRemote) {
				animation.transition(isInvisible() ? "grow" : "shrink");
			} else {
				inverse = isInvisible();
				if(!inverse) inverse();
				PacketHelper.sendPhenomenaPacket(this);
			}
			timer = 20;
		}
	}

	private void inverse() {
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(Properties.ACTIVE, !state.getValue(Properties.ACTIVE)));
	}

	private void propagate() {
		for(EnumFacing facing : EnumFacing.values()) {
			BlockPos offset = pos.offset(facing);
			IBlockState state = world.getBlockState(offset);
			if(state.getBlock() == ModBlocks.PHENOMENA) {
				TilePhenomena phenomena = (TilePhenomena) world.getTileEntity(offset);
				if(phenomena != null) {
					phenomena.makePhenomenon();
				}
			}
		}
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
	}

	public boolean hasCooldown() {
		return timer > 0;
	}

	public boolean isInvisible() {
		return !getStateValue(Properties.ACTIVE, pos).orElse(true);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityAnimation.ANIMATION_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityAnimation.ANIMATION_CAPABILITY ? CapabilityAnimation.ANIMATION_CAPABILITY.cast(animation) : null;
	}

	@Override
	public boolean hasFastRenderer() {
		return hasCooldown();
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		inverse = cmp.getBoolean("inverse");
		timer = cmp.getInteger("timer");
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		cmp.setBoolean("inverse", inverse);
		cmp.setInteger("timer", timer);
	}
}
