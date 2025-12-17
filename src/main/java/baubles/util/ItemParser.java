package baubles.util;

import baubles.api.IBaubleKey;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ItemParser {

    public static IBaubleKey parse(String input) throws Throwable {
        if (input == null || input.trim().isEmpty()) throw new Throwable(input);

        String rawInput = input.trim();
        int nbtSplitIndex = rawInput.lastIndexOf(":{");
        String basePart;
        String nbtRaw;

        if (nbtSplitIndex != -1) {
            basePart = rawInput.substring(0, nbtSplitIndex).trim();
            nbtRaw = rawInput.substring(nbtSplitIndex + 1).trim();
        }
        else {
            basePart = rawInput;
            nbtRaw = null;
        }

        String[] baseSegments = basePart.split(":");
        String itemName;
        int meta = 0;

        switch (baseSegments.length) {
            case 3:
                itemName = baseSegments[0].trim() + ":" + baseSegments[1].trim();
                meta = parseMeta(baseSegments[2].trim());
                break;

            case 2:
                try {
                    itemName = baseSegments[0].trim();
                    meta = parseMeta(baseSegments[1].trim());
                } catch (NumberFormatException e) {
                    itemName = baseSegments[0].trim() + ":" + baseSegments[1].trim();
                }
                break;

            case 1:
                itemName = baseSegments[0].trim();
                break;

            default:
                throw new Throwable(baseSegments[4]);
        }

        Item item = Item.getByNameOrId(itemName);
        if (item == null) throw new Throwable(itemName);

        if (baseSegments.length == 1) return IBaubleKey.BaubleKey.wrap(item);

        ItemStack stack = new ItemStack(item, 1, meta);
        try {
            if (nbtRaw != null) {
                stack.setTagCompound(JsonToNBT.getTagFromJson(nbtRaw));
            }
        } catch (Throwable e) {
            throw new Throwable(nbtRaw);
        }

        return IBaubleKey.BaubleKey.wrap(stack);
    }

    private static int parseMeta(String metaStr) throws NumberFormatException {
        int meta = Integer.parseInt(metaStr);
        if (meta < 0) {
            throw new NumberFormatException(metaStr);
        }
        return meta;
    }

    public static String itemDef(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        int meta = stack.getMetadata();
        NBTTagCompound nbt = stack.getTagCompound();
        return (meta == 0 && nbt == null) ? String.valueOf(name) : name + ":" + meta + ":" + nbt;
    }
}
