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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
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
public class BakedAlternator extends BakedBrightness {

	private TextureAtlasSprite base;
	private TextureAtlasSprite overlay_on;
	private TextureAtlasSprite overlay_off;

	@Override
	public ImmutableCollection getTextures() {
		return ImmutableList.of(
				ResourceLibrary.ALTERNATOR_BASE,
				ResourceLibrary.ALTERNATOR_OVERLAY_ON,
				ResourceLibrary.ALTERNATOR_OVERLAY_OFF
		);
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		this.base = sprites.apply(ResourceLibrary.ALTERNATOR_BASE);
		this.overlay_on = sprites.apply(ResourceLibrary.ALTERNATOR_OVERLAY_ON);
		this.overlay_off = sprites.apply(ResourceLibrary.ALTERNATOR_OVERLAY_OFF);
		return this;
	}

	@Override
	List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> quads = Lists.newArrayList();
		if(state == null) {
			//Base
			addBaseQuads(quads, format);
			//Overlay
			addOverlayQuads(quads, format, false, true);
		} else {
			switch(MinecraftForgeClient.getRenderLayer()) {
				case SOLID:
					//Base
					addBaseQuads(quads, format);
					break;
				case CUTOUT_MIPPED:
					//Overlay
					addOverlayQuads(quads, format, true, state.getValue(State.ACTIVE));
					break;
			}
		}
		return quads;
	}

	private void addBaseQuads(List<BakedQuad> quads, VertexFormat format) {
		QuadBuilder out = QuadBuilder.withFormat(format)
				.setFrom(0.5F, 8.5F, 0.5F)
				.setTo(7.5F, 15.5F, 7.5F)
				.addFace(EnumFacing.UP, 9F, 16F, 9F, 16F, base)
				.addFace(EnumFacing.DOWN, 9F, 16F, 0F, 7F, base)
				.addFace(EnumFacing.SOUTH, 9F, 16F, 0F, 7F, base)
				.addFace(EnumFacing.NORTH, 9F, 16F, 9F, 16F, base)
				.addFace(EnumFacing.WEST, 0F, 7F, 0F, 7F, base)
				.addFace(EnumFacing.EAST, 0F, 7F, 9F, 16F, base);
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 4; j++) {
				out.rotate(EnumFacing.Axis.Y, 90F);
				quads.addAll(out.bake());
			}
			out.rotate(EnumFacing.Axis.X, 180F);
		}
	}

	private void addOverlayQuads(List<BakedQuad> quads, VertexFormat format, boolean bright, boolean active) {
		quads.addAll(
				QuadBuilder.withFormat(format)
						.setFrom(2.5F, 2.5F, 2.5F)
						.setTo(13.5F, 13.5F, 13.5F)
						.addAll(0F, 11F, 0F, 11F, active ? overlay_on : overlay_off)
						.setHasBrightness(bright)
						.bake()
		);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
