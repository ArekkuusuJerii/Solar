/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler;

import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
public class CreativeTabHandler {

	public static final CreativeTab MISC_ITEMS = new MiscItems();
	public static final CreativeTab MISC_BLOCKS = new MiscBlocks();

	private static abstract class CreativeTab extends CreativeTabs {

		NonNullList<ItemStack> list;

		CreativeTab(String name) {
			super(LibMod.MOD_ID + "." + name);
		}

		@Override
		@SideOnly(Side.CLIENT)
		@Nonnull
		public ItemStack getTabIconItem() {
			return getIconItemStack();
		}

		@SideOnly(Side.CLIENT)
		void addItem(Item item) {
			item.getSubItems(this, list);
		}

		@SideOnly(Side.CLIENT)
		void addBlock(Block block) {
			block.getSubBlocks(this, list);
		}
	}

	private static class MiscItems extends CreativeTab {

		MiscItems() {
			super("misc_items");
			setBackgroundImageName("items.png");
		}

		@Override
		@Nonnull
		public ItemStack getIconItemStack() {
			return new ItemStack(ModItems.quingentilliard);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
			this.list = list;
			addItem(ModItems.quingentilliard);
			addItem(ModItems.quantum_quartz);
		}
	}

	private static class MiscBlocks extends CreativeTab {

		MiscBlocks() {
			super("misc_blocks");
			setBackgroundImageName("items.png");
		}

		@Override
		@Nonnull
		public ItemStack getIconItemStack() {
			return new ItemStack(ModBlocks.schrodinger_glyph);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
			this.list = list;
			addBlock(ModBlocks.primal_stone);
			addBlock(ModBlocks.primal_glyph);
			addBlock(ModBlocks.schrodinger_glyph);
			addBlock(ModBlocks.phenomena);
			addBlock(ModBlocks.q_squared);
			addBlock(ModBlocks.theorema);
			addBlock(ModBlocks.singularity);
			addBlock(ModBlocks.prism_flower);
			addBlock(ModBlocks.quantum_mirror);
			addBlock(ModBlocks.gravity_hopper);
			addBlock(ModBlocks.crystal_void);
			addBlock(ModBlocks.blinker);
		}
	}
}
