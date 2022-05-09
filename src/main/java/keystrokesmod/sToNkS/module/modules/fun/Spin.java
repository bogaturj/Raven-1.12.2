package keystrokesmod.sToNkS.module.modules.fun;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import net.minecraft.client.entity.EntityPlayerSP;

public class Spin extends Module {
    public static ModuleSettingSlider a;
    public static ModuleSettingSlider b;
    private float yaw;

    public Spin() {
        super("Spin", Module.category.fun, 0);
        this.registerSetting(a = new ModuleSettingSlider("Rotation", 360.0D, 30.0D, 360.0D, 1.0D));
        this.registerSetting(b = new ModuleSettingSlider("Speed", 25.0D, 1.0D, 60.0D, 1.0D));
    }

    public void onEnable() {
        this.yaw = mc.player.rotationYaw;
    }

    public void onDisable() {
        this.yaw = 0.0F;
    }

    public void update() {
        double left = (double)this.yaw + a.getInput() - (double)mc.player.rotationYaw;
        EntityPlayerSP var10000;
        if (left < b.getInput()) {
            var10000 = mc.player;
            var10000.rotationYaw = (float)((double)var10000.rotationYaw + left);
            this.disable();
        } else {
            var10000 = mc.player;
            var10000.rotationYaw = (float)((double)var10000.rotationYaw + b.getInput());
            if ((double)mc.player.rotationYaw >= (double)this.yaw + a.getInput()) {
                this.disable();
            }
        }

    }
}