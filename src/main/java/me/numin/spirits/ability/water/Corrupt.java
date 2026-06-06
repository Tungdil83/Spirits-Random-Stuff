package me.numin.spirits.ability.water;

import java.time.Duration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.airbending.Suffocate;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.numin.spirits.SpiritElement;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.WaterAbility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Corrupt extends WaterAbility {

    public LivingEntity target;
    @Attribute(Attribute.RANGE)
    private double range;
    public static Set<Integer> heldEntities = new HashSet<Integer>();
    public byte stage = 0;
    public Location travelLoc = null;
    @Attribute(Attribute.DURATION)
    private long duration;
    public double yaw;
    public Random random;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    private boolean hasReached = true;
    private int ticks;
    private int chargeTicks;
    private int potionduration;
    private int potionlevel;
    private boolean charged = false;
    private boolean setElement;
    long durationCurrentTime;
    long durationCurrentTime2;

    //for the future to optimize a bit, maybe. This kinda old. 
    /*public static int colorMixer(int a) {
        
        a = a+30;
        System.out.println("Value of a: " + a);
        if (a > 254 || a < 0) {
            a = 253;
        }
        return a;
    };*/

    public Corrupt(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            System.out.println("Fail");
            return;
        }



        firstloop: for (int i = 20; i < 100; i++) {
            Location loc = GeneralMethods.getTargetedLocation(player, range);
            for (Entity e : GeneralMethods.getEntitiesAroundPoint(loc, 10)) {
                if (e instanceof LivingEntity && e.getEntityId() != player.getEntityId()) {
                    //System.out.println("Target is a living entity 2");
                    target = (LivingEntity) e;
                    break firstloop;
                }
            }
        }

        if (target instanceof LivingEntity) {
            heldEntities.add(target.getEntityId());
            setFields();
            this.target = (LivingEntity) target;
            //System.out.println("Target is a living entity");
            start();
        }
        
        if (target == null) {
            return;
        }
        

        /*heldEntities.add(target.getEntityId());
        setFields();
        if (isEnabled()) {
            start();
        }*/
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Water.Corrupt.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Water.Corrupt.Duration");
        this.range = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Water.Corrupt.Range");
        this.potionduration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.Water.Corrupt.PotionDuration");
        this.potionlevel = Spirits.plugin.getConfig().getInt("Abilities.Spirits.Water.Corrupt.PotionLevel");
        this.setElement = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Water.Corrupt.SetElement");
    }

    public double calculateSize(LivingEntity entity) {
        return (entity.getEyeLocation().distance(entity.getLocation()) / 2 + 0.8D);
    }

    @Override
    public void remove() {
        super.remove();

        if (target != null) {
            heldEntities.remove(target.getEntityId());
        }
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public String getName() {
        return "Corrupt";
    }

    @Override
    public boolean isExplosiveAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public boolean isIgniteAbility() {
        return false;
    }

    @Override
    public boolean isSneakAbility() {
        return true;
    }


    
    @Override
    public void progress() {
        if (!bPlayer.canBendIgnoreCooldowns(this)) {
            remove();
            return;
        }

        if (target == null || target.isDead()) {
            remove();
            return;
        }


        if (!target.getWorld().equals(player.getWorld())) {
            remove();
            return;
        }

        if (target.getLocation().distance(player.getLocation()) > 25) {

            remove();
            return;
        }

        if (System.currentTimeMillis() - getStartTime() > duration*0.5 /*default 10000L */) {
            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "* READY *"));
            charged = true;
            createNewSpirals();
        }

        if (System.currentTimeMillis() - getStartTime() > duration) {
            remove();
            bPlayer.addCooldown(this);
            return;
        }

        if (charged) {
            if (!player.isSneaking()) {
                if (target instanceof OfflinePlayer && setElement) {
                    BendingPlayer bPlayer = BendingPlayer.getBendingPlayer((OfflinePlayer) target);
                    if (bPlayer.hasElement(SpiritElement.LIGHT)) {
                        bPlayer.addElement(SpiritElement.DARK);
                        bPlayer.getElements().remove(SpiritElement.LIGHT);
                        bPlayer.saveElements();
                        bPlayer.removeUnusableAbilities();
                        //GeneralMethods.saveElements(bPlayer);
                        //GeneralMethods.removeUnusableAbilities(bPlayer.getName());
                        target.sendMessage(SpiritElement.LIGHT.getColor() + "You are now a" + ChatColor.BOLD + "" + ChatColor.BLUE + " DarkSpirit");
                        ParticleEffect.SPELL_WITCH.display(target.getLocation(), 3, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
                    } else {
                        DamageHandler.damageEntity(target, 7, this);
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 300, 2));
                        ParticleEffect.SPELL_WITCH.display(target.getLocation(), 3, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
                    }

                } else if (target instanceof Animals) {
                    Location targetlocation = target.getLocation();
                    // Pig -> Zombified Piglin
                    if (target.getType() == EntityType.PIG) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.ZOMBIFIED_PIGLIN);
                    }
                    // Cow -> Skeleton
                    else if (target.getType() == EntityType.COW) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.SKELETON);
                    }
                    // Rabbit -> Stray
                    else if (target.getType() == EntityType.RABBIT) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.STRAY);
                    }
                    // Chicken -> Husk
                    else if (target.getType() == EntityType.CHICKEN) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.HUSK);
                    }
                    // Cat -> Witch
                    else if (target.getType() == EntityType.CAT) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.WITCH);
                    }
                    // Villager -> Zombie Villager (random chance)
                    else if (target.getType() == EntityType.VILLAGER) {
                                                if (targetlocation.getBlock().isLiquid() || targetlocation.getBlock().getType().toString().contains("WATER")) {
                            target.getWorld().spawnEntity(targetlocation, EntityType.DROWNED);
                        }
                        Random rand = new Random();
                        int roll = rand.nextInt(100);
                        if (roll < 10) { // 10% chance for Zombie Villager
                            target.getWorld().spawnEntity(targetlocation, EntityType.ZOMBIE_VILLAGER);
                        } else {
                            target.getWorld().spawnEntity(targetlocation, EntityType.ZOMBIE);
                        }
                    }
                    // Allay -> Vex
                    else if (target.getType() == EntityType.ALLAY) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.VEX);
                    }
                    // Default: Vex
                    else {
                        target.getWorld().spawnEntity(targetlocation, EntityType.VEX);
                    }
                    target.remove();
                    ParticleEffect.PORTAL.display(target.getLocation(), 8, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);

                }   else if (target instanceof Villager) {
                    Location targetlocation = target.getLocation();
                    // Villager -> Zombie Villager
                    target.getWorld().spawnEntity(targetlocation, EntityType.ZOMBIE_VILLAGER);
                    target.remove();
                    ParticleEffect.PORTAL.display(target.getLocation(), 8, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);

                } else if (target != null) {
                    DamageHandler.damageEntity(target, 4, this);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, potionduration, potionlevel));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, potionduration, potionlevel));
                    ParticleEffect.SPELL_WITCH.display(target.getLocation(), 3, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
                }
            }
        }

        if (stage == 0) {

            if (!player.isSneaking()) {
                bPlayer.addCooldown(this);
                remove();
                return;
            }

            if (travelLoc == null && this.getStartTime() + duration < System.currentTimeMillis()) {
                bPlayer.addCooldown(this);
                travelLoc = player.getEyeLocation();
            } else if (travelLoc == null) {
                ticks++;
                Long chargingTime = System.currentTimeMillis() - getStartTime();
                this.chargeTicks = (int) (chargingTime / 25);
                if (!charged) {
                    createSpirals();
                } else {
                    createNewSpirals();
                }
                //ParticleEffect.MAGIC_CRIT.display(0.3F, 0.3F, 0.3F, 0.1F, 8, target.getLocation().clone().add(0, 0.8, 0), 90);
                //f7f2f6
                for (int i = -180; i < 180; i += 10) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 300, 128));
