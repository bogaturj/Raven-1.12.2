package keystrokesmod.sToNkS.module.modules.combat;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleManager;
import keystrokesmod.sToNkS.module.ModuleSettingDoubleSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.MouseEvent;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.List;

public class Reach extends Module {
   public static ModuleSettingDoubleSlider reach;
   public static ModuleSettingTick weapon_only;
   public static ModuleSettingTick moving_only;
   public static ModuleSettingTick sprint_only;
   public static ModuleSettingTick hit_through_blocks;
   public Reach() {
      super("Reach", Module.category.combat, 0);
      this.registerSetting(reach = new ModuleSettingDoubleSlider("Reach (Blocks)", 3.1, 3.3, 3, 6, 0.05));
      this.registerSetting(weapon_only = new ModuleSettingTick("Weapon only", false));
      this.registerSetting(moving_only = new ModuleSettingTick("Moving only", false));
      this.registerSetting(sprint_only = new ModuleSettingTick("Sprint only", false));
      this.registerSetting(hit_through_blocks = new ModuleSettingTick("Hit through blocks", false));
   }

   @FMLEvent
   public void onMouse(MouseEvent ev) {
      // legit event
      if(!Utils.Player.isPlayerInGame()) return;
      Module autoClicker = ModuleManager.getModuleByClazz(AutoClicker.class);
      if (autoClicker != null && autoClicker.isEnabled() && AutoClicker.leftClick.isToggled() && Mouse.isButtonDown(0)) return;
      if (ev.getButton() >= 0 && ev.isButtonstate()) {
         call();
      }
   }

   @FMLEvent
   public void onRenderTick(TickEvent.RenderTickEvent ev) {
      // autoclick event
      if(!Utils.Player.isPlayerInGame()) return;
      Module autoClicker = ModuleManager.getModuleByClazz(AutoClicker.class);
      if (autoClicker == null || !autoClicker.isEnabled() || !AutoClicker.leftClick.isToggled()) return;

      if (autoClicker.isEnabled() && AutoClicker.leftClick.isToggled() && Mouse.isButtonDown(0)){
         call();
      }
   }

   public static boolean call() {
      if (!Utils.Player.isPlayerInGame()) return false;
      if (weapon_only.isToggled() && !Utils.Player.isPlayerHoldingWeapon()) return false;
      if (moving_only.isToggled() && (double)mc.player.moveForward == 0.0D && (double)mc.player.moveStrafing == 0.0D) return false;
      if (sprint_only.isToggled() && !mc.player.isSprinting()) return false;
      if (!hit_through_blocks.isToggled() && mc.objectMouseOver != null) {
         BlockPos p = mc.objectMouseOver.getBlockPos();
         if (p != null && mc.world.getBlockState(p).getBlock() != Blocks.AIR) {
            return false;
         }
      }

      double r = Utils.Client.ranModuleVal(reach, Utils.Java.rand());
      Object[] o = zz(r, 0.0D);
      if (o == null) {
         return false;
      } else {
         Entity en = (Entity)o[0];
         mc.objectMouseOver = new RayTraceResult(en, (Vec3d)o[1]);
         mc.pointedEntity = en;
         return true;
      }
   }

   private static Object[] zz(double zzD, double zzE) {
      Module reach = ModuleManager.getModuleByClazz(Reach.class);
      if (reach != null && !reach.isEnabled()) {
         zzD = mc.playerController.extendedReach() ? 6.0D : 3.0D;
      }

      Entity entity1 = mc.getRenderViewEntity();
      Entity entity = null;
      if (entity1 == null) {
         return null;
      } else {
         mc.mcProfiler.startSection("pick");
         Vec3d eyes_positions = entity1.getPositionEyes(1.0F);
         Vec3d look = entity1.getLook(1.0F);
         Vec3d new_eyes_pos = eyes_positions.addVector(look.x * zzD, look.y * zzD, look.z * zzD);
         Vec3d zz6 = null;
         List<Entity> zz8 = mc.world.getEntitiesWithinAABBExcludingEntity(entity1, entity1.getEntityBoundingBox().expand(look.x * zzD, look.y * zzD, look.z * zzD).expand(1.0D, 1.0D, 1.0D));
         double zz9 = zzD;

         for (Entity o : zz8) {
            if (o.canBeCollidedWith()) {
               float ex = (float) ((double) o.getCollisionBorderSize() * HitBox.exp(o));
               AxisAlignedBB zz13 = o.getEntityBoundingBox().expand(ex, ex, ex);
               zz13 = zz13.expand(zzE, zzE, zzE);
               RayTraceResult zz14 = zz13.calculateIntercept(eyes_positions, new_eyes_pos);
               if (zz13.contains(eyes_positions)) {
                  if (0.0D < zz9 || zz9 == 0.0D) {
                     entity = o;
                     zz6 = zz14 == null ? eyes_positions : zz14.hitVec;
                     zz9 = 0.0D;
                  }
               } else if (zz14 != null) {
                  double zz15 = eyes_positions.distanceTo(zz14.hitVec);
                  if (zz15 < zz9 || zz9 == 0.0D) {
                     if (o == entity1.getRidingEntity()) {
                        if (zz9 == 0.0D) {
                           entity = o;
                           zz6 = zz14.hitVec;
                        }
                     } else {
                        entity = o;
                        zz6 = zz14.hitVec;
                        zz9 = zz15;
                     }
                  }
               }
            }
         }

         if (zz9 < zzD && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
         }

         mc.mcProfiler.endSection();
         if (entity != null && zz6 != null) {
            return new Object[]{entity, zz6};
         } else {
            return null;
         }
      }
   }
}
