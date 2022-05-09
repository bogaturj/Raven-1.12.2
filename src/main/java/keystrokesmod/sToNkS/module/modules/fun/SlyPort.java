package keystrokesmod.sToNkS.module.modules.fun;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleDesc;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.module.modules.world.AntiBot;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;

public class SlyPort extends Module {
    public static ModuleDesc f;
    public static ModuleSettingSlider r;
    public static ModuleSettingTick b;
    public static ModuleSettingTick d;
    public static ModuleSettingTick e;
    private final boolean s = false;

    public SlyPort() {
        super("SlyPort", Module.category.fun, 0);
        this.registerSetting(f = new ModuleDesc("Teleport behind enemies."));
        this.registerSetting(r = new ModuleSettingSlider("Range", 6.0D, 2.0D, 15.0D, 1.0D));
        this.registerSetting(e = new ModuleSettingTick("Aim", true));
        this.registerSetting(b = new ModuleSettingTick("Play sound", true));
        this.registerSetting(d = new ModuleSettingTick("Players only", true));
    }

    public void onEnable() {
        Entity en = this.ge();
        if (en != null) {
            this.tp(en);
        }

        this.disable();
    }

    private void tp(Entity en) {
        if (b.isToggled()) {
            mc.player.playSound(SoundEvents.ENTITY_ENDERMEN_AMBIENT, 1.0F, 1.0F);
        }

        Vec3d vec = en.getLookVec();
        double x = en.posX - vec.x * 2.5D;
        double z = en.posZ - vec.z * 2.5D;
        mc.player.setPosition(x, mc.player.posY, z);
        if (e.isToggled()) {
            Utils.Player.aim(en, 0.0F, false);
        }

    }

    private Entity ge() {
        Entity en = null;
        double r = Math.pow(SlyPort.r.getInput(), 2.0D);
        double dist = r + 1.0D;
        Iterator var6 = mc.world.loadedEntityList.iterator();

        while(true) {
            Entity ent;
            do {
                do {
                    do {
                        do {
                            if (!var6.hasNext()) {
                                return en;
                            }

                            ent = (Entity)var6.next();
                        } while(ent == mc.player);
                    } while(!(ent instanceof EntityLivingBase));
                } while(((EntityLivingBase)ent).deathTime != 0);
            } while(SlyPort.d.isToggled() && !(ent instanceof EntityPlayer));

            if (!AntiBot.bot(ent)) {
                double d = mc.player.getDistanceSq(ent);
                if (!(d > r) && !(dist < d)) {
                    dist = d;
                    en = ent;
                }
            }
        }
    }
}
