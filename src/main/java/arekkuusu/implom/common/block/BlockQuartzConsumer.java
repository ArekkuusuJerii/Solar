/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileQuartzConsumer;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 4/30/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockQuartzConsumer extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.5625, 0.75);

	public BlockQuartzConsumer() {
		super(LibNames.QUARTZ_CONSUMER, IPMMaterial.MONOLITH);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		Vector3 posVec = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D, 0.55D, 0.5D);
		getTile(TileQuartzConsumer.class, world, pos).ifPresent(t -> {
			for(int i = 0; i < 3 + rand.nextInt(6); i++) {
				Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 45);
				Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 45);
				Vector3 randVec = Vector3.randomVector().multiply(0.1D);
				double speed = 0.005D + rand.nextDouble() * 0.005D;
				Vector3 speedVec = Vector3.rotateRandom()
						.multiply(speed)
						.rotate(x.multiply(z));
				IPM.getProxy().spawnSpeck(world, posVec.add(randVec), speedVec, 45, 0.5F, 0x49FFFF, Light.GLOW, ResourceLibrary.GLOW_PARTICLE);
			}
		});
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face != EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileQuartzConsumer();
	}
}
