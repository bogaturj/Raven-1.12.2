package keystrokesmod.sToNkS.module;

import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.EventManager;
import keystrokesmod.sToNkS.NotificationRenderer;
import keystrokesmod.sToNkS.main.Ravenbplus;
import keystrokesmod.sToNkS.module.modules.HUD;
import keystrokesmod.sToNkS.module.modules.other.DiscordRPCModule;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Module {
   protected ArrayList<ModuleSettingsList> settings;
   private final String moduleName;
   private final Module.category moduleCategory;
   private boolean enabled;
   private int keycode;
   protected static Minecraft mc;
   private boolean isToggled = false;

   public Module(String name, Module.category moduleCategory) {
      this.moduleName = name;
      this.moduleCategory = moduleCategory;
      this.keycode = 0;
      this.enabled = false;
      this.settings = new ArrayList<>();
   }

   public Module(String moduleName, Module.category moduleCategory, int keycode) {
      this.moduleName = moduleName;
      this.moduleCategory = moduleCategory;
      this.keycode = keycode;
      this.enabled = false;
      mc = Minecraft.getMinecraft();
      this.settings = new ArrayList<>();
   }

   public void keybind() {
      if (this.keycode != 0 && this.canBeEnabled()) {
         if (!this.isToggled && Keyboard.isKeyDown(this.keycode)) {
            this.toggle();
            this.isToggled = true;
         } else if (!Keyboard.isKeyDown(this.keycode)) {
            this.isToggled = false;
         }
      }
   }

   public boolean canBeEnabled() {
      return true;
   }

   public void enable() {
      boolean oldState = this.enabled;
      this.setToggled(true);
      if (oldState != this.enabled) {
         NotificationRenderer.moduleStateChanged(this);
      }
      ModuleManager.enModsList.add(this);

      Module hud = ModuleManager.getModuleByClazz(HUD.class);

      if (hud != null && hud.isEnabled()) {
         ModuleManager.sort();
      }

      EventManager.register(this);

      this.onEnable();
      if(Ravenbplus.configManager != null){
         Ravenbplus.configManager.save();
      }

      Module discordRPCModule = ModuleManager.getModuleByClazz(DiscordRPCModule.class);
      if (discordRPCModule != null && discordRPCModule.isEnabled()) {
         DiscordRPCModule.rpc.updateRavenRPC();
      }
   }

   public void disable() {
      boolean oldState = this.enabled;
      this.setToggled(false);
      if (oldState != this.enabled) {
         NotificationRenderer.moduleStateChanged(this);
      }
      ModuleManager.enModsList.remove(this);
      EventManager.unregister(this);
      this.onDisable();
      if(Ravenbplus.configManager != null){
         Ravenbplus.configManager.save();
      }

      Module discordRPCModule = ModuleManager.getModuleByClazz(DiscordRPCModule.class);
      if (discordRPCModule != null && discordRPCModule.isEnabled()) {
         DiscordRPCModule.rpc.updateRavenRPC();
      }
   }

   public void setToggled(boolean enabled) {
      this.enabled = enabled;
      if(Ravenbplus.configManager != null){
         Ravenbplus.configManager.save();
      }
   }

   public String getName() {
      return this.moduleName;
   }

   public ArrayList<ModuleSettingsList> getSettings() {
      return this.settings;
   }

   public ModuleSettingsList getSettingByName(String name) {
      for (ModuleSettingsList setting : this.settings) {
         if (setting.getName().equalsIgnoreCase(name))
            return setting;
      }
      return null;
   }

   public void registerSetting(ModuleSettingsList Setting) {
      this.settings.add(Setting);
   }

   public Module.category moduleCategory() {
      return this.moduleCategory;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void toggle() {
      if (this.isEnabled()) {
         this.disable();
      } else {
         this.enable();
      }
      if(Ravenbplus.configManager != null){
         Ravenbplus.configManager.save();
      }
   }

   public void update() {
   }

   public void guiUpdate() {
   }

   public void guiButtonToggled(ModuleSettingTick b) {
   }

   public int getKeycode() {
      return this.keycode;
   }

   public void setbind(int keybind) {
      this.keycode = keybind;
      if(Ravenbplus.configManager != null){
         Ravenbplus.configManager.save();
      }
   }

   public enum category {
      combat,
      movement,
      player,
      world,
      render,
      minigames,
      fun,
      other,
      client,
      hotkey,
      debug
   }
}
