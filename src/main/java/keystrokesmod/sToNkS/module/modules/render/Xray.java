package keystrokesmod.sToNkS.module.modules.render;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Xray extends Module {
   public static ModuleSettingSlider r;
   public static ModuleSettingTick a;
   public static ModuleSettingTick b;
   public static ModuleSettingTick c;
   public static ModuleSettingTick d;
   public static ModuleSettingTick e;
   public static ModuleSettingTick f;
   public static ModuleSettingTick g;
   public static ModuleSettingTick h;
   private java.util.Timer t;
   private List<BlockPos> ren;
   private final long per = 200L;

   public Xray() {
      super("Xray", Module.category.render, 0);
      this.registerSetting(r = new ModuleSettingSlider("Range", 20.0D, 5.0D, 50.0D, 1.0D));
      this.registerSetting(a = new ModuleSettingTick("Iron", true));
      this.registerSetting(b = new ModuleSettingTick("Gold", true));
      this.registerSetting(c = new ModuleSettingTick("Diamond", true));
      this.registerSetting(d = new ModuleSettingTick("Emerald", true));
      this.registerSetting(e = new ModuleSettingTick("Lapis", true));
      this.registerSetting(f = new ModuleSettingTick("Redstone", true));
      this.registerSetting(g = new ModuleSettingTick("Coal", true));
      this.registerSetting(h = new ModuleSettingTick("Spawner", true));
   }

   public void onEnable() {
      this.ren = new ArrayList<>();
      (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 200L);
   }

   public void onDisable() {
      if (this.t != null) {
         this.t.cancel();
         this.t.purge();
         this.t = null;
      }

   }

   private TimerTask t() {
      return new TimerTask() {
         public void run() {
            Xray.this.ren.clear();
            int ra = (int)Xray.r.getInput();

            for(int y = ra; y >= -ra; --y) {
               for(int x = -ra; x <= ra; ++x) {
                  for(int z = -ra; z <= ra; ++z) {
                     if (Utils.Player.isPlayerInGame()) {
                        BlockPos p = new BlockPos(Module.mc.player.posX + (double)x, Module.mc.player.posY + (double)y, Module.mc.player.posZ + (double)z);
                        Block bl = Module.mc.world.getBlockState(p).getBlock();
                        if (Xray.a.isToggled() && bl.equals(Blocks.IRON_ORE) || Xray.b.isToggled() && bl.equals(Blocks.GOLD_ORE) || Xray.c.isToggled() && bl.equals(Blocks.DIAMOND_ORE) || Xray.d.isToggled() && bl.equals(Blocks.EMERALD_ORE) || Xray.e.isToggled() && bl.equals(Blocks.LAPIS_ORE) || Xray.f.isToggled() && bl.equals(Blocks.REDSTONE_ORE) || Xray.g.isToggled() && bl.equals(Blocks.COAL_ORE) || Xray.h.isToggled() && bl.equals(Blocks.MOB_SPAWNER)) {
                           Xray.this.ren.add(p);
                        }
                     }
                  }
               }
            }

         }
      };
   }

   @FMLEvent
   public void orl(RenderWorldLastEvent ev) {
      if (Utils.Player.isPlayerInGame() && !this.ren.isEmpty()) {
         List<BlockPos> tRen = new ArrayList<>(this.ren);

         for (BlockPos p : tRen) {
            this.dr(p);
         }
      }

   }

   private void dr(BlockPos p) {
      int[] rgb = this.c(mc.world.getBlockState(p).getBlock());
      if (rgb[0] + rgb[1] + rgb[2] != 0) {
         Utils.HUD.re(p, (new Color(rgb[0], rgb[1], rgb[2])).getRGB(), true);
      }

   }

   private int[] c(Block b) {
      int red = 0;
      int green = 0;
      int blue = 0;
      if (b.equals(Blocks.IRON_ORE)) {
         red = 255;
         green = 255;
         blue = 255;
      } else if (b.equals(Blocks.GOLD_ORE)) {
         red = 255;
         green = 255;
      } else if (b.equals(Blocks.DIAMOND_ORE)) {
         green = 220;
         blue = 255;
      } else if (b.equals(Blocks.EMERALD_ORE)) {
         red = 35;
         green = 255;
      } else if (b.equals(Blocks.LAPIS_ORE)) {
         green = 50;
         blue = 255;
      } else if (b.equals(Blocks.REDSTONE_ORE)) {
         red = 255;
      } else if (b.equals(Blocks.MOB_SPAWNER)) {
         red = 30;
         blue = 135;
      }

      return new int[]{red, green, blue};
   }
}
