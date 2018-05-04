/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.entity.EntityLumen;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Solar.
 */
public class TileLuminicMechanism extends TileLumenBase implements ITickable {

	public static final int MAX_LUMEN = 16;
	private int tick;

	public TileLuminicMechanism() {
		super(MAX_LUMEN);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			EnumFacing facing = getFacingLazy();
			if(handler.get() < handler.getMax() && tick++ % 20 == 0) {
				BlockPos pos = getPos().offset(facing.getOpposite());
				IBlockState state = world.getBlockState(pos);
				if(state.getBlock().hasTileEntity(state)) {
					TileEntity tile = world.getTileEntity(pos);
					if(tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
						IFluidHandler fluid = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
						FluidStack stack = fluid != null ? fluid.drain(250, false) : null;
						if(stack != null && stack.amount == 250) {
							fluid.drain(250, true);
							handler.fill(1);
						}
					}
				}
			}
			if(world.isAirBlock(pos.offset(facing))) spit();
			else transfer();
		}
	}

	private void spit() {
		if(handler.get() >= 16) {
			Vector3 pos = new Vector3.WrappedVec3i(getFacingLazy().getDirectionVec()).asImmutable()
					.multiply(0.5D).add(
							getPos().getX() + 0.5D,
							getPos().getY() + 0.5D,
							getPos().getZ() + 0.5D
					);
			EntityLumen lumen = EntityLumen.spawn(world, pos, handler.drain(16));
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 25F);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 25F);
			Vector3 vec = new Vector3.WrappedVec3i(getFacingLazy().getDirectionVec()).asImmutable().rotate(x.multiply(z)).multiply(0.1D);
			lumen.motionX = vec.x();
			lumen.motionY = vec.y();
			lumen.motionZ = vec.z();
		}
	}

	private void transfer() {
		if(handler.get() >= 1) {
			EnumFacing facing = getFacingLazy();
			BlockPos pos = getPos().offset(facing);
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock().hasTileEntity(state)) {
				getTile(TileEntity.class, world, pos)
						.filter(t -> t.hasCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
						.map(t -> t.getCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
						.ifPresent(lumen -> handler.drain(1 - lumen.fill(1)));
			}
		}
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
