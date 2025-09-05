package net.yiran.extraholopage.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

import java.util.*;

public class StatBarShowManager {
    public static class Internal {
        public static StatBarShowManager INSTANCE = new StatBarShowManager();
    }

    public Map<String, ShowHandler> LabelToHandle = new HashMap<>();
    public Multimap<String, Item> ItemGroup = ArrayListMultimap.create();

    public StatBarShowManager() {
        addItemInGroup("bow", ModularBowItem.instance);
        addItemInGroup("crossbow", ModularCrossbowItem.instance);
        addItemInGroup("ranged", ModularBowItem.instance, ModularCrossbowItem.instance);
        addItemInGroup("weapon", ModularBladedItem.instance, ModularDoubleHeadedItem.instance, ModularSingleHeadedItem.instance);
        addItemInGroup("shield", ModularShieldItem.instance);

        registerAllowWithGroup("weapon", "tetra.stats.speed", "tetra.stats.attack_damage");
        registerAllowWithGroup("shield", "tetra.stats.blocking_reflect");
        registerAllowWithGroup("ranged", "tetra.stats.spread", "tetra.stats.draw_speed", "tetra.stats.draw_strength");
        //registerSimplyHandle("tetra.stats.throwable", checkItem -> ShowResult.SUCCESS);
    }

    public static void addGroupItem(String group, Item... item) {
        getInstance().addItemInGroup(group, item);
    }

    public static Set<String> getAllGroups() {
        return getInstance().ItemGroup.keySet();
    }

    public static Collection<Item> getItemsInGroup(String group) {
        return getInstance().ItemGroup.get(group);
    }

    public static StatBarShowManager getInstance() {
        return Internal.INSTANCE;
    }

    public void addItemInGroup(String group, Item item) {
        ItemGroup.put(group, item);
    }

    public void addItemInGroup(String group, Item... item) {
        ItemGroup.putAll(group, List.of(item));
    }

    public void registerAllowWithGroup(String group, String label) {
        registerSimplyHandle(label, checkItem -> ItemGroup.containsKey(group) ? ItemGroup.get(group).contains(checkItem) ? ShowResult.SKIP : ShowResult.FAIL : ShowResult.FAIL);
    }

    public void registerAllowWithGroup(String group, String... labels) {
        for (String label : labels) {
            registerAllowWithGroup(group, label);
        }
    }

    public void registerDestroyWithGroup(String group, String label) {
        registerSimplyHandle(label, checkItem -> ItemGroup.containsKey(group) ? ItemGroup.get(group).contains(checkItem) ? ShowResult.FAIL : ShowResult.SKIP : ShowResult.SKIP);
    }

    public void registerDestroyWithGroup(String group, String... labels) {
        for (String label : labels) {
            registerDestroyWithGroup(group, label);
        }
    }

    public void registerSimplyHandle(String label, ShowSimplyHandler handler) {
        registerHandler(label, handler);
    }

    public void registerHandler(String label, ShowHandler handler) {
        LabelToHandle.put(label, handler);
    }

    public ShowResult handle(String label, ItemStack stack) {
        if (!LabelToHandle.containsKey(label)) {
            return ShowResult.SKIP;
        }
        return LabelToHandle.get(label).check(stack);
    }

    @FunctionalInterface
    public interface ShowHandler {
        ShowResult check(ItemStack checkStack);
    }

    @FunctionalInterface
    public interface ShowSimplyHandler extends ShowHandler {
        ShowResult check(Item checkItem);

        default ShowResult check(ItemStack checkStack) {
            return check(checkStack.getItem());
        }
    }

    public enum ShowResult {
        SUCCESS,
        FAIL,
        SKIP
    }
}
