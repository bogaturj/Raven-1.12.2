package keystrokesmod.sToNkS.module.modules.movement;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleDesc;
import keystrokesmod.sToNkS.module.ModuleManager;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.utils.Utils;

public class Boost extends Module {
   public static ModuleDesc c;
   public static ModuleSettingSlider a;
   public static ModuleSettingSlider b;
   private int i = 0;
   private boolean t = false;

   public Boost() {
      super("Boost", Module.category.movement, 0);
      this.registerSetting(c = new ModuleDesc("20 ticks are in 1 second"));
      this.registerSetting(a = new ModuleSettingSlider("Multiplier", 20D, 1.0D, 100D, 0.05D));
      this.registerSetting(b = new ModuleSettingSlider("Time (ticks)", 15.0D, 1.0D, 80.0D, 1.0D));
   }

   public void onEnable() {
      Module timer = ModuleManager.getModuleByClazz(Timer.class);
      if (timer != null && timer.isEnabled()) {
         this.t = true;
         timer.disable();
      }

   }

   public void onDisable() {
      this.i = 0;
      if (Utils.Client.getTimerSpeed() != 20F) {
         Utils.Client.resetTimer();
      }

      if (this.t) {
         Module timer = ModuleManager.getModuleByClazz(Timer.class);
         if (timer != null) timer.enable();
      }

      this.t = false;
   }

   public void update() {
      if (this.i == 0) {
         this.i = mc.player.ticksExisted;
      }

      Utils.Client.setTimerSpeed((float)a.getInput());
      if ((double)this.i == (double)mc.player.ticksExisted - b.getInput()) {
         Utils.Client.resetTimer();
         this.disable();
      }

   }
}
