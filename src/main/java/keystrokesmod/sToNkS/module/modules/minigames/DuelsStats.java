package keystrokesmod.sToNkS.module.modules.minigames;

import keystrokesmod.sToNkS.main.Ravenbplus;
import keystrokesmod.sToNkS.module.Module;
import keystrokesmod.sToNkS.module.ModuleDesc;
import keystrokesmod.sToNkS.module.ModuleSettingSlider;
import keystrokesmod.sToNkS.module.ModuleSettingTick;
import keystrokesmod.sToNkS.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import keystrokesmod.sToNkS.lib.fr.jmraich.rax.event.FMLEvent;

import java.util.ArrayList;
import java.util.List;

public class DuelsStats extends Module {
   public static ModuleSettingSlider value;
   public static ModuleDesc bruh;
   public static ModuleSettingSlider threatLeaveMode;
   public static ModuleDesc moduleDesc;
   public static ModuleSettingTick a;
   public static ModuleSettingTick queueDodge;
   public static ModuleSettingTick threatLevel;
   public static String nk = "";
   private String ign = "";
   private String en = "";
   private static final String[] thr_lvl;
   private final List<String> q = new ArrayList<>();

   public DuelsStats() {
      super("Duels Stats", Module.category.minigames, 0);
      this.registerSetting(value = new ModuleSettingSlider("Value", 1.0D, 1.0D, 7.0D, 1.0D));
      this.registerSetting(moduleDesc = new ModuleDesc("Mode: OVERALL"));
      this.registerSetting(a = new ModuleSettingTick("Send ign on join", false));
      this.registerSetting(threatLevel = new ModuleSettingTick("Threat Level", true));
      this.registerSetting(queueDodge = new ModuleSettingTick("Queue dodge", false));
      this.registerSetting(threatLeaveMode = new ModuleSettingSlider("Value:", 4, 1, 4, 1));
      this.registerSetting(bruh = new ModuleDesc("Dodge threat level: HIGH"));
   }

   public void onEnable() {
      if (mc.player != null) {
         this.ign = mc.player.getName();
      } else {
         this.disable();
      }

   }

   public void onDisable() {
      this.en = "";
      this.q.clear();
   }

   public void guiUpdate() {
      moduleDesc.setDesc(Utils.md + Utils.Profiles.DM.values()[(int)(value.getInput() - 1.0D)].name());
      bruh.setDesc("Dodge levels: " + thr_lvl[(int)threatLeaveMode.getInput() - 1].substring(2, thr_lvl[(int)threatLeaveMode.getInput() - 1].length() -1));
   }

   public void update() {
      if (this.id() && this.en.isEmpty()) {
         List<EntityPlayer> pl = mc.world.playerEntities;
         pl.remove(mc.player);

         for (EntityPlayer p : pl) {
            String n = p.getName();
            if (!n.equals(this.ign) && !n.equals(nk) && !this.q.contains(n) && p.getDisplayName().getUnformattedText().contains("§k")) {
               this.ef(n);
               break;
            }
         }
      }

   }

   @FMLEvent
   public void onMessageRecieved(ClientChatReceivedEvent c) {
      if (Utils.Player.isPlayerInGame() && this.id()) {
         String s = Utils.Java.str(c.getMessage().getUnformattedText());
         if (s.contains(" ")) {
            String[] sp = s.split(" ");
            String n;
            if (sp.length == 4 && sp[1].equals("has") && sp[2].equals("joined") && sp[3].equals("(2/2)!")) {
               n = sp[0];
               if (!n.equals(this.ign) && !n.equals(nk) && this.en.isEmpty()) {
                  this.q.remove(n);
                  this.ef(n);
               }
            } else if (sp.length == 3 && sp[1].equals("has") && sp[2].equals("quit!")) {
               n = sp[0];
               if (this.en.equals(n)) {
                  this.en = "";
               }

               this.q.add(n);
            }
         }

      }
   }

   @FMLEvent
   public void onEntityJoin(EntityJoinWorldEvent j) {
      if (j.getEntity() == mc.player) {
         this.en = "";
         this.q.clear();
      }

   }

