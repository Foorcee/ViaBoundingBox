package de.foorcee.viaboundingbox.bukkit;

import de.foorcee.viaboundingbox.common.BoundingBoxRegistry;
import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;
import de.foorcee.viaboundingbox.common.asm.PlayerConnectionInjector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ViaBoundingBoxPlugin extends JavaPlugin{

    @Override
    public void onLoad() {
        try {
            PlayerConnectionInjector.loadClasses(Bukkit.class.getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        AbstractBoundingBoxInjector injector = BoundingBoxRegistry.getInjector(serverVersion);
        if(injector != null){
            try{
                new ViaBoundingBoxBukkitModule(injector);
            }catch (Exception ex){
                ex.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }else{
            System.out.println("Unable to find a injector for " + serverVersion);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
