/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Solar.
 */
public class TileKondenzator extends TileBase {

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
