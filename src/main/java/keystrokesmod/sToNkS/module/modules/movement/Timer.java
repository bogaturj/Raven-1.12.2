package keystrokesmod.sToNkS.module.modules.movement;

import keystrokesmod.sToNkS.clickgui.raven.ClickGui;
import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;

public class Timer extends Module {
   public static ModuleSettingSlider a;
   public static ModuleSettingTick b;

   public Timer() {
      super("Timer", Module.category.movement, 0);
      a = new ModuleSettingSlider("Speed", 20D, 1D, 100, 0.01D);
      b = new ModuleSettingTick("Strafe only", false);
      this.registerSetting(a);
      this.registerSetting(b);
   }

   public void update() {
      if (!(mc.currentScreen instanceof ClickGui)) {
         if (b.isToggled() && mc.player.moveStrafing == 0.0F) {
            Utils.Client.resetTimer();
            return;
         }

         Utils.Client.setTimerSpeed((float)a.getInput());
      } else {
         Utils.Client.resetTimer();
      }

   }

   public void onDisable() {
      Utils.Client.resetTimer();
   }
}
