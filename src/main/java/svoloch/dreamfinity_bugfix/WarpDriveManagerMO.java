package svoloch.dreamfinity_bugfix;

import matteroverdrive.entity.player.AndroidPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class WarpDriveManagerMO {
    public static boolean isAndroid(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return AndroidPlayer.get((EntityPlayer) entity).isAndroid();
        } else {
            return false;
        }
    }

    public static boolean isAndroidSP() {
        Minecraft minecraft = Minecraft.getMinecraft();
        final EntityPlayer entityPlayer = minecraft.thePlayer;
        if (entityPlayer == null) {
            return false;
        }
        return AndroidPlayer.get(entityPlayer).isAndroid();
    }
}
