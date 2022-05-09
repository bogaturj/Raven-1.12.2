package keystrokesmod.sToNkS.module.modules.render;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;

public class TargetHUD extends Module {
    public static ModuleSettingTick editPosition;
    public static int height, width;
    public static FontRenderer fr;
    ScaledResolution sr;
    public TargetHUD() {
        super("Target HUD", category.render, 0);
        sr = new ScaledResolution(Minecraft.getMinecraft());
        height = sr.getScaledHeight();
        width = sr.getScaledWidth();
        fr = mc.fontRenderer;
    }

    @FMLEvent
    public void r(RenderGameOverlayEvent ev) {
        if (ev.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS || !Utils.Player.isPlayerInGame()) return;
        ////System.out.println("render");
        if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
            return;
        }
        height = sr.getScaledHeight();
        width = sr.getScaledWidth();
        ////System.out.println("render");
        ////System.out.println("left " + (int)(width * 0.65) + " || top " + (int)(height * 0.65) + " || right " + (int)(width * 0.75)  + " || bottom " + (int)(height * 0.65));
        Gui.drawRect(width - 10, 0,width, height, 0x90000000);
        Gui.drawRect((int)(width * 0.65), (int)(height * 0.65), (int)(width * 0.75), (int)(height * 0.75),0xffff4500);
    }
}
