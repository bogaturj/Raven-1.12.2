package keystrokesmod.sToNkS.module.modules.movement;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Sprint extends Module {
   public static ModuleSettingTick a;

   public Sprint() {
      super("Sprint", Module.category.movement, 0);
      a = new ModuleSettingTick("OmniSprint", false);
      this.registerSetting(a);
   }

   @FMLEvent
   public void p(PlayerTickEvent e) {
      if (Utils.Player.isPlayerInGame() && mc.inGameHasFocus) {
         EntityPlayerSP p = mc.player;
         if (a.isToggled()) {
            if (Utils.Player.isMoving() && p.getFoodStats().getFoodLevel() > 6) {
               p.setSprinting(true);
            }
         } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
         }
      }

   }
}
