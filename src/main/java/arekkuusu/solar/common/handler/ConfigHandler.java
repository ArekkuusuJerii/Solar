/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler;

import arekkuusu.solar.common.lib.LibMod;
import net.minecraftforge.common.config.Config;

import static net.minecraftforge.common.config.Config.*;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Config(modid = LibMod.MOD_ID)
public final class ConfigHandler {

	@Comment("Enable/Disable/Play with structure spawn rates")
	@RequiresMcRestart
	@LangKey(LibMod.MOD_ID + ".config.gen")
	public static Gen GEN_CONFIG = new Gen();

	public static class Gen {

		public final AshenCubeStructureConfig ASHEN_CUBE_STRUCTURE = new AshenCubeStructureConfig();
		public final MonolithConfig MONOLITH_CONFIG = new MonolithConfig();

		public static class AshenCubeStructureConfig {

			public final AshenWeights WEIGHTS = new AshenWeights();

			@Comment("If the structure should generate underground only --Debug stuff--")
			public boolean underground = true;
			@Comment("Max amount of \"nuggets\" allowed in one Structure")
			public double size = 25;
			@Comment("Spread of \"nuggets\" in the structure")
			public int spread = 15;
			@Comment("Chance of 0-100% for loot")
			public double loot = 15;
			@Comment("Chance of 0-100% to generate")
			public double rarity = 0.01;

			public static class AshenWeights {
				@Comment("Weight of big \"nuggets\"")
				public int big = 10;
				@Comment("Weight of small \"nuggets\"")
				public int small = 6;
				@Comment("Weight of spawn \"nuggets\"")
				public int spawn = 1;
			}
		}

		public static class MonolithConfig {

			public final HollowMonolithStructure MONOLITH_STRUCTURE = new HollowMonolithStructure();
			public final ObeliskDecorator OBELISK_DECORATOR = new ObeliskDecorator();

			public static class HollowMonolithStructure {

				@Comment("Chance of 0-100% to generate")
				public double rarity = 10;
				@Comment("If obelisks should generate around the structure")
				public boolean generateObelisks = true;
			}

			public static class ObeliskDecorator {

				public final ObeliskWeights WEIGHTS = new ObeliskWeights();

				@Comment("Chance of 0-100% to generate")
				public double rarity = 10;
				@Comment("Max amount of obelisks allowed in one chunk")
				public double size = 1;

				public static class ObeliskWeights {
					@Comment("Weight of monolithic obelisk")
					public int monolithic = 2;
					@Comment("Weight of fragmented obelisk")
					public int fragmented = 1;
				}
			}
		}
	}
}