   private void ef(String n) {
      this.en = n;
      if (a.isToggled()) {
         Utils.Player.sendMessageToSelf("&eOpponent found: " + "&3" + n);
      }

      if (Utils.URLS.hypixelApiKey.isEmpty()) {
         Utils.Player.sendMessageToSelf("&cAPI Key is empty!");
      } else {
         Utils.Profiles.DM dm = Utils.Profiles.DM.values()[(int)(value.getInput() - 1.0D)];
         Ravenbplus.getExecutor().execute(() -> {
            int[] s = Utils.Profiles.getHypixelStats(n, dm);
            if (s != null) {
               if (s[0] == -1) {
                  Utils.Player.sendMessageToSelf("&3" + n + " " + "&eis nicked!");
                  return;
               }

               double wlr = s[1] != 0 ? Utils.Java.round((double)s[0] / (double)s[1], 2) : (double)s[0];
               Utils.Player.sendMessageToSelf("&7&m-------------------------");
               if (dm != Utils.Profiles.DM.OVERALL) {
                  Utils.Player.sendMessageToSelf("&e" + Utils.md + "&3" + dm.name());
               }

               Utils.Player.sendMessageToSelf("&eOpponent: &3" + n);
               Utils.Player.sendMessageToSelf("&eWins: &3" + s[0]);
               Utils.Player.sendMessageToSelf("&eLosses: &3" + s[1]);
               Utils.Player.sendMessageToSelf("&eWLR: &3" + wlr);
               Utils.Player.sendMessageToSelf("&eWS: &3" + s[2]);
               if (threatLevel.isToggled()) {
                  Utils.Player.sendMessageToSelf("&eThreat: &3" + gtl(s[0], s[1], wlr, s[2]));
               }
               if(queueDodge.isToggled()) {
                  if(Utils.Java.indexOf(thr_lvl[(int)threatLeaveMode.getInput() - 1], thr_lvl) <= (int)threatLeaveMode.getInput() - 1) {
                     Utils.Player.sendMessageToSelf("&cLeaving because the player was too dangerous!");
                     mc.player.sendChatMessage("/l");
                  }else{
                     Utils.Player.sendMessageToSelf("&2Player meets queue expectations!");
                  }
               }

               Utils.Player.sendMessageToSelf("&7&m-------------------------");


            } else {
               Utils.Player.sendMessageToSelf("&cThere was an error.");
            }

         });
      }
   }

   private boolean id() {
      if (Utils.Client.isHyp()) {
         int l = 0;

         for (String s : Utils.Client.getPlayersFromScoreboard()) {
            if (s.contains("Map:")) {
               ++l;
            } else if (s.contains("Players:") && s.contains("/2")) {
               ++l;
            }
         }

         return l == 2;
      } else {
         return false;
      }
   }

   public static String gtl(int wins, int loses, double wlr, int ws) {
      int t = 0;
      int m = wins + loses;
      if (m <= 13) {
         t += 2;
      }

      if (ws >= 30) {
         t += 9;
      } else if (ws >= 15) {
         t += 7;
      } else if (ws >= 8) {
         t += 5;
      } else if (ws >= 4) {
         t += 3;
      } else if (ws >= 1) {
         ++t;
      }

      if (wlr >= 20.0D) {
         t += 8;
      } else if (wlr >= 10.0D) {
         t += 5;
      } else if (wlr >= 5.0D) {
         t += 4;
      } else if (wlr >= 3.0D) {
         t += 2;
      } else if (wlr >= 0.8D) {
         ++t;
      }

      if (wins >= 20000) {
         t += 4;
      } else if (wins >= 10000) {
         t += 3;
      } else if (wins >= 5000) {
         t += 2;
      } else if (wins >= 1000) {
         ++t;
      }

      if (loses == 0) {
         if (wins == 0) {
            t += 3;
         } else {
            t += 4;
         }
      } else if (loses <= 10 && wlr >= 4.0D) {
         t += 2;
      }

      String thr;
      if (t == 0) {
         thr = thr_lvl[4];
      } else if (t <= 3) {
         thr = thr_lvl[3];
      } else if (t <= 6) {
         thr = thr_lvl[2];
      } else if (t <= 10) {
         thr = thr_lvl[1];
      } else {
         thr = thr_lvl[0];
      }

      return thr;
   }

   static {
      thr_lvl = new String[]{"&4VERY HIGH", "&cHIGH", "&6MODERATE", "&aLOW", "&2VERY LOW"};
   }
}
