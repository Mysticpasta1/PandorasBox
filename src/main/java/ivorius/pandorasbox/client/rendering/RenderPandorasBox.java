/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import ivorius.pandorasbox.PandorasBox;
import ivorius.pandorasbox.client.rendering.effects.PBEffectRenderer;
import ivorius.pandorasbox.client.rendering.effects.PBEffectRenderingRegistry;
import ivorius.pandorasbox.effects.PBEffect;
import ivorius.pandorasbox.entitites.EntityPandorasBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 30.03.14.
 */
@OnlyIn(Dist.CLIENT)
public class RenderPandorasBox extends EntityRenderer<EntityPandorasBox>
{
    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation("textures/entity/pandoras_box.png");
    private final ModelPandorasBox model;

    public RenderPandorasBox(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ModelPandorasBox(pContext.bakeLayer(ModelPandorasBox.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPandorasBox pEntity) {
        return TRIDENT_LOCATION;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks)
    {
        EntityPandorasBox entityPandorasBox = (EntityPandorasBox) entity;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + MathHelper.sin((entity.ticksExisted + partialTicks) * 0.04f) * 0.05, z);
        GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);

        PBEffect effect = entityPandorasBox.getBoxEffect();
        if (!effect.isDone(entityPandorasBox, entityPandorasBox.getEffectTicksExisted()) && entityPandorasBox.getDeathTicks() < 0)
        {
            PBEffectRenderer renderer = PBEffectRenderingRegistry.rendererForEffect(effect);
            if (renderer != null)
                renderer.renderBox(entityPandorasBox, effect, partialTicks);
        }

        if (!entity.isInvisible())
        {
            float boxScale = entityPandorasBox.getCurrentScale();
            if (boxScale < 1.0f)
                GlStateManager.scale(boxScale, boxScale, boxScale);

            GlStateManager.translate(0.0f, 1.5f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            EntityArrow emptyEntity = new EntityTippedArrow(entity.world);
            emptyEntity.rotationPitch = entityPandorasBox.getRatioBoxOpen(partialTicks) * 120.0f / 180.0f * 3.1415926f;
            bindEntityTexture(entity);
            model.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

//            int animationCounter = Math.min(89, MathHelper.floor((entityPandorasBox.getRatioBoxOpen(partialTicks) + 0.5f) * 90));
//            renderB3DModel(renderManager.renderEngine, model, animationCounter);
        }

        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, yaw, partialTicks);
    }
    @Override
    public void render(EntityPandorasBox pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(x, y + MathHelper.sin((entity.ticksExisted + partialTicks) * 0.04f) * 0.05, z);
        pMatrixStack.rotate(-yaw, 0.0F, 1.0F, 0.0F);
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
