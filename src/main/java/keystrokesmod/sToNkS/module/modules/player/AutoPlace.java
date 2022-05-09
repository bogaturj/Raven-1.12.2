package keystrokesmod.sToNkS.module.modules.player;

import keystrokesmod.sToNkS.module.*;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import org.lwjgl.input.Mouse;

public class AutoPlace {
   /*
   public static ModuleDesc ds;
   public static ModuleSettingTick a;
   public static ModuleSettingTick b;
   public static ModuleSettingSlider c;
   private double lfd = 0.0D;
   private final int d = 25;
   private long l = 0L;
   private int f = 0;
   private RayTraceResult lm = null;
   private BlockPos lp = null;

   public AutoPlace() {
      super("AutoPlace", Module.category.player, 0);
      this.registerSetting(ds = new ModuleDesc("FD: FPS/80"));
      this.registerSetting(c = new ModuleSettingSlider("Frame delay", 8.0D, 0.0D, 30.0D, 1.0D));
      this.registerSetting(a = new ModuleSettingTick("Hold right", true));
   }

   public void guiUpdate() {
      if (this.lfd != c.getInput()) {
         this.rv();
      }

      this.lfd = c.getInput();
   }

   public void onDisable() {
      if (a.isToggled()) {
         this.rd(4);
      }

      this.rv();
   }

   public void update() {
      Module fastPlace = ModuleManager.getModuleByClazz(FastPlace.class);
      if (a.isToggled() && Mouse.isButtonDown(1) && !mc.player.capabilities.isFlying && fastPlace != null && !fastPlace.isEnabled()) {
         ItemStack i = mc.player.getHeldItem(EnumHand.MAIN_HAND);
         if (i == null || !(i.getItem() instanceof ItemBlock)) {
            return;
         }

         this.rd(mc.player.motionY > 0.0D ? 1 : 1000);
      }

   }

   @FMLEvent
   public void bh(DrawBlockHighlightEvent ev) {
      if (Utils.Player.isPlayerInGame()) {
         if (mc.currentScreen == null && !mc.player.capabilities.isFlying) {
            ItemStack i = mc.player.getHeldItem(EnumHand.MAIN_HAND);
            if (i != null && i.getItem() instanceof ItemBlock) {
               RayTraceResult m = mc.objectMouseOver;
               if (m != null && m.typeOfHit == RayTraceResult.Type.BLOCK && m.sideHit != EnumFacing.UP && m.sideHit != EnumFacing.DOWN) {
                  if (this.lm != null && (double)this.f < c.getInput()) {
                     ++this.f;
                  } else {
                     this.lm = m;
                     BlockPos pos = m.getBlockPos();
                     if (this.lp == null || pos.getX() != this.lp.getX() || pos.getY() != this.lp.getY() || pos.getZ() != this.lp.getZ()) {
                        Block b = mc.world.getBlockState(pos).getBlock();
                        if (b != null && b != Blocks.AIR && !(b instanceof BlockLiquid)) {
                           if (!a.isToggled() || Mouse.isButtonDown(1)) {
                              long n = System.currentTimeMillis();
                              if (n - this.l >= 25L) {
                                 this.l = n;
                                 if (mc.playerController.processRightClickBlock(mc.player, mc.world, i, pos, m.sideHit, m.hitVec)) {
                                    Utils.Client.setMouseButtonState(1, true);
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                    mc.getItemRenderer().resetEquippedProgress(EnumHand.MAIN_HAND);
                                    Utils.Client.setMouseButtonState(1, false);
                                    this.lp = pos;
                                    this.f = 0;
                                 }

                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void rd(int i) {
      try {
         if (FastPlace.rightClickDelayTimerField != null) {
            FastPlace.rightClickDelayTimerField.set(mc, i);
         }
      } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {}
   }

   private void rv() {
      this.lp = null;
      this.lm = null;
      this.f = 0;
   }*/
}
