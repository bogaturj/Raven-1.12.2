package keystrokesmod.keystroke;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class keystrokeCommand extends CommandBase {
   public String getName() {
      return "keystrokesmod";
   }

   public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
      Minecraft.getMinecraft().displayGuiScreen(new KeyStrokeConfigGui());
      System.out.println(Minecraft.getMinecraft().currentScreen.getClass());
   }

   public String getUsage(ICommandSender sender) {
      return "/keystrokesmod";
   }

   public int getRequiredPermissionLevel() {
      return 0;
   }

   public boolean canCommandSenderUseCommand(ICommandSender sender) {
      return true;
   }
}
