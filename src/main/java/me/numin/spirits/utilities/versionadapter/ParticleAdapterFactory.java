package me.numin.spirits.utilities.versionadapter;

import com.projectkorra.projectkorra.GeneralMethods;
import org.bukkit.Bukkit;

public class ParticleAdapterFactory {

    private ParticleAdapter adapter;

    public ParticleAdapterFactory() {
        int serverVersion = GeneralMethods.getMCVersion();

        if (serverVersion >= 1219) {
            Bukkit.getLogger().info("[Spirits] Using 1.21.9+ ParticleAdapter");
            adapter = new ParticleAdapter_1_21_9();
        } else {
            Bukkit.getLogger().info("[Spirits] Using 1.21.8- ParticleAdapter");
            adapter = new ParticleAdapter_1_21_8();
        }
    }

    public ParticleAdapter getAdapter() {
        return adapter;
    }
}

//ParticleAdapter by https://github.com/CozmycDev/