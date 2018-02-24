/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.util.ResourceLibrary;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class BakedElectron extends BakedBrightness {

	private TextureAtlasSprite base_on;
	private TextureAtlasSprite base_off;

	@Override
	public ImmutableCollection getTextures() {
		return ImmutableList.of(
				ResourceLibrary.ELECTRON_ON,
				ResourceLibrary.ELECTRON_OFF
		);
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		this.base_on = sprites.apply(ResourceLibrary.ELECTRON_ON);
		this.base_off = sprites.apply(ResourceLibrary.ELECTRON_OFF);
		return this;
	}

	@Override
	List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> quads = Lists.newArrayList();
		if(state == null) {
			addBase(quads, format, false);
		} else {
			addBase(quads, format, state.getValue(State.POWER) > 0);
		}
		return quads;
	}

	private void addBase(List<BakedQuad> quads, VertexFormat format, boolean on) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(5.5D, 5.5D, 5.5D)
				.setTo(10.5D, 10.5D, 10.5D)
				.addAll(0F, 5F, 0, 5F, on ? base_on : base_off)
				.setHasBrightness(on)
				.bake()
		);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base_off;
	}
}
