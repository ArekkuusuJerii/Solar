/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.lib.LibMod;
import net.katsstuff.mirror.client.helper.ResourceHelperStatic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
public class ItemMonolithicGlyph extends ItemBaseBlock {

	public ItemMonolithicGlyph() {
		super(ModBlocks.MONOLITHIC_GLYPH);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		for (int i = 0; i < 16; i++) {
			ModelHandler.registerModel(this, i, ResourceHelperStatic.getModel(LibMod.MOD_ID, "monolithic_glyph_", "glyph=" + i));
		}
	}
}
