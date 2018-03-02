/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.util.resource.SpriteManager;
import arekkuusu.solar.client.util.resource.sprite.FrameSpriteResource;
import arekkuusu.solar.client.util.resource.sprite.SpriteResource;
import net.katsstuff.mirror.client.helper.TextureLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 03/07/2017.
 * It's distributed as part of solar.
 */
@SideOnly(Side.CLIENT)
public final class SpriteLibrary {

	public static final FrameSpriteResource QUORN_PARTICLE = SpriteManager.load(
			TextureLocation.Effect(), "quorn_particle", 7, 1
	);
	public static final SpriteResource NEUTRON_PARTICLE = SpriteManager.load(
			TextureLocation.Effect(), "neutron_particle"
	);
	public static final SpriteResource LIGHT_PARTICLE = SpriteManager.load(
			TextureLocation.Effect(), "light_particle"
	);
	public static final SpriteResource DARK_PARTICLE = SpriteManager.load(
			TextureLocation.Effect(), "dark_particle"
	);
	public static final SpriteResource CHARGED_ICE = SpriteManager.load(
			TextureLocation.Effect(), "charged_ice"
	);
	public static final FrameSpriteResource SQUARED = SpriteManager.load(
			TextureLocation.Effect(), "squared", 11, 1
	);
	public static final FrameSpriteResource QUANTUM_MIRROR = SpriteManager.load(
			TextureLocation.Blocks(), "quantum_mirror", 9, 1
	);
	public static final SpriteResource Q_SQUARED = SpriteManager.load(
			TextureLocation.Blocks(), "q_squared"
	);
	public static final SpriteResource EYE_OF_SCHRODINGER_LAYER = SpriteManager.load(
			TextureLocation.Model(), "eye_of_schrodinger_layer"
	);

	public static void init() {}
}
