package com.mrcrayfish.vehicle.entity.vehicle;

import com.mrcrayfish.vehicle.client.EntityRaytracer.IEntityRaytraceable;
import com.mrcrayfish.vehicle.entity.BoatEntity;
import com.mrcrayfish.vehicle.entity.EngineType;
import com.mrcrayfish.vehicle.init.ModEntities;
import com.mrcrayfish.vehicle.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class AluminumBoatEntity extends BoatEntity implements IEntityRaytraceable
{
    public AluminumBoatEntity(World worldIn)
    {
        super(ModEntities.ALUMINUM_BOAT, worldIn);
        this.setMaxSpeed(10F);
        this.setTurnSensitivity(5);
        this.setMaxTurnAngle(20);
        this.setFuelCapacity(25000F);
        this.setFuelConsumption(0.5F);
    }

    @Override
    public boolean isLockable()
    {
        return false;
    }

    @Override
    public void createParticles()
    {
        if(state == State.IN_WATER)
        {
            if(this.getAcceleration() == AccelerationDirection.FORWARD)
            {
                for(int i = 0; i < 5; i++)
                {
                    this.world.addParticle(ParticleTypes.SPLASH, this.getPosX() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), this.getBoundingBox().minY + 0.1D, this.getPosZ() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), -this.getMotion().x * 4.0D, 1.5D, -this.getMotion().z * 4.0D);
                }

                for(int i = 0; i < 5; i++)
                {
                    this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), this.getBoundingBox().minY + 0.1D, this.getPosZ() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), -this.getMotion().x * 2.0D, 0.0D, -this.getMotion().z * 2.0D);
                }
            }
        }
    }

    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.SPEED_BOAT_ENGINE_MONO;
    }

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.SPEED_BOAT_ENGINE_STEREO;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.SMALL_MOTOR;
    }

    @Override
    public float getMinEnginePitch()
    {
        return 0.8F;
    }

    @Override
    public float getMaxEnginePitch()
    {
        return 1.5F;
    }

    @Override
    public double getMountedYOffset()
    {
        return 4 * 0.0625;
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            float xOffset = -0.9F;
            float yOffset = (float)((!this.isAlive() ? 0.01D : this.getMountedYOffset()) + passenger.getYOffset());
            float zOffset = -0.5F;

            if (this.getPassengers().size() > 1)
            {
                int index = this.getPassengers().indexOf(passenger);
                if (index > 0)
                {
                    xOffset += (index / 2F) * 1.2F;
                    zOffset += (index % 2) * 1F;
                }
            }

            Vec3d vec3d = (new Vec3d(xOffset, 0.0D, zOffset)).rotateYaw(-this.rotationYaw * 0.017453292F - ((float)Math.PI / 2F));
            passenger.setPosition(this.getPosX() + vec3d.x, this.getPosY() + (double)yOffset, this.getPosZ() + vec3d.z);
            passenger.rotationYaw -= deltaYaw;
            passenger.setRotationYawHead(passenger.rotationYaw);
            this.applyYawToEntity(passenger);
        }
    }

    @Override
    protected boolean canFitPassenger(Entity passenger)
    {
        return this.getPassengers().size() < 4;
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }

    @Override
    public boolean canMountTrailer()
    {
        return false;
    }


}
