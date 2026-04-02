package org.jon.magicclient.client.fightbot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.class_1657;
import net.minecraft.class_239;
import net.minecraft.class_310;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.commands.FriendsCommand;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class SwordBot implements Command {
   private final MessageHelper msgHelper = new MessageHelper();
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private class_1657 currentTarget = null;
   private long lastAttackTime = 0L;
   private long lastJumpTime = 0L;
   private boolean wasJumping = false;
   private int totalAttacks = 0;
   private int successfulHits = 0;
   private double targetDistance = 5.0D;
   private double attackDistance = 3.0D;
   private double jumpCooldown = 500.0D;
   private boolean autoJump = true;
   private boolean wTapEnabled = true;
   private boolean sTapEnabled = true;
   private long lastWTapTime = 0L;
   private long lastSTapTime = 0L;
   private long wTapCooldown = 200L;
   private long sTapCooldown = 300L;
   private boolean isWTapping = false;
   private boolean isSTapping = false;
   private boolean skeppingEnabled = true;
   private long lastSkepTime = 0L;
   private long skepCooldown = 400L;
   private boolean isSkepping = false;
   private boolean circleClockwise = true;
   private int circlePhase = 0;
   private long lastDirectionChange = 0L;
   private final List<OptionUtil> options = new ArrayList();

   public SwordBot() {
      this.options.add(new OptionUtil("Target Distance", OptionType.DOUBLE));
      this.options.add(new OptionUtil("Attack Distance", OptionType.DOUBLE));
      this.options.add(new OptionUtil("Jump Cooldown", OptionType.INTEGER));
      this.options.add(new OptionUtil("Auto Jump", OptionType.BOOLEAN));
      this.options.add(new OptionUtil("W-Tap", OptionType.BOOLEAN));
      this.options.add(new OptionUtil("S-Tap", OptionType.BOOLEAN));
      this.options.add(new OptionUtil("Skepping", OptionType.BOOLEAN));
      ((OptionUtil)this.options.get(0)).setValue(this.targetDistance);
      ((OptionUtil)this.options.get(1)).setValue(this.attackDistance);
      ((OptionUtil)this.options.get(2)).setValue((int)this.jumpCooldown);
      ((OptionUtil)this.options.get(3)).setValue(this.autoJump);
      ((OptionUtil)this.options.get(4)).setValue(this.wTapEnabled);
      ((OptionUtil)this.options.get(5)).setValue(this.sTapEnabled);
      ((OptionUtil)this.options.get(6)).setValue(this.skeppingEnabled);
   }

   public String getName() {
      return "swordbot";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
      if (enabled) {
         this.msgHelper.sendMessage("§a[SwordBot] Enabled! Enhanced target tracking activated...", true);
         String config = "TargetDistance=" + this.targetDistance + ", AttackDistance=" + this.attackDistance;
         DiscordWebhookSender.sendCrasherActivation("swordbot", config);
      } else {
         this.msgHelper.sendMessage("§c[SwordBot] Disabled!", true);
         this.currentTarget = null;
         this.resetMovement();
         DiscordWebhookSender.sendCrasherCompletion("swordbot", "SwordBot disabled", true);
      }

   }

   public String getDescription() {
      return "Enhanced Sword Bot with Aggressive Target Tracking";
   }

   public void onCommand(String[] args) {
      this.enabled = !this.enabled;
      if (this.enabled) {
         this.msgHelper.sendMessage("§a[SwordBot] Enabled! Enhanced target tracking activated...", true);
      } else {
         this.msgHelper.sendMessage("§c[SwordBot] Disabled!", true);
      }

   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.COMBAT;
   }

   public void onTick() {
      if (this.enabled && this.mc.field_1724 != null && this.mc.field_1687 != null) {
         this.targetDistance = (Double)((OptionUtil)this.options.get(0)).getValue();
         this.attackDistance = (Double)((OptionUtil)this.options.get(1)).getValue();
         this.jumpCooldown = (double)((Integer)((OptionUtil)this.options.get(2)).getValue()).longValue();
         this.autoJump = (Boolean)((OptionUtil)this.options.get(3)).getValue();
         class_1657 target = this.findBestTarget();
         if (target != null) {
            this.currentTarget = target;
            this.handleCombatMovement(target);
            this.handleCombat(target);
            if (this.autoJump) {
               this.handleJumping();
            }
         } else {
            this.currentTarget = null;
            this.resetMovement();
         }

      }
   }

   private class_1657 findBestTarget() {
      if (this.mc.field_1724 != null && this.mc.field_1687 != null) {
         if (this.currentTarget != null && this.isTargetValid(this.currentTarget)) {
            return this.currentTarget;
         } else {
            class_1657 bestTarget = null;
            double closestDistance = this.targetDistance;
            Iterator var4 = this.mc.field_1687.method_18456().iterator();

            while(var4.hasNext()) {
               class_1657 player = (class_1657)var4.next();
               if (player != this.mc.field_1724 && this.isTargetValid(player)) {
                  double distance = (double)this.mc.field_1724.method_5739(player);
                  if (distance < closestDistance && this.mc.field_1724.method_6057(player)) {
                     closestDistance = distance;
                     bestTarget = player;
                  }
               }
            }

            return bestTarget;
         }
      } else {
         return null;
      }
   }

   private boolean isTargetValid(class_1657 target) {
      if (target == null) {
         return false;
      } else if (!target.method_29504() && target.method_5805()) {
         if (FriendsCommand.isFriend(target.method_5477().getString())) {
            return false;
         } else {
            double distance = (double)this.mc.field_1724.method_5739(target);
            if (distance > this.targetDistance * 1.5D) {
               return false;
            } else {
               return this.mc.field_1724.method_6057(target) || !(distance > this.targetDistance);
            }
         }
      } else {
         return false;
      }
   }

   private void handleCombatMovement(class_1657 target) {
      if (this.mc.field_1724 != null && target != null) {
         double distance = (double)this.mc.field_1724.method_5739(target);
         this.resetMovement();
         double predictedX = target.method_23317() + target.method_18798().field_1352 * 0.8D;
         double predictedZ = target.method_23321() + target.method_18798().field_1350 * 0.8D;
         double predictedY = target.method_23318() + target.method_18798().field_1351 * 0.3D;
         boolean targetMovingAway = this.isTargetMovingAway(target);
         if (!(distance > this.attackDistance + 1.5D) && !targetMovingAway) {
            if (distance < 2.0D) {
               this.moveAway(target);
            } else {
               this.circleTargetEnhanced(target);
            }
         } else {
            this.moveTowardsPredictedAggressive(predictedX, predictedZ, predictedY);
         }

         this.faceTargetEnhanced(target, predictedX, predictedZ, predictedY);
         if (this.shouldJump(target)) {
            this.mc.field_1724.method_6043();
            this.lastJumpTime = System.currentTimeMillis();
         }

      }
   }

   private boolean isTargetMovingAway(class_1657 target) {
      if (target != null && this.mc.field_1724 != null) {
         double dx = target.method_23317() - this.mc.field_1724.method_23317();
         double dz = target.method_23321() - this.mc.field_1724.method_23321();
         double velX = target.method_18798().field_1352;
         double velZ = target.method_18798().field_1350;
         double dotProduct = dx * velX + dz * velZ;
         return dotProduct > 0.0D && (Math.abs(velX) > 0.1D || Math.abs(velZ) > 0.1D);
      } else {
         return false;
      }
   }

   private void moveTowardsPredictedAggressive(double predictedX, double predictedZ, double predictedY) {
      double dx = predictedX - this.mc.field_1724.method_23317();
      double dz = predictedZ - this.mc.field_1724.method_23321();
      double dy = predictedY - this.mc.field_1724.method_23318();
      double distance = Math.sqrt(dx * dx + dz * dz);
      if (distance > 0.0D) {
         double angle = Math.atan2(dz, dx);
         double playerYaw = Math.toRadians((double)this.mc.field_1724.method_36454());

         double relativeAngle;
         for(relativeAngle = angle - playerYaw; relativeAngle > 3.141592653589793D; relativeAngle -= 6.283185307179586D) {
         }

         while(relativeAngle < -3.141592653589793D) {
            relativeAngle += 6.283185307179586D;
         }

         this.mc.field_1690.field_1894.method_23481(true);
         this.mc.field_1690.field_1867.method_23481(true);
         if (Math.abs(relativeAngle) > 0.5235987755982988D) {
            if (relativeAngle > 0.0D) {
               this.mc.field_1690.field_1849.method_23481(true);
            } else {
               this.mc.field_1690.field_1913.method_23481(true);
            }
         }

         if (dy > 1.0D && this.mc.field_1724.method_24828()) {
            this.mc.field_1724.method_6043();
         }
      }

   }

   private void moveAway(class_1657 target) {
      double dx = this.mc.field_1724.method_23317() - target.method_23317();
      double dz = this.mc.field_1724.method_23321() - target.method_23321();
      double distance = Math.sqrt(dx * dx + dz * dz);
      if (distance > 0.0D) {
         double angle = Math.atan2(dz, dx);
         double playerYaw = Math.toRadians((double)this.mc.field_1724.method_36454());

         double relativeAngle;
         for(relativeAngle = angle - playerYaw; relativeAngle > 3.141592653589793D; relativeAngle -= 6.283185307179586D) {
         }

         while(relativeAngle < -3.141592653589793D) {
            relativeAngle += 6.283185307179586D;
         }

         this.mc.field_1690.field_1881.method_23481(true);
         if (Math.abs(relativeAngle) > 0.7853981633974483D) {
            if (relativeAngle > 0.0D) {
               this.mc.field_1690.field_1849.method_23481(true);
            } else {
               this.mc.field_1690.field_1913.method_23481(true);
            }
         }
      }

   }

   private void circleTargetEnhanced(class_1657 target) {
      if (this.mc.field_1724 != null && target != null) {
         long currentTime = System.currentTimeMillis();
         if ((double)(currentTime - this.lastDirectionChange) > 1500.0D + Math.random() * 1000.0D) {
            this.circleClockwise = !this.circleClockwise;
            this.circlePhase = (this.circlePhase + 1) % 4;
            this.lastDirectionChange = currentTime;
         }

         double baseRadius = 3.0D;
         double radiusVariation = Math.sin((double)currentTime / 800.0D) * 0.8D;
         double radius = baseRadius + radiusVariation;
         double angleSpeed = this.circleClockwise ? 1.2D : -1.2D;
         double phaseOffset = (double)this.circlePhase * 3.141592653589793D / 2.0D;
         double time = (double)currentTime / 1000.0D;
         double angle = time * angleSpeed + phaseOffset;
         double predictedX = target.method_23317() + target.method_18798().field_1352 * 0.6D;
         double predictedZ = target.method_23321() + target.method_18798().field_1350 * 0.6D;
         double circleX = predictedX + Math.cos(angle) * radius;
         double circleZ = predictedZ + Math.sin(angle) * radius;
         double dx = circleX - this.mc.field_1724.method_23317();
         double dz = circleZ - this.mc.field_1724.method_23321();
         double distance = Math.sqrt(dx * dx + dz * dz);
         if (distance > 0.5D) {
            double moveAngle = Math.atan2(dz, dx);
            double playerYaw = Math.toRadians((double)this.mc.field_1724.method_36454());

            double relativeAngle;
            for(relativeAngle = moveAngle - playerYaw; relativeAngle > 3.141592653589793D; relativeAngle -= 6.283185307179586D) {
            }

            while(relativeAngle < -3.141592653589793D) {
               relativeAngle += 6.283185307179586D;
            }

            this.mc.field_1690.field_1894.method_23481(true);
            this.mc.field_1690.field_1867.method_23481(true);
            if (Math.abs(relativeAngle) > 0.7853981633974483D) {
               if (relativeAngle > 0.0D) {
                  this.mc.field_1690.field_1849.method_23481(true);
               } else {
                  this.mc.field_1690.field_1913.method_23481(true);
               }
            }
         }

      }
   }

   private void faceTargetEnhanced(class_1657 target, double predictedX, double predictedZ, double predictedY) {
      if (this.mc.field_1724 != null && target != null) {
         double dx = predictedX - this.mc.field_1724.method_23317();
         double dy = predictedY - (this.mc.field_1724.method_23318() + (double)this.mc.field_1724.method_5751());
         double dz = predictedZ - this.mc.field_1724.method_23321();
         double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
         float targetYaw = (float)Math.toDegrees(-Math.atan2(dx, dz));
         float targetPitch = (float)Math.toDegrees(-Math.atan2(dy, distance));
         float currentYaw = this.mc.field_1724.method_36454();
         float currentPitch = this.mc.field_1724.method_36455();
         float yawDiff = this.normalizeAngle(targetYaw - currentYaw);
         float pitchDiff = this.normalizeAngle(targetPitch - currentPitch);
         float distanceFactor = Math.min(1.5F, (float)(distance / 8.0D));
         float maxYawSpeed = 12.0F * distanceFactor;
         float maxPitchSpeed = 10.0F * distanceFactor;
         float randomYawVariation = (float)(Math.random() * 0.2D - 0.1D);
         float randomPitchVariation = (float)(Math.random() * 0.15D - 0.075D);
         float yawSpeed = Math.min(maxYawSpeed, Math.abs(yawDiff) * 0.4F * distanceFactor);
         float pitchSpeed = Math.min(maxPitchSpeed, Math.abs(pitchDiff) * 0.35F * distanceFactor);
         float newYaw = currentYaw;
         float newPitch = currentPitch;
         float pitchStep;
         if (Math.abs(yawDiff) > 0.3F) {
            pitchStep = Math.signum(yawDiff) * Math.min(yawSpeed, Math.abs(yawDiff));
            newYaw = currentYaw + pitchStep + randomYawVariation;
         }

         if (Math.abs(pitchDiff) > 0.3F) {
            pitchStep = Math.signum(pitchDiff) * Math.min(pitchSpeed, Math.abs(pitchDiff));
            newPitch = currentPitch + pitchStep + randomPitchVariation;
         }

         this.mc.field_1724.method_36456(newYaw);
         this.mc.field_1724.method_36457(Math.max(-90.0F, Math.min(90.0F, newPitch)));
      }
   }

   private float normalizeAngle(float angle) {
      angle %= 360.0F;
      if (angle > 180.0F) {
         angle -= 360.0F;
      } else if (angle < -180.0F) {
         angle += 360.0F;
      }

      return angle;
   }

   private void handleCombat(class_1657 target) {
      if (this.mc.field_1724 != null && this.mc.field_1761 != null) {
         double distance = (double)this.mc.field_1724.method_5739(target);
         if (distance <= this.attackDistance && this.isSwordReady()) {
            class_239 hit = this.mc.field_1724.method_5745(this.attackDistance, 0.0F, false);
            if (hit.method_17783() == class_240.field_1331 && ((class_3966)hit).method_17782() == target || distance <= this.attackDistance && this.mc.field_1724.method_6057(target)) {
               if (this.wTapEnabled && this.shouldWTap(target)) {
                  this.performWTap();
               } else if (this.sTapEnabled && this.shouldSTap(target)) {
                  this.performSTap();
               } else if (this.skeppingEnabled && this.shouldSkep(target)) {
                  this.performSkep();
               }

               this.mc.field_1761.method_2918(this.mc.field_1724, target);
               this.mc.field_1724.method_6104(this.mc.field_1724.method_6058());
               this.lastAttackTime = System.currentTimeMillis();
               ++this.totalAttacks;
               ++this.successfulHits;
               if (this.totalAttacks % 3 == 0) {
                  this.lastJumpTime = 0L;
               }
            }
         }

      }
   }

   private void handleJumping() {
      long currentTime = System.currentTimeMillis();
      if (this.totalAttacks > 0 && this.totalAttacks % 3 == 0) {
         if ((double)(currentTime - this.lastJumpTime) >= this.jumpCooldown && this.mc.field_1724.method_24828()) {
            this.mc.field_1724.method_6043();
            this.lastJumpTime = currentTime;
            this.wasJumping = true;
         }
      } else if (!this.mc.field_1724.method_24828()) {
         this.wasJumping = false;
      }

   }

   private boolean isSwordReady() {
      if (this.mc.field_1724 == null) {
         return false;
      } else {
         float cooldownProgress = this.mc.field_1724.method_7261(0.5F);
         return cooldownProgress >= 0.9F;
      }
   }

   private void resetMovement() {
      if (this.mc.field_1724 != null) {
         this.mc.field_1690.field_1894.method_23481(false);
         this.mc.field_1690.field_1881.method_23481(false);
         this.mc.field_1690.field_1913.method_23481(false);
         this.mc.field_1690.field_1849.method_23481(false);
         this.mc.field_1690.field_1867.method_23481(false);
      }
   }

   private boolean shouldJump(class_1657 target) {
      if (target != null && this.mc.field_1724 != null && this.autoJump) {
         long currentTime = System.currentTimeMillis();
         if ((double)(currentTime - this.lastJumpTime) < this.jumpCooldown) {
            return false;
         } else {
            double distance = (double)this.mc.field_1724.method_5739(target);
            return distance <= this.attackDistance + 1.0D && this.mc.field_1724.method_24828();
         }
      } else {
         return false;
      }
   }

   private boolean shouldWTap(class_1657 target) {
      if (target != null && this.wTapEnabled) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.lastWTapTime < this.wTapCooldown) {
            return false;
         } else {
            double distance = (double)this.mc.field_1724.method_5739(target);
            return distance <= this.attackDistance + 0.5D && !this.isWTapping;
         }
      } else {
         return false;
      }
   }

   private boolean shouldSTap(class_1657 target) {
      if (target != null && this.sTapEnabled) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.lastSTapTime < this.sTapCooldown) {
            return false;
         } else {
            double distance = (double)this.mc.field_1724.method_5739(target);
            return distance <= this.attackDistance + 0.5D && !this.isSTapping;
         }
      } else {
         return false;
      }
   }

   private boolean shouldSkep(class_1657 target) {
      if (target != null && this.skeppingEnabled) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.lastSkepTime < this.skepCooldown) {
            return false;
         } else {
            double distance = (double)this.mc.field_1724.method_5739(target);
            return distance <= this.attackDistance + 0.5D && !this.isSkepping;
         }
      } else {
         return false;
      }
   }

   private void performWTap() {
      if (this.mc.field_1724 != null) {
         this.isWTapping = true;
         this.lastWTapTime = System.currentTimeMillis();
         this.mc.field_1690.field_1881.method_23481(true);
         Timer timer = new Timer();
         timer.schedule(new TimerTask() {
            public void run() {
               SwordBot.this.mc.field_1690.field_1881.method_23481(false);
               SwordBot.this.mc.field_1690.field_1894.method_23481(true);
               (new Timer()).schedule(new TimerTask() {
                  // $FF: synthetic field
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = this$1;
                  }

                  public void run() {
                     this.this$1.this$0.mc.field_1690.field_1894.method_23481(false);
                     this.this$1.this$0.isWTapping = false;
                  }
               }, 50L);
            }
         }, 50L);
      }
   }

   private void performSTap() {
      if (this.mc.field_1724 != null) {
         this.isSTapping = true;
         this.lastSTapTime = System.currentTimeMillis();
         this.mc.field_1690.field_1913.method_23481(true);
         Timer timer = new Timer();
         timer.schedule(new TimerTask() {
            public void run() {
               SwordBot.this.mc.field_1690.field_1913.method_23481(false);
               SwordBot.this.mc.field_1690.field_1849.method_23481(true);
               (new Timer()).schedule(new TimerTask() {
                  // $FF: synthetic field
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = this$1;
                  }

                  public void run() {
                     this.this$1.this$0.mc.field_1690.field_1849.method_23481(false);
                     this.this$1.this$0.isSTapping = false;
                  }
               }, 50L);
            }
         }, 50L);
      }
   }

   private void performSkep() {
      if (this.mc.field_1724 != null) {
         this.isSkepping = true;
         this.lastSkepTime = System.currentTimeMillis();
         this.mc.field_1690.field_1894.method_23481(true);
         this.mc.field_1690.field_1867.method_23481(true);
         this.mc.field_1690.field_1849.method_23481(true);
         Timer timer = new Timer();
         timer.schedule(new TimerTask() {
            public void run() {
               SwordBot.this.mc.field_1690.field_1894.method_23481(false);
               SwordBot.this.mc.field_1690.field_1867.method_23481(false);
               SwordBot.this.mc.field_1690.field_1849.method_23481(false);
               SwordBot.this.isSkepping = false;
            }
         }, 100L);
      }
   }

   public String getArgsUsage() {
      return "";
   }
}
