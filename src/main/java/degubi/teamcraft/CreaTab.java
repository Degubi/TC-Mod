package degubi.teamcraft;

import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

public final class CreaTab extends CreativeTabs {
    private final ItemStack icon;

    public CreaTab(String label, Item item) {
        super(label);
        icon = new ItemStack(item);
    }

    public CreaTab(String label, Block block) {
        super(label);
        icon = new ItemStack(block);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return icon;
    }
}