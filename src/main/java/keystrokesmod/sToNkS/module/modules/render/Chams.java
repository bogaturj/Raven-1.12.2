package keystrokesmod.sToNkS.module.modules.render;

import keystrokesmod.sToNkS.module.Module;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {
   public Chams() {
      super("Chams", Module.category.render, 0);
   }

   @FMLEvent
   public void r1(Pre e) {
      if (e.getEntity() != mc.player) {
         GL11.glEnable(32823);
         GL11.glPolygonOffset(1.0F, -1100000.0F);
      }
   }

   @FMLEvent
   public void r2(Post e) {
      if (e.getEntity() != mc.player) {
         GL11.glDisable(32823);
         GL11.glPolygonOffset(1.0F, 1100000.0F);
      }
   }
}
