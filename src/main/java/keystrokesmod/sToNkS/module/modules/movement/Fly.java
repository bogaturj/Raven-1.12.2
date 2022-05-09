package keystrokesmod.sToNkS.module.modules.movement;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleDesc;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.utils.Utils;

public class Fly extends Module {
   private final Fly.VanFly vanFly = new VanFly();
   private final Fly.GliFly gliFly = new Fly.GliFly();
   public static ModuleDesc dc;
   public static ModuleSettingSlider a;
   public static ModuleSettingSlider b;
   private static final String c1 = "Vanilla";
   private static final String c2 = "Glide";

   public Fly() {
      super("Fly", Module.category.movement, 0);
      this.registerSetting(a = new ModuleSettingSlider("Value", 1.0D, 1.0D, 2.0D, 1.0D));
      this.registerSetting(dc = new ModuleDesc(Utils.md + c1));
      this.registerSetting(b = new ModuleSettingSlider("Speed", 2.0D, 1.0D, 5.0D, 0.1D));
   }

   public void onEnable() {
      switch((int)a.getInput()) {
      case 1:
         this.vanFly.onEnable();
         break;
      case 2:
         this.gliFly.onEnable();
      }

   }

   public void onDisable() {
      switch((int)a.getInput()) {
      case 1:
         this.vanFly.onDisable();
         break;
      case 2:
         this.gliFly.onDisable();
      }

   }

   public void update() {
      switch((int)a.getInput()) {
      case 1:
         this.vanFly.update();
         break;
      case 2:
         this.gliFly.update();
      }

   }

   public void guiUpdate() {
      switch((int)a.getInput()) {
      case 1:
         dc.setDesc(Utils.md + c1);
         break;
      case 2:
         dc.setDesc(Utils.md + c2);
      }

   }

   class GliFly {
      boolean opf = false;

      public void onEnable() {}

      public void onDisable() {
         this.opf = false;
      }

      public void update() {
         if (Module.mc.player.movementInput.moveForward > 0.0F) {
            if (!this.opf) {
               this.opf = true;
               if (Module.mc.player.onGround) {
                  Module.mc.player.jump();
               }
            } else {
               if (Module.mc.player.onGround || Module.mc.player.collidedHorizontally) {
                  Fly.this.disable();
                  return;
               }

               double s = 1.94D * Fly.b.getInput();
               double r = Math.toRadians(Module.mc.player.rotationYaw + 90.0F);
               Module.mc.player.motionX = s * Math.cos(r);
               Module.mc.player.motionZ = s * Math.sin(r);
            }
         }

      }
   }

   static class VanFly {
      private final float dfs = 0.05F;

      public void onEnable() {}

      public void onDisable() {
         if (Module.mc.player.capabilities.isFlying) {
            Module.mc.player.capabilities.isFlying = false;
         }

         Module.mc.player.capabilities.setFlySpeed(0.05F);
      }

      public void update() {
         Module.mc.player.motionY = 0.0D;
         Module.mc.player.capabilities.setFlySpeed((float)(0.05000000074505806D * Fly.b.getInput()));
         Module.mc.player.capabilities.isFlying = true;
      }
   }
}
