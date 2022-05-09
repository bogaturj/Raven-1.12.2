package keystrokesmod.sToNkS.module.modules.render;

import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.module.modules.world.AntiBot;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
   public static ModuleSettingSlider a;
   public static ModuleSettingTick b;
   public static ModuleSettingTick c;
   public static ModuleSettingTick d;
   public static ModuleSettingTick rm;
   public static ModuleSettingTick e;

   public Nametags() {
      super("Nametags", Module.category.render, 0);
      this.registerSetting(a = new ModuleSettingSlider("Offset", 0.0D, -40.0D, 40.0D, 1.0D));
      this.registerSetting(b = new ModuleSettingTick("Rect", true));
      this.registerSetting(c = new ModuleSettingTick("Show health", true));
      this.registerSetting(d = new ModuleSettingTick("Show invis", true));
      this.registerSetting(rm = new ModuleSettingTick("Remove tags", false));
   }

   @FMLEvent
   public void r(Pre e) {
      if (rm.isToggled()) {
         e.setCanceled(true);
      } else {
         if (e.getEntity() instanceof EntityPlayer && e.getEntity() != mc.player && e.getEntity().deathTime == 0) {
            EntityPlayer en = (EntityPlayer)e.getEntity();
            if (!d.isToggled() && en.isInvisible()) {
               return;
            }

            if (AntiBot.bot(en) || en.getDisplayNameString().isEmpty()) {
               return;
            }

            e.setCanceled(true);
            String str = en.getDisplayName().getFormattedText();
            if (c.isToggled()) {
               double r = en.getHealth() / en.getMaxHealth();
               String h = (r < 0.3D ? "§c" : (r < 0.5D ? "§6" : (r < 0.7D ? "§e" : "§a"))) + Utils.Java.round(en.getHealth(), 1);
               str = str + " " + h;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate((float)e.getX() + 0.0F, (float)e.getY() + en.height + 0.5F, (float)e.getZ());
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            float f1 = 0.02666667F;
            GlStateManager.scale(-f1, -f1, f1);
            if (en.isSneaking()) {
               GlStateManager.translate(0.0F, 9.374999F, 0.0F);
            }

            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder worldrenderer = tessellator.getBuffer();
            int i = (int)(-a.getInput());
            int j = mc.fontRenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            if (b.isToggled()) {
               worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
               worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               tessellator.draw();
            }

            GlStateManager.enableTexture2D();
            mc.fontRenderer.drawString(str, -mc.fontRenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
         }

      }
   }
}
