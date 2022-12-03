/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import ivorius.pandorasbox.client.rendering.effects.PBEffectRenderer;
import ivorius.pandorasbox.client.rendering.effects.PBEffectRenderingRegistry;
import ivorius.pandorasbox.effects.PBEffect;
import ivorius.pandorasbox.entitites.EntityPandorasBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by lukas on 30.03.14.
 */
@OnlyIn(Dist.CLIENT)
public class RenderPandorasBoxEntity extends EntityRenderer<EntityPandorasBox>
{
    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation("textures/entity/pandoras_box.png");
    private final ModelPandorasBox model;

    public RenderPandorasBoxEntity(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ModelPandorasBox(pContext.bakeLayer(ModelPandorasBox.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPandorasBox pEntity) {
        return TRIDENT_LOCATION;
    }
    @Override
    public void render(EntityPandorasBox pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(pEntity.getX(), pEntity.getY() + Mth.sin((pEntity.tickCount + pPartialTicks) * 0.04f) * 0.05, pEntity.getZ());
        pMatrixStack.mulPose(Vector3f.YP.rotation(1.0F));

        PBEffect effect = pEntity.getBoxEffect();
        if (!effect.isDone(pEntity, pEntity.getEffectTicksExisted()) && pEntity.getDeathTicks() < 0)
        {
            PBEffectRenderer renderer = PBEffectRenderingRegistry.rendererForEffect(effect);
            if (renderer != null)
                renderer.renderBox(pEntity, effect, pPartialTicks);
        }

        if (!pEntity.isInvisible())
        {
            float boxScale = pEntity.getCurrentScale();
            if (boxScale < 1.0f)
                pMatrixStack.scale(boxScale, boxScale, boxScale);

            pMatrixStack.translate(0.0f, 1.5f, 0.0f);
            pMatrixStack.mulPose(Vector3f.ZP.rotation(1));
            AbstractArrow emptyEntity = ((ArrowItem)Items.ARROW).createArrow(pEntity.level, new ItemStack(Items.ARROW), Minecraft.getInstance().player);
            emptyEntity.rotate(Rotation.CLOCKWISE_90);
        }
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
