package keystrokesmod.sToNkS.module.modules.combat;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingDoubleSlider;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.utils.CoolDown;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class AutoBlock extends Module {
    public static ModuleSettingDoubleSlider duration, distance;
    public static ModuleSettingSlider chance;
    private boolean engaged;
    private CoolDown engagedTime = new CoolDown(0);

    public AutoBlock(){
        super("AutoBlock", category.combat, 0);

        this.registerSetting(duration = new ModuleSettingDoubleSlider("Block duration (MS)", 20, 100, 1, 500, 1));
        this.registerSetting(distance = new ModuleSettingDoubleSlider("Distance to player (blocks)", 0, 3, 0, 6, 0.01));
        this.registerSetting(chance = new ModuleSettingSlider("Chance %", 100, 0, 100, 1));
    }

    @FMLEvent
    public void yes(TickEvent.RenderTickEvent e){
        if(!Utils.Player.isPlayerInGame())
            return;

        if(engaged){
            if((engagedTime.hasTimeElapsed() || !Mouse.isButtonDown(0)) && duration.getInputMin() <= engagedTime.getElapsedTime()){
                engaged = false;
                release();
            }
            return;
        }

        if (Mouse.isButtonDown(0) && mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.player.getDistance(mc.objectMouseOver.entityHit) >= distance.getInputMin()&& mc.objectMouseOver.entityHit != null && mc.player.getDistance(mc.objectMouseOver.entityHit) <= distance.getInputMax() && (chance.getInput() == 100 || Math.random() <= chance.getInput() / 100)){
            engaged = true;
            engagedTime.setCooldown((long)duration.getInputMax());
            engagedTime.start();
            press();
        }
    }

    private static void release() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, false);
        Utils.Client.setMouseButtonState(1, false);
    }

    private static void press() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, true);
        KeyBinding.onTick(key);
        Utils.Client.setMouseButtonState(1, true);
    }
}