//                    target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 300, 128)); Disabled because of issue with high jumping while corruption
                }
                return;
            }
        }
    }

    public void paralyze(Entity entity) {
        if (entity instanceof Creature) {
            ((Creature) entity).setTarget(null);
        }

        if (entity instanceof Player) {
            if (Suffocate.isChannelingSphere((Player) entity)) {
                Suffocate.remove((Player) entity);
            }
        }
        
    }
    private void createSpirals() {

        int bluecolornumber = 244;
        int greencolornumber = 170;
        int redcolornumber = 66;
              
        Color purple = Color.fromRGB(193, 44, 176);
        DustOptions dustPurple = new DustOptions(purple, 1);

        Color darkPurple = Color.fromRGB(201, 58, 137);
        DustOptions dustDark = new DustOptions(darkPurple, 1);

        double increaseValueToChange = (duration*0.5);
        //12000ms * 0.5 = 6000ms, 6000ms = 100%.
        //1000ms = 1s
        //20t = 1s
        //Color purple = Color.fromRGB(193, 44, 176);
        //6000ms
        //193+44+176=413
        //duration/413()*2
        
        durationCurrentTime = (System.currentTimeMillis() - getStartTime()) / 10;
        durationCurrentTime2 = (System.currentTimeMillis() - getStartTime());
        durationCurrentTime2 = ((durationCurrentTime2/100)+30);
        //System.currentTimeMillis() - getStartTime() > duration*0.5
        durationCurrentTime2 = (duration);

        bluecolornumber = bluecolornumber - (int) (durationCurrentTime2);
        greencolornumber = greencolornumber - (int) (durationCurrentTime2 );
        redcolornumber = redcolornumber + (int) (durationCurrentTime2) ;
        //System.out.println("Duration Time" + durationCurrentTime2 + " /100 = " + durationCurrentTime2/100);

        // Clamp RGB values to 0-255
        bluecolornumber = Math.max(0, Math.min(255, bluecolornumber));
        greencolornumber = Math.max(0, Math.min(255, greencolornumber));
        redcolornumber = Math.max(0, Math.min(255, redcolornumber));

        //System.out.println("Blue: " + bluecolornumber + " Green: " + greencolornumber + " Red: " + redcolornumber);

        Color blue = Color.fromRGB(bluecolornumber,greencolornumber,redcolornumber);
        DustOptions dustBlue = new DustOptions(blue, 1);

        durationCurrentTime2 = (durationCurrentTime2/100) - 20;
        bluecolornumber = bluecolornumber - (int) (durationCurrentTime2);
        greencolornumber = greencolornumber - (int) (durationCurrentTime2 );
        redcolornumber = redcolornumber + (int) (durationCurrentTime2) ;

        // Clamp again for lightBlue
        bluecolornumber = Math.max(0, Math.min(255, bluecolornumber));
        greencolornumber = Math.max(0, Math.min(255, greencolornumber));
        redcolornumber = Math.max(0, Math.min(255, redcolornumber));

        Color lightBlue = Color.fromRGB(bluecolornumber,greencolornumber,redcolornumber);
        DustOptions dustLight = new DustOptions(lightBlue, 1);

/*        Color blue = Color.fromRGB(244, 170, 66);
        DustOptions dustBlue = new DustOptions(blue, 1);

        Color lightBlue = Color.fromRGB(255, 221, 112);
        DustOptions dustLight = new DustOptions(lightBlue, 1);
 */

        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "* CORRUPTING LEVEL * " + durationCurrentTime + " out of "+ ((duration*0.5) / 10 )));
        if (hasReached) {
            int amount = chargeTicks + 2;
            double maxHeight = 4;
            double distanceFromPlayer = 1.5;

            int angle = 5 * amount + 5 * ticks;
            double x = Math.cos(Math.toRadians(angle)) * distanceFromPlayer;
            double z = Math.sin(Math.toRadians(angle)) * distanceFromPlayer;
            double height = (amount * 0.10) % maxHeight;
            Location displayLoc = target.getLocation().clone().add(x, height, z);

            int angle2 = 5 * amount + 180 + 5 * ticks;
            double x2 = Math.cos(Math.toRadians(angle2)) * distanceFromPlayer;
            double z2 = Math.sin(Math.toRadians(angle2)) * distanceFromPlayer;
            Location displayLoc2 = target.getLocation().clone().add(x2, height, z2);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, dustBlue);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, dustBlue);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, dustLight);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, dustLight);
        }
    }
    private void createNewSpirals() {
        Color purple = Color.fromRGB(193, 44, 176);
        DustOptions dustPurple = new DustOptions(purple, 1);

        Color darkPurple = Color.fromRGB(201, 58, 137);
        DustOptions dustDark = new DustOptions(darkPurple, 1);

        if (hasReached) {
            int amount = chargeTicks + 2;
            double maxHeight = 4;
            double distanceFromPlayer = 1.5;

            int angle = 5 * amount + 5 * ticks;
            double x = Math.cos(Math.toRadians(angle)) * distanceFromPlayer;
            double z = Math.sin(Math.toRadians(angle)) * distanceFromPlayer;
            double height = (amount * 0.10) % maxHeight;
            Location displayLoc = target.getLocation().clone().add(x, height, z);

            int angle2 = 5 * amount + 180 + 5 * ticks;
            double x2 = Math.cos(Math.toRadians(angle2)) * distanceFromPlayer;
            double z2 = Math.sin(Math.toRadians(angle2)) * distanceFromPlayer;
            Location displayLoc2 = target.getLocation().clone().add(x2, height, z2);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, 0, dustPurple);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, 0, dustPurple);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, 0, dustDark);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, 0, dustDark);
        }
    }

    @Override
    public void load() {}

    @Override
    public void stop() {}

    @Override
    public String getAuthor() {
        return "Prride";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isHiddenAbility() {
        return false; //TODO Temp for now while we redo this ability
    }
}
