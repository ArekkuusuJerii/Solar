/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.ModBlocks;

/**
 * Created by <Arekkuusu> on 16/12/2017.
 * It's distributed as part of Solar.
 */
public class ItemElectron extends ItemBaseBlock {

	public ItemElectron() {
		super(ModBlocks.ELECTRON);
	}

	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0, ResourceLibrary.getModel("electron_", ""));
	}
}
