package keystrokesmod.sToNkS.module.modules.combat;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.module.modules.world.AntiBot;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.MouseEvent;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;

import java.util.Iterator;

public class RodAimbot extends Module {
   public static ModuleSettingSlider a;
   public static ModuleSettingSlider b;
   public static ModuleSettingTick c;

   public RodAimbot() {
      super("RodAimbot", Module.category.combat, 0);
      this.registerSetting(a = new ModuleSettingSlider("FOV", 90.0D, 15.0D, 360.0D, 1.0D));
      this.registerSetting(b = new ModuleSettingSlider("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
      this.registerSetting(c = new ModuleSettingTick("Aim invis", false));
   }

   @FMLEvent
   public void x(MouseEvent ev) {
      if (ev.getButton() == 1 && ev.isButtonstate() && Utils.Player.isPlayerInGame() && mc.currentScreen == null) {
         if (mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod && mc.player.fishEntity == null) {
            Entity en = this.gE();
            if (en != null) {
               ev.setCanceled(true);
               Utils.Player.aim(en, -7.0F, true);
               mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
            }
         }

      }
   }

   public Entity gE() {
      int f = (int)a.getInput();
      Iterator var2 = mc.world.playerEntities.iterator();

      EntityPlayer en;
      do {
         do {
            do {
               do {
                  if (!var2.hasNext()) {
                     return null;
                  }

                  en = (EntityPlayer)var2.next();
               } while(en == mc.player);
            } while(en.deathTime != 0);
         } while(!c.isToggled() && en.isInvisible());
      } while((double)mc.player.getDistance(en) > b.getInput() || AntiBot.bot(en) || !Utils.Player.fov(en, (float)f));

      return en;
   }
}
