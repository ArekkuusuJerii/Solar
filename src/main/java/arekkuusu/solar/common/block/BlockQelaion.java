/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedQelaion;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileQelaion;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Created by <Snack> on 24/02/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQelaion extends BlockBase {

	public static final PropertyBool HAS_NODE = PropertyBool.create("has_node");

	public BlockQelaion() {
		super(LibNames.QELAION, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(HAS_NODE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
		setLightLevel(0.2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return true;
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItem() == ModItems.QELAION) {
			Optional<TileQelaion> optional = getTile(TileQelaion.class, world, pos);
			if(optional.isPresent()) {
				TileQelaion qelaion = optional.get();
				Optional<UUID> nodes = ((IEntangledStack) stack.getItem()).getKey(stack);
				Optional<UUID> parent = qelaion.getKey();
				if(nodes.isPresent() && parent.isPresent() && !nodes.get().equals(parent.get())) {
					qelaion.setNodes(nodes.get());
					return true;
				}
				return false;
			}
		} else if(stack.isEmpty()) {
			getTile(TileQelaion.class, world, pos).ifPresent(qelaion -> {
				qelaion.putFacing(facing);
			});
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileQelaion.class, world, pos).ifPresent(qelaion -> {
				IEntangledStack entangled = (IEntangledStack) stack.getItem();
				if(!entangled.getKey(stack).isPresent()) {
					entangled.setKey(stack, UUID.randomUUID());
				}
				entangled.getKey(stack).ifPresent(qelaion::setKey);
				if(NBTHelper.hasUniqueID(stack, "nodes")) {
					qelaion.setNodes(NBTHelper.getUniqueID(stack, "nodes"));
				}
			});
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileQelaion> optional = getTile(TileQelaion.class, world, pos);
		if(optional.isPresent()) {
			TileQelaion qelaion = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			qelaion.getKey().ifPresent(uuid -> {
				((IEntangledStack) stack.getItem()).setKey(stack, uuid);
			});
			if(qelaion.getNodes() != null) {
				NBTHelper.setUniqueID(stack, "nodes", qelaion.getNodes());
			}
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		getTile(TileQelaion.class, world, pos).ifPresent(qelaion -> {
			ImmutableList<EnumFacing> facings = qelaion.getFacings();
			for(EnumFacing facing : EnumFacing.values()) {
				boolean on = facings.contains(facing);
				for(int i = 0; i < 1 + rand.nextInt(3); i++) {
					Vector3 vec = Vector3.create(facing).multiply(0.025D + 0.005D * rand.nextDouble());
					vec.rotatePitchX((world.rand.nextFloat() * 2F - 1F) * 0.25F);
					vec.rotatePitchZ((world.rand.nextFloat() * 2F - 1F) * 0.25F);
					ParticleUtil.spawnLightParticle(world, Vector3.create(pos).add(0.5D), vec, on ? 0x49FFFF : 0xFF0303, 60, 2F);
				}
			}

			qelaion.getFacings().forEach(facing -> {

			});
		});
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(HAS_NODE) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(HAS_NODE, meta != 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HAS_NODE);
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
		return new TileQelaion();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, BakedQelaion::new);
		ModelHandler.registerModel(this, 0, "");
	}
}