package keystrokesmod.sToNkS.module.modules.player;

import keystrokesmod.sToNkS.module.*;
import keystrokesmod.sToNkS.module.modules.movement.Fly;

public class FallSpeed extends Module {
   public static ModuleDesc dc;
   public static ModuleSettingSlider a;
   public static ModuleSettingTick b;

   public FallSpeed() {
      super("FallSpeed", Module.category.player, 0);
      this.registerSetting(dc = new ModuleDesc("Vanilla max: 3.92"));
      this.registerSetting(a = new ModuleSettingSlider("Motion", 5.0D, 0.0D, 8.0D, 0.1D));
      this.registerSetting(b = new ModuleSettingTick("Disable XZ motion", true));
   }

   public void update() {
      if ((double)mc.player.fallDistance >= 2.5D) {
         Module fly = ModuleManager.getModuleByClazz(Fly.class);
         Module noFall = ModuleManager.getModuleByClazz(NoFall.class);

         if (
              (fly != null && fly.isEnabled()) ||
              (noFall != null && noFall.isEnabled())
         ) {
            return;
         }

         if (mc.player.capabilities.isCreativeMode || mc.player.capabilities.isFlying) {
            return;
         }

         if (mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava()) {
            return;
         }

         mc.player.motionY = -a.getInput();
         if (b.isToggled()) {
            mc.player.motionX = mc.player.motionZ = 0.0D;
         }
      }

   }
}
