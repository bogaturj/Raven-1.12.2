package keystrokesmod.sToNkS.module.modules.combat;

import keystrokesmod.sToNkS.main.Ravenbplus;
import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleDesc;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BurstClicker extends Module {
   public static ModuleDesc artificialDragClicking;
   public static ModuleSettingSlider clicks;
   public static ModuleSettingSlider delay;
   public static ModuleSettingTick delayRandomizer;
   public static ModuleSettingTick placeWhenBlock;
   private boolean l_c = false;
   private boolean l_r = false;
   private Method rightClickMouse = null;

   public BurstClicker() {
      super("BurstClicker", Module.category.combat, 0);
      this.registerSetting(artificialDragClicking = new ModuleDesc("Artificial dragclicking."));
      this.registerSetting(clicks = new ModuleSettingSlider("Clicks", 0.0D, 0.0D, 50.0D, 1.0D));
      this.registerSetting(delay = new ModuleSettingSlider("Delay (ms)", 5.0D, 1.0D, 40.0D, 1.0D));
      this.registerSetting(delayRandomizer = new ModuleSettingTick("Delay randomizer", true));
      this.registerSetting(placeWhenBlock = new ModuleSettingTick("Place when block", false));
      try {
         try {
            this.rightClickMouse = mc.getClass().getDeclaredMethod("func_147121_ag");
         } catch (NoSuchMethodException var4) {
            try {
               this.rightClickMouse = mc.getClass().getDeclaredMethod("rightClickMouse");
            } catch (NoSuchMethodException var3) {
            }
         }
      } catch(NoClassDefFoundError varbruh){
         varbruh.printStackTrace();
      }

      if (this.rightClickMouse != null) {
         this.rightClickMouse.setAccessible(true);
      }

   }

   public void onEnable() {
      if (clicks.getInput() != 0.0D && mc.currentScreen == null && mc.inGameHasFocus) {
         Ravenbplus.getExecutor().execute(() -> {
            try {
               int cl = (int) clicks.getInput();
               int del = (int) delay.getInput();

               for(int i = 0; i < cl * 2 && this.isEnabled() && Utils.Player.isPlayerInGame() && mc.currentScreen == null && mc.inGameHasFocus; ++i) {
                  if (i % 2 == 0) {
                     this.l_c = true;
                     if (del != 0) {
                        int realDel = del;
                        if (delayRandomizer.isToggled()) {
                           realDel = del + Utils.Java.rand().nextInt(25) * (Utils.Java.rand().nextBoolean() ? -1 : 1);
                           if (realDel <= 0) {
                              realDel = del / 3 - realDel;
                           }
                        }

                        Thread.sleep(realDel);
                     }
                  } else {
                     this.l_r = true;
                  }
               }

               this.disable();
            } catch (InterruptedException var5) {
            }

         });
      } else {
         this.disable();
      }
   }

   public void onDisable() {
      this.l_c = false;
      this.l_r = false;
   }

   @FMLEvent
   public void r(RenderTickEvent ev) {
      if (Utils.Player.isPlayerInGame()) {
         if (this.l_c) {
            this.c(true);
            this.l_c = false;
         } else if (this.l_r) {
            this.c(false);
            this.l_r = false;
         }
      }

   }

   private void c(boolean st) {
      boolean r = placeWhenBlock.isToggled() && mc.player.getHeldItem(EnumHand.MAIN_HAND) != null && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock;
      if (r) {
         try {
            this.rightClickMouse.invoke(mc);
         } catch (IllegalAccessException | InvocationTargetException var4) {
         }
      } else {
         int key = mc.gameSettings.keyBindAttack.getKeyCode();
         KeyBinding.setKeyBindState(key, st);
         if (st) {
            KeyBinding.onTick(key);
         }
      }

      Utils.Client.setMouseButtonState(r ? 1 : 0, st);
   }
}
