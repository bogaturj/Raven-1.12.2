package keystrokesmod.sToNkS.module.modules.movement;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;

public class VClip extends Module {
   public static ModuleSettingSlider a;

   public VClip() {
      super("VClip", Module.category.movement, 0);
      this.registerSetting(a = new ModuleSettingSlider("Distace", 2.0D, -10.0D, 10.0D, 0.5D));
   }

   public void onEnable() {
      if (a.getInput() != 0.0D) {
         mc.player.setPosition(mc.player.posX, mc.player.posY + a.getInput(), mc.player.posZ);
      }

      this.disable();
   }
}
