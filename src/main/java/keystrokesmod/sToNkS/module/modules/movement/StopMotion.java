package keystrokesmod.sToNkS.module.modules.movement;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingTick;

public class StopMotion extends Module {
   public static ModuleSettingTick a;
   public static ModuleSettingTick b;
   public static ModuleSettingTick c;

   public StopMotion() {
      super("Stop Motion", Module.category.movement, 0);
      this.registerSetting(a = new ModuleSettingTick("Stop X", true));
      this.registerSetting(b = new ModuleSettingTick("Stop Y", true));
      this.registerSetting(c = new ModuleSettingTick("Stop Z", true));
   }

   public void onEnable() {
       //System.out.println("No fuck you");

      this.disable();
   }
}
