package com.mrcrayfish.vehicle.client.render.vehicle;

import com.mrcrayfish.vehicle.client.EntityRaytracer;
import com.mrcrayfish.vehicle.client.Models;
import com.mrcrayfish.vehicle.client.render.AbstractRenderVehicle;
import com.mrcrayfish.vehicle.client.render.RenderVehicle;
import com.mrcrayfish.vehicle.common.inventory.StorageInventory;
import com.mrcrayfish.vehicle.entity.trailer.EntitySeederTrailer;
import com.mrcrayfish.vehicle.entity.trailer.EntityStorageTrailer;
import com.mrcrayfish.vehicle.entity.trailer.EntityTrailer;
import com.mrcrayfish.vehicle.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.Calendar;

/**
 * Author: MrCrayfish
 */
public class RenderSeederTrailer extends RenderVehicle<EntitySeederTrailer, AbstractRenderVehicle<EntitySeederTrailer>>
{
    public RenderSeederTrailer(RenderManager renderManager)
    {
        super(renderManager, null);
    }

    @Override
    public void doRender(EntitySeederTrailer entity, double x, double y, double z, float currentYaw, float partialTicks)
    {
        RenderHelper.enableStandardItemLighting();

        EntityLivingBase entityLivingBase = (EntityLivingBase) entity.getControllingPassenger();
        if(entityLivingBase != null)
        {
            entityLivingBase.renderYawOffset = currentYaw;
            entityLivingBase.prevRenderYawOffset = currentYaw;
        }

        //Render the tow bar. Performed before scaling so size is consistent for all vehicles
        if(entity.canTowTrailer())
        {
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-currentYaw, 0, 1, 0);
                GlStateManager.rotate(180F, 0, 1, 0);

                Vec3d towBarOffset = entity.getTowBarVec();
                GlStateManager.translate(towBarOffset.x, towBarOffset.y + 0.5, -towBarOffset.z);
                Minecraft.getMinecraft().getRenderItem().renderItem(entity.towBar, ItemCameraTransforms.TransformType.NONE);
            }
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-currentYaw, 0, 1, 0);
            GlStateManager.scale(1.1, 1.1, 1.1);

            this.setupBreakAnimation(entity, partialTicks);

            double bodyOffset = 0.8;

            //Render the body
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(0, bodyOffset, 0);
                RenderUtil.renderItemModel(entity.body, Models.SEEDER_TRAILER.getModel(), ItemCameraTransforms.TransformType.NONE);
            }
            GlStateManager.popMatrix();

            RenderHelper.enableStandardItemLighting();

            this.renderWheel(entity, -17.5F * 0.0625F, 0.3F, 0.0F, 2.0F, partialTicks);
            this.renderWheel(entity, 17.5F * 0.0625F, 0.3F, 0.0F, 2.0F, partialTicks);

            StorageInventory inventory = entity.getChest();
            if(inventory != null)
            {
                int layer = 0;
                int index = 0;
                for(int i = 0; i < inventory.getSizeInventory(); i++)
                {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if(!stack.isEmpty())
                    {
                        GlStateManager.pushMatrix();
                        {
                            GlStateManager.translate(-10.5 * 0.0625, 10 * 0.0625, -2 * 0.0625);
                            GlStateManager.scale(0.45, 0.45, 0.45);

                            int count = Math.max(1, stack.getCount() / 16);
                            int width = 4;
                            int maxLayerCount = 8;
                            for(int j = 0; j < count; j++)
                            {
                                GlStateManager.pushMatrix();
                                {
                                    int layerIndex = index % maxLayerCount;
                                    //double yOffset = Math.sin(Math.PI * (((layerIndex + 0.5) % (double) width) / (double) width)) * 0.1;
                                    //GlStateManager.translate(0, yOffset * ((double) layer / inventory.getSizeInventory()), 0);
                                    GlStateManager.translate(0, layer * 0.05, 0);
                                    GlStateManager.translate((layerIndex % width) * 0.75, 0, (layerIndex / width) * 0.5);
                                    GlStateManager.translate(0.7 * (layer % 2), 0, 0);
                                    GlStateManager.rotate(90F, 1, 0, 0);
                                    GlStateManager.rotate(47F * index, 0, 0, 1);
                                    GlStateManager.rotate(2F * layerIndex, 1, 0, 0);
                                    GlStateManager.translate(layer * 0.001, layer * 0.001, layer * 0.001); // Fixes Z fighting
                                    Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
                                }
                                GlStateManager.popMatrix();
                                index++;
                            }
                            if(index % maxLayerCount == 0)
                            {
                                layer++;
                            }
                        }
                        GlStateManager.popMatrix();
                    }
                }
            }

            this.renderSpiker(entity, -12.0F * 0.0625F, 0.15F, 0.0F, 0.75F, partialTicks);
            this.renderSpiker(entity, -8.0F * 0.0625F, 0.15F, 0.0F, 0.75F, partialTicks);
            this.renderSpiker(entity, -4.0F * 0.0625F, 0.15F, 0.0F, 0.75F, partialTicks);
            this.renderSpiker(entity, 0.0F, 0.15F, 0.0F, 0.75F, partialTicks);
            this.renderSpiker(entity, 4.0F * 0.0625F, 0.15F, 0.0F, 0.75F, partialTicks);
            this.renderSpiker(entity, 8.0F * 0.0625F, 0.15F, 0.0F, 0.75F, partialTicks);
            this.renderSpiker(entity, 12.0F * 0.0625F, 0.15F, 0.0F, 0.75F, partialTicks);
        }
        GlStateManager.popMatrix();

        EntityRaytracer.renderRaytraceElements(entity, x, y, z, currentYaw);
    }

    public void renderWheel(EntityTrailer trailer, float offsetX, float offsetY, float offsetZ, float wheelScale, float partialTicks)
    {
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(offsetX, offsetY, offsetZ);
            GlStateManager.pushMatrix();
            {
                GlStateManager.pushMatrix();
                {
                    float wheelRotation = trailer.prevWheelRotation + (trailer.wheelRotation - trailer.prevWheelRotation) * partialTicks;
                    GlStateManager.rotate(-wheelRotation, 1, 0, 0);
                    GlStateManager.scale(wheelScale, wheelScale, wheelScale);
                    Minecraft.getMinecraft().getRenderItem().renderItem(trailer.wheel, ItemCameraTransforms.TransformType.NONE);
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    public void renderSpiker(EntityTrailer trailer, float offsetX, float offsetY, float offsetZ, float wheelScale, float partialTicks)
    {
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(offsetX, offsetY, offsetZ);
            GlStateManager.pushMatrix();
            {
                GlStateManager.pushMatrix();
                {
                    float wheelRotation = trailer.prevWheelRotation + (trailer.wheelRotation - trailer.prevWheelRotation) * partialTicks;
                    GlStateManager.rotate(-wheelRotation, 1, 0, 0);
                    GlStateManager.scale(wheelScale, wheelScale, wheelScale);
                    RenderUtil.renderItemModel(trailer.body, Models.SEED_SPIKER.getModel(), ItemCameraTransforms.TransformType.NONE);
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }
}