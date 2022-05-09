package keystrokesmod.sToNkS.module.modules;

import keystrokesmod.sToNkS.main.Ravenbplus;
import keystrokesmod.sToNkS.module.*;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static keystrokesmod.sToNkS.main.Ravenbplus.mResourceLocation;

public class HUD extends Module {
   public static ModuleSettingTick editPosition;
   public static ModuleSettingTick dropShadow;
   public static ModuleSettingTick logo;
   public static ModuleSettingTick alphabeticalSort;
   public static ModuleSettingSlider colourMode;
   public static ModuleDesc colourModeDesc;
   private static int hudX = 5;
   private static int hudY = 70;
   public static Utils.HUD.PositionMode positionMode;
   public static int logoSize;
   public static boolean showedError;
   public static final String HUDX_prefix = "HUDX~ ";
   public static final String HUDY_prefix = "HUDY~ ";


   public HUD() {
      super("HUD", Module.category.render, 0);
      this.registerSetting(editPosition = new ModuleSettingTick("Edit position", false));
      this.registerSetting(dropShadow = new ModuleSettingTick("Drop shadow", true));
      this.registerSetting(logo = new ModuleSettingTick("Logo", false));
      this.registerSetting(alphabeticalSort = new ModuleSettingTick("Alphabetical sort", false));
      this.registerSetting(colourMode = new ModuleSettingSlider("Value: ", 1, 1, 5, 1));
      this.registerSetting(colourModeDesc = new ModuleDesc("Mode: RAVEN"));
      logoSize = 64;
      showedError = false;
   }

   public void guiUpdate(){
      colourModeDesc.setDesc(Utils.md + ColourModes.values()[(int) colourMode.getInput()-1]);
   }

   public void onEnable() {
      ModuleManager.sort();
   }

   public void guiButtonToggled(ModuleSettingTick b) {
      if (b == editPosition) {
         editPosition.disable();
         mc.displayGuiScreen(new EditHudPositionScreen());
      } else if (b == alphabeticalSort) {
         ModuleManager.sort();
      }
   }

   @FMLEvent
   public void L(RenderGameOverlayEvent e){
      if(e.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {

      }
   }

   @FMLEvent
   public void a(RenderTickEvent ev) {
      // IK THIS METHOD IS INNEFECTIVE I WILL OPTIMISE IT LATER DONT BULLY ME

      if (ev.phase == Phase.END && Utils.Player.isPlayerInGame()) {
         if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
            return;
         }

         int margin = 2;
         int y = hudY;
         int del = 0;

         if(mResourceLocation == null && !showedError) {
            ////System.out.println("Showed error");
            Utils.Player.sendMessageToSelf("Error! Failed to load the raven B+ logo. Please report this in our discord!");
            showedError = true;
         }

         if (mResourceLocation != null && logo.isToggled()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(mResourceLocation);
            Gui.drawModalRectWithCustomSizedTexture(hudX - 13, hudY, 0, 0, logoSize, logoSize, logoSize, logoSize);
         }
         if (!alphabeticalSort.isToggled()){
            if (positionMode == Utils.HUD.PositionMode.UPLEFT || positionMode == Utils.HUD.PositionMode.UPRIGHT) {
               ModuleManager.sortShortLong();
            }
            else if(positionMode == Utils.HUD.PositionMode.DOWNLEFT || positionMode == Utils.HUD.PositionMode.DOWNRIGHT) {
               ModuleManager.sortLongShort();
            }
         }
         List<Module> en = new ArrayList<>(Ravenbplus.moduleManager.listofmods());
         if(en.isEmpty()) return;

         int textBoxWidth = ModuleManager.getLongestActiveModule(mc.fontRenderer);
         int textBoxHeight = ModuleManager.getBoxHeight(mc.fontRenderer, margin);
         //////System.out.println(mc.displayWidth + " " + mc.displayHeight + " || " + hudX + " " + hudY);
         if(logo.isToggled() && mResourceLocation != null) {
            y += (logoSize * 0.8);
         }

         if(hudX < 0) {
            hudX = margin;
         }
         if(hudY < 0) {
            if(logo.isToggled() && mResourceLocation != null) {
               hudY = logoSize - (int)(logoSize * 0.2);
            } else {
               hudY = margin;
            }
         }

         if(hudX + textBoxWidth > mc.displayWidth/2){
            hudX = mc.displayWidth/2 - textBoxWidth - margin;
         }

         if(hudY + textBoxHeight > mc.displayHeight/2){
            hudY = mc.displayHeight/2 - textBoxHeight;
         }

         for (Module m : en) {
            if (m.isEnabled() && m != this) {
               if (HUD.positionMode == Utils.HUD.PositionMode.DOWNRIGHT || HUD.positionMode == Utils.HUD.PositionMode.UPRIGHT) {
                  if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.RAVEN) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRenderer.getStringWidth(m.getName())), (float) y, Utils.Client.rainbowDraw(2L, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 120;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.RAVEN2) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRenderer.getStringWidth(m.getName())), (float) y, Utils.Client.rainbowDraw(2L, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 10;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.ASTOLFO) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRenderer.getStringWidth(m.getName())), (float) y, Utils.Client.astolfoColorsDraw(10, 14), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 120;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.ASTOLFO2) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRenderer.getStringWidth(m.getName())), (float) y, Utils.Client.astolfoColorsDraw(10, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 120;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.ASTOLFO3) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRenderer.getStringWidth(m.getName())), (float) y, Utils.Client.astolfoColorsDraw(10, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 10;
                  }
               } else {
                  if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.RAVEN) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX, (float) y, Utils.Client.rainbowDraw(2L, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 120;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.RAVEN2) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX, (float) y, Utils.Client.rainbowDraw(2L, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 10;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.ASTOLFO) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX, (float) y, Utils.Client.astolfoColorsDraw(10, 14), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 120;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.ASTOLFO2) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX, (float) y, Utils.Client.astolfoColorsDraw(10, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 120;
                  } else if (ColourModes.values()[(int) colourMode.getInput() - 1] == ColourModes.ASTOLFO3) {
                     mc.fontRenderer.drawString(m.getName(), (float) hudX, (float) y, Utils.Client.astolfoColorsDraw(10, del), dropShadow.isToggled());
                     y += mc.fontRenderer.FONT_HEIGHT + margin;
                     del -= 10;
                  }
               }
            }
         }
      }

   }

   static class EditHudPositionScreen extends GuiScreen {
      final String hudTextExample = "This is an-Example-HUD";
      GuiButtonExt resetPosButton;
      boolean mouseDown = false;
      int textBoxStartX = 0;
      int textBoxStartY = 0;
      ScaledResolution sr;
      int textBoxEndX = 0;
      int textBoxEndY = 0;
      int marginX = 5;
      int marginY = 70;
      int lastMousePosX = 0;
      int lastMousePosY = 0;
      int sessionMousePosX = 0;
      int sessionMousePosY = 0;

      public void initGui() {
         super.initGui();
         this.buttonList.add(this.resetPosButton = new GuiButtonExt(1, this.width - 90, 5, 85, 20, "Reset position"));
         this.marginX = HUD.hudX;
         this.marginY = HUD.hudY;
         sr = new ScaledResolution(mc);
         HUD.positionMode = Utils.HUD.getPostitionMode(marginX, marginY, sr.getScaledWidth(), sr.getScaledHeight());
      }

      public void drawScreen(int mX, int mY, float pt) {
         drawRect(0, 0, this.width, this.height, -1308622848);
         drawRect(0, this.height /2, this.width, this.height /2 + 1, 0x9936393f);
         drawRect(this.width /2, 0, this.width /2 + 1, this.height, 0x9936393f);
         int textBoxStartX = this.marginX;
         int textBoxStartY = this.marginY;
         int textBoxEndX = textBoxStartX + 50;
         int textBoxEndY = textBoxStartY + 32;
         this.drawArrayList(this.mc.fontRenderer, this.hudTextExample);
         this.textBoxStartX = textBoxStartX;
         this.textBoxStartY = textBoxStartY;
         this.textBoxEndX = textBoxEndX;
         this.textBoxEndY = textBoxEndY;
         HUD.hudX = textBoxStartX;
         HUD.hudY = textBoxStartY;
         ScaledResolution res = new ScaledResolution(this.mc);
         int descriptionOffsetX = res.getScaledWidth() / 2 - 84;
         int descriptionOffsetY = res.getScaledHeight() / 2 - 20;
         Utils.HUD.drawColouredText("Edit the HUD position by dragging.", '-', descriptionOffsetX, descriptionOffsetY, 2L, 0L, true, this.mc.fontRenderer);

         try {
            this.handleInput();
         } catch (IOException var12) {
         }

         super.drawScreen(mX, mY, pt);
      }

      private void drawArrayList(FontRenderer fr, String t) {
         int x = this.textBoxStartX;
         int gap = this.textBoxEndX - this.textBoxStartX;
         int y = this.textBoxStartY;
         double marginY = fr.FONT_HEIGHT + 2;
         String[] var4 = t.split("-");
         ArrayList<String> var5 = Utils.Java.toArrayList(var4);
         if (HUD.positionMode == Utils.HUD.PositionMode.UPLEFT || HUD.positionMode == Utils.HUD.PositionMode.UPRIGHT) {
            var5.sort((o1, o2) -> Utils.mc.fontRenderer.getStringWidth(o2) - Utils.mc.fontRenderer.getStringWidth(o1));
         }
         else if(HUD.positionMode == Utils.HUD.PositionMode.DOWNLEFT || HUD.positionMode == Utils.HUD.PositionMode.DOWNRIGHT) {
            var5.sort(Comparator.comparingInt(o2 -> Utils.mc.fontRenderer.getStringWidth(o2)));
         }

         if(HUD.positionMode == Utils.HUD.PositionMode.DOWNRIGHT || HUD.positionMode == Utils.HUD.PositionMode.UPRIGHT){
            for (String s : var5) {
               fr.drawString(s, (float) x + (gap - fr.getStringWidth(s)), (float) y, Color.white.getRGB(), HUD.dropShadow.isToggled());
               y += marginY;
            }
         } else {
            for (String s : var5) {
               fr.drawString(s, (float) x, (float) y, Color.white.getRGB(), HUD.dropShadow.isToggled());
               y += marginY;
            }
         }
      }

      protected void mouseClickMove(int mousePosX, int mousePosY, int clickedMouseButton, long timeSinceLastClick) {
         super.mouseClickMove(mousePosX, mousePosY, clickedMouseButton, timeSinceLastClick);
         if (clickedMouseButton == 0) {
            if (this.mouseDown) {
               this.marginX = this.lastMousePosX + (mousePosX - this.sessionMousePosX);
               this.marginY = this.lastMousePosY + (mousePosY - this.sessionMousePosY);
               sr = new ScaledResolution(mc);
               HUD.positionMode = Utils.HUD.getPostitionMode(marginX, marginY,sr.getScaledWidth(), sr.getScaledHeight());

               //in the else if statement, we check if the mouse is clicked AND inside the "text box"
            } else if (mousePosX > this.textBoxStartX && mousePosX < this.textBoxEndX && mousePosY > this.textBoxStartY && mousePosY < this.textBoxEndY) {
               this.mouseDown = true;
               this.sessionMousePosX = mousePosX;
               this.sessionMousePosY = mousePosY;
               this.lastMousePosX = this.marginX;
               this.lastMousePosY = this.marginY;
            }

         }
      }

      protected void mouseReleased(int mX, int mY, int state) {
         super.mouseReleased(mX, mY, state);
         if (state == 0) {
            this.mouseDown = false;
         }

      }

      public void actionPerformed(GuiButton b) {
         if (b == this.resetPosButton) {
            this.marginX = HUD.hudX = 5;
            this.marginY = HUD.hudY = 70;
         }

      }

      public boolean doesGuiPauseGame() {
         return false;
      }
   }

   public enum ColourModes {
      RAVEN,
      RAVEN2,
      ASTOLFO,
      ASTOLFO2,
      ASTOLFO3,
      KOPAMED
   }

   public static int getHudX() {
      return hudX;
   }

   public static int getHudY() {
      return hudY;
   }

   public static void setHudX(int hudX) {
      HUD.hudX = hudX;
   }

   public static void setHudY(int hudY) {
      HUD.hudY = hudY;
   }
}
