package keystrokesmod.sToNkS.module.modules.render;

import keystrokesmod.sToNkS.main.Ravenbplus;
import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.module.modules.world.AntiBot;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;

import java.awt.*;
import java.util.Iterator;

public class Tracers extends Module {
   public static ModuleSettingTick a;
   public static ModuleSettingSlider b;
   public static ModuleSettingSlider c;
   public static ModuleSettingSlider d;
   public static ModuleSettingTick e;
   public static ModuleSettingSlider f;
   private boolean g;
   private int rgb_c = 0;

   public Tracers() {
      super("Tracers", Module.category.render, 0);
      this.registerSetting(a = new ModuleSettingTick("Show invis", true));
      this.registerSetting(f = new ModuleSettingSlider("Line Width", 1.0D, 1.0D, 5.0D, 1.0D));
      this.registerSetting(b = new ModuleSettingSlider("Red", 0.0D, 0.0D, 255.0D, 1.0D));
      this.registerSetting(c = new ModuleSettingSlider("Green", 255.0D, 0.0D, 255.0D, 1.0D));
      this.registerSetting(d = new ModuleSettingSlider("Blue", 0.0D, 0.0D, 255.0D, 1.0D));
      this.registerSetting(e = new ModuleSettingTick("Rainbow", false));
   }

   public void onEnable() {
      this.g = mc.gameSettings.viewBobbing;
      if (this.g) {
         mc.gameSettings.viewBobbing = false;
      }

   }

   public void onDisable() {
      mc.gameSettings.viewBobbing = this.g;
   }

   public void update() {
      if (mc.gameSettings.viewBobbing) {
         mc.gameSettings.viewBobbing = false;
      }

   }

   public void guiUpdate() {
      this.rgb_c = (new Color((int)b.getInput(), (int)c.getInput(), (int)d.getInput())).getRGB();
   }

   @FMLEvent
   public void o(RenderWorldLastEvent ev) {
      if (Utils.Player.isPlayerInGame()) {
         int rgb = e.isToggled() ? Utils.Client.rainbowDraw(2L, 0L) : this.rgb_c;
         Iterator var3;
         if (Ravenbplus.debugger) {
            var3 = mc.world.loadedEntityList.iterator();

            while(var3.hasNext()) {
               Entity en = (Entity)var3.next();
               if (en instanceof EntityLivingBase && en != mc.player) {
                  Utils.HUD.dtl(en, rgb, (float)f.getInput());
               }
            }

         } else {
            var3 = mc.world.playerEntities.iterator();

            while(true) {
               EntityPlayer en;
               do {
                  do {
                     do {
                        if (!var3.hasNext()) {
                           return;
                        }

                        en = (EntityPlayer)var3.next();
                     } while(en == mc.player);
                  } while(en.deathTime != 0);
               } while(!a.isToggled() && en.isInvisible());

               if (!AntiBot.bot(en)) {
                  Utils.HUD.dtl(en, rgb, (float)f.getInput());
               }
            }
         }
      }
   }
}
