package keystrokesmod.sToNkS.module.modules.hotkey;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.item.ItemStack;

public class Ladders extends Module {
    private final ModuleSettingTick preferSlot;
    private final ModuleSettingSlider hotbarSlotPreference;
    public Ladders() {
        super("Ladders", category.hotkey, 0);

        this.registerSetting(preferSlot = new ModuleSettingTick("Prefer a slot", false));
        this.registerSetting(hotbarSlotPreference = new ModuleSettingSlider("Prefer wich slot", 8, 1, 9, 1));
    }

    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame())
            return;

        if (preferSlot.isToggled()) {
            int preferedSlot = (int) hotbarSlotPreference.getInput() - 1;

            if(checkSlot(preferedSlot)) {
                mc.player.inventory.currentItem = preferedSlot;
                this.disable();
                return;
            }
        }

        for (int slot = 0; slot <= 8; slot++) {
            if(checkSlot(slot)) {
                if(mc.player.inventory.currentItem != slot){
                    mc.player.inventory.currentItem = slot;
                } else {
                    return;
                }
                this.disable();
                return;
            }
        }
        this.disable();
    }

    public static boolean checkSlot(int slot) {
        ItemStack itemInSlot = mc.player.inventory.getStackInSlot(slot);
        if(itemInSlot == null)
            return false;

        return itemInSlot != null && itemInSlot.getDisplayName().equalsIgnoreCase("ladder");
    }
}
