package me.numin.spirits.ability.water;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.airbending.Suffocate;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.numin.spirits.SpiritElement;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.WaterAbility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Purify extends WaterAbility {

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
    private boolean charged = false;
    private boolean setElement;
    private int potionduration;
    private int potionlevel;
    long durationCurrentTime;
    long durationCurrentTime2;

    public Purify(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        firstloop: for (int i = 20; i < 100; i++) {
            Location loc = GeneralMethods.getTargetedLocation(player, range);
            for (Entity e : GeneralMethods.getEntitiesAroundPoint(loc, 10)) {
                if (e instanceof LivingEntity && e.getEntityId() != player.getEntityId()) {
                    target = (LivingEntity) e;
                    break firstloop;
                }
            }
        }

        if (target instanceof LivingEntity) {
            heldEntities.add(target.getEntityId());
            setFields();
            this.target = (LivingEntity) target;
            start();
        }

        if (target == null) {
            return;
        }
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Water.Purify.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Water.Purify.Duration");
        this.range = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Water.Purify.Range");
        this.potionduration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.Water.Purify.PotionDuration");
        this.potionlevel = Spirits.plugin.getConfig().getInt("Abilities.Spirits.Water.Purify.PotionLevel");
        this.setElement = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Water.Purify.SetElement");
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
        return "Purify";
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

        if (System.currentTimeMillis() - getStartTime() > duration * 0.5) {
            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.AQUA + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "* READY *"));
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
                    if (bPlayer.hasElement(SpiritElement.DARK)) {
                        bPlayer.addElement(SpiritElement.LIGHT);
                        bPlayer.getElements().remove(SpiritElement.DARK);
                        bPlayer.saveElements();
                        bPlayer.removeUnusableAbilities();
                        target.sendMessage(SpiritElement.DARK.getColor() + "You are now a" + ChatColor.BOLD + "" + ChatColor.AQUA + " LightSpirit");
                        ParticleEffect.FIREWORKS_SPARK.display(target.getLocation(), 3, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0F);
                    } else {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 2));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 300, 2));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2));
                        ParticleEffect.FIREWORKS_SPARK.display(target.getLocation(), 3, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0F);
                    }
                } else if (target instanceof Monster) {
                    Location targetlocation = target.getLocation();
                    Random rand = new Random();
                    int roll = rand.nextInt(100); // 0-99

                    // Zombie Villager -> Villager
                    if (target.getType() == EntityType.ZOMBIE_VILLAGER) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.VILLAGER);
                    }
                    // Zombified Piglin -> Pig
                    else if (target.getType() == EntityType.ZOMBIFIED_PIGLIN) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.PIG);
                    }
                    // Skeleton -> Cow
                    else if (target.getType() == EntityType.SKELETON) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.COW);
                    }
                    // Stray -> Rabbit
                    else if (target.getType() == EntityType.STRAY) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.RABBIT);
                    }
                    // Husk -> Chicken
                    else if (target.getType() == EntityType.HUSK) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.CHICKEN);
                    }
                    // Drowned -> Dolphin
                    else if (target.getType() == EntityType.DROWNED) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.DOLPHIN);
                    }
                    // Witch -> Cat
                    else if (target.getType() == EntityType.WITCH) {
                        target.getWorld().spawnEntity(targetlocation, EntityType.CAT);
                    }
                    // Default: Allay or Sheep
                    else if (roll < 2) { // 2% chance for Allay
                        target.getWorld().spawnEntity(targetlocation, EntityType.ALLAY);
                    } else {
                        org.bukkit.entity.Sheep sheep = (org.bukkit.entity.Sheep) target.getWorld().spawnEntity(targetlocation, EntityType.SHEEP);
                        int colorRoll = rand.nextInt(3);
                        switch (colorRoll) {
                            case 0:
                                sheep.setColor(org.bukkit.DyeColor.PINK);
                                break;
                            case 1:
                                sheep.setColor(org.bukkit.DyeColor.LIGHT_BLUE);
                                break;
                            default:
                                sheep.setColor(org.bukkit.DyeColor.WHITE);
                                break;
                        }
                    }
                    target.remove();
                    ParticleEffect.PORTAL.display(target.getLocation(), 8, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
                } else if (target != null) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, potionduration, potionlevel));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, potionduration, potionlevel));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, potionduration, potionlevel));
                    ParticleEffect.FIREWORKS_SPARK.display(target.getLocation(), 3, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0F);
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
                for (int i = -180; i < 180; i += 10) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 300, 128));
//                    target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 300, 128)); Disabled because of issues when players jump while affected with Purify
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
        // Use bright, pure colors (cyan/yellow/white)
        int bluecolornumber = 200;
        int greencolornumber = 255;
        int redcolornumber = 255;

        Color cyan = Color.fromRGB(155, 255, 250);
        DustOptions dustCyan = new DustOptions(cyan, 1);

        Color yellow = Color.fromRGB(255, 255, 112);
        DustOptions dustYellow = new DustOptions(yellow, 1);

        durationCurrentTime = (System.currentTimeMillis() - getStartTime()) / 10;
        durationCurrentTime2 = (System.currentTimeMillis() - getStartTime());
        durationCurrentTime2 = ((durationCurrentTime2/100)+30);
        durationCurrentTime2 = (duration);

        // Clamp minimums to keep colors bright
        bluecolornumber = Math.max(200, bluecolornumber - (int) (durationCurrentTime2 / 2));
        greencolornumber = Math.max(200, greencolornumber - (int) (durationCurrentTime2 / 3));
        redcolornumber = Math.max(200, redcolornumber - (int) (durationCurrentTime2 / 4));

        bluecolornumber = Math.min(255, bluecolornumber);
        greencolornumber = Math.min(255, greencolornumber);
        redcolornumber = Math.min(255, redcolornumber);

        Color light = Color.fromRGB(bluecolornumber, greencolornumber, redcolornumber);
        DustOptions dustLight = new DustOptions(light, 1);

        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.AQUA + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "* PURIFYING LEVEL * " + durationCurrentTime + " out of "+ ((duration*0.5) / 10 )));
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
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, dustCyan);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, dustCyan);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, dustYellow);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, dustYellow);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, dustLight);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, dustLight);
        }
    }

    private void createNewSpirals() {
        Color cyan = Color.fromRGB(155, 255, 250);
        DustOptions dustCyan = new DustOptions(cyan, 1);

        Color yellow = Color.fromRGB(255, 255, 112);
        DustOptions dustYellow = new DustOptions(yellow, 1);

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
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, dustCyan);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, dustCyan);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc, 1, 0, 0, 0, dustYellow);
            target.getWorld().spawnParticle(Particle.DUST, displayLoc2, 1, 0, 0, 0, dustYellow);
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
        return false;
    }
}
