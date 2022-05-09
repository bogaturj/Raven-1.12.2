package keystrokesmod.sToNkS.module.modules.combat;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.entity.Entity;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class AutoWeapon extends Module {
    public static ModuleSettingTick onlyWhenHoldingDown;
    public static ModuleSettingTick goBackToPrevSlot;
    private boolean onWeapon;
    private int prevSlot;

    public AutoWeapon(){
        super("AutoWeapon", category.combat, 0);
        this.registerSetting(onlyWhenHoldingDown = new ModuleSettingTick("Only when holding lmb", true));
        this.registerSetting(goBackToPrevSlot = new ModuleSettingTick("Revert to old slot", true));
    }

    @FMLEvent
    public void datsDaSoundOfDaPolis(TickEvent.RenderTickEvent ev){
        if(!Utils.Player.isPlayerInGame() || mc.currentScreen != null) return;


        if(mc.objectMouseOver==null || mc.objectMouseOver.entityHit==null || (onlyWhenHoldingDown.isToggled() && !Mouse.isButtonDown(0))){
            if(onWeapon){
                onWeapon = false;
                if(goBackToPrevSlot.isToggled()){
                    mc.player.inventory.currentItem = prevSlot;
                }
            }
        } else{
            Entity target = mc.objectMouseOver.entityHit;
            if(onlyWhenHoldingDown.isToggled()){
                if(!Mouse.isButtonDown(0)) return;
            }
            if(!onWeapon){
                prevSlot = mc.player.inventory.currentItem;
                onWeapon = true;

                int maxDamageSlot = Utils.Player.getMaxDamageSlot();

                if(maxDamageSlot > 0 && Utils.Player.getSlotDamage(maxDamageSlot) > Utils.Player.getSlotDamage(mc.player.inventory.currentItem)){
                    mc.player.inventory.currentItem = maxDamageSlot;
                }
            }
        }
    }
}
