package keystrokesmod.sToNkS.module.modules.player;

import keystrokesmod.sToNkS.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {
   public NoFall() {
      super("NoFall", Module.category.player, 0);
   }

   public void update() {
      if ((double)mc.player.fallDistance > 2.5D) {
         mc.player.connection.sendPacket(new CPacketPlayer(true));
      }

   }
}
