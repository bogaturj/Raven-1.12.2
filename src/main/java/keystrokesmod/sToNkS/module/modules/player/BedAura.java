package keystrokesmod.sToNkS.module.modules.player;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.TimerTask;

public class BedAura extends Module {
   public static ModuleSettingSlider r;
   private java.util.Timer t;
   private BlockPos m = null;
   private final long per = 600L;

   public BedAura() {
      super("BedAura", Module.category.player, 0);
      this.registerSetting(r = new ModuleSettingSlider("Range", 5.0D, 2.0D, 10.0D, 1.0D));
   }

   public void onEnable() {
      (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 600L);
   }

   public void onDisable() {
      if (this.t != null) {
         this.t.cancel();
         this.t.purge();
         this.t = null;
      }

      this.m = null;
   }

   public TimerTask t() {
      return new TimerTask() {
         public void run() {
            int ra = (int)BedAura.r.getInput();

            for(int y = ra; y >= -ra; --y) {
               for(int x = -ra; x <= ra; ++x) {
                  for(int z = -ra; z <= ra; ++z) {
                     if (Utils.Player.isPlayerInGame()) {
                        BlockPos p = new BlockPos(Module.mc.player.posX + (double) x, Module.mc.player.posY + (double) y, Module.mc.player.posZ + (double) z);
                        boolean bed = Module.mc.world.getBlockState(p).getBlock() == Blocks.BED;
                        if (BedAura.this.m == p) {
                           if (!bed) {
                              BedAura.this.m = null;
                           }
                        } else if (bed) {
                           BedAura.this.mi(p);
                           BedAura.this.m = p;
                           break;
                        }
                     }
                  }
               }
            }

         }
      };
   }

   private void mi(BlockPos p) {
      mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, p, EnumFacing.NORTH));
      mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, p, EnumFacing.NORTH));
   }
}
