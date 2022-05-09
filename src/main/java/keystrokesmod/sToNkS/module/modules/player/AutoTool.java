package keystrokesmod.sToNkS.module.modules.player;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingDoubleSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.module.modules.combat.AutoClicker;
import keystrokesmod.sToNkS.utils.CoolDown;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class AutoTool extends Module {
    private final ModuleSettingTick hotkeyBack;
    private Block previousBlock;
    private boolean isWaiting;
    public static ModuleSettingDoubleSlider mineDelay;
    public static int previousSlot;
    public static boolean justFinishedMining, mining;
    public static CoolDown delay;
    //public static List<Block> pickaxe = Arrays.asList(ItemBlock.class, BlockIce.class);

    public AutoTool() {
        super("Auto Tool", category.player, 0);

        this.registerSetting(hotkeyBack = new ModuleSettingTick("Hotkey back", true));
        this.registerSetting(mineDelay = new ModuleSettingDoubleSlider("Max delay", 10, 50, 0, 2000, 1));
        delay = new CoolDown(0);
    }

    @FMLEvent
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (!Utils.Player.isPlayerInGame() || mc.currentScreen != null)
            return;

        // quit if the player is not tryna mine
        if(!Mouse.isButtonDown(0)){
            if(mining)
                finishMining();
            if(isWaiting)
                isWaiting = false;
            return;
        }



        //make sure that we are allowed to breack blocks if ac is enabled
        if(AutoClicker.autoClickerEnabled) {
            if(!AutoClicker.breakBlocks.isToggled()) {
                return;
            }
        }

        BlockPos lookingAtBlock = mc.objectMouseOver.getBlockPos();
        if (lookingAtBlock != null) {

            Block stateBlock = mc.world.getBlockState(lookingAtBlock).getBlock();
            if (stateBlock != Blocks.AIR && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block) {

                if(mineDelay.getInputMax() > 0){
                    if(previousBlock != null){
                        if(previousBlock!=stateBlock){
                            previousBlock = stateBlock;
                            isWaiting = true;
                            delay.setCooldown((long)ThreadLocalRandom.current().nextDouble(mineDelay.getInputMin(), mineDelay.getInputMax() + 0.01));
                            delay.start();
                        } else {
                            if(isWaiting && delay.hasTimeElapsed()) {
                                isWaiting = false;
                                previousSlot = Utils.Player.getCurrentPlayerSlot();
                                mining = true;
                                hotkeyToFastest();
                            }
                        }
                    } else {
                        previousBlock = stateBlock;
                        isWaiting = false;
                    }
                    return;
                }

                if(!mining) {
                    previousSlot = Utils.Player.getCurrentPlayerSlot();
                    mining = true;
                }

                hotkeyToFastest();
            }
        }
    }

    public void finishMining(){
        if(hotkeyBack.isToggled()) {
            Utils.Player.hotkeyToSlot(previousSlot);
        }
        justFinishedMining = false;
        mining = false;
    }

    private void hotkeyToFastest(){
        int index = -1;
        double speed = 1;


        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = mc.player.inventory.getStackInSlot(slot);
            if(itemInSlot != null) {
                if( itemInSlot.getItem() instanceof ItemTool || itemInSlot.getItem() instanceof ItemShears){
                    BlockPos p = mc.objectMouseOver.getBlockPos();
                    Block bl = mc.world.getBlockState(p).getBlock();

                    if(itemInSlot.getItem().getDestroySpeed(itemInSlot, bl.getDefaultState()) > speed) {
                        speed = itemInSlot.getItem().getDestroySpeed(itemInSlot, bl.getDefaultState());
                        index = slot;
                    }
                }
            }
        }

        if(index == -1 || speed <= 1.1 || speed == 0) {
        } else {
            Utils.Player.hotkeyToSlot(index);
        }
    }
}
