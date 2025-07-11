package de.mari_023.ae2wtlib.api.terminal;

import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.util.IConfigManager;
import appeng.core.AEConfig;
import appeng.helpers.WirelessTerminalMenuHost;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.menu.MenuOpener;
import appeng.menu.locator.ItemMenuHostLocator;
import appeng.menu.locator.MenuLocators;
import de.mari_023.ae2wtlib.api.AE2wtlibAPI;
import de.mari_023.ae2wtlib.api.registration.WTDefinition;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class ItemWT extends WirelessTerminalItem {
    public ItemWT() {
        super(AEConfig.instance().getWirelessTerminalBattery(), new Item.Properties().stacksTo(1));
    }

    public boolean open(final Player player, final ItemMenuHostLocator locator,
                        boolean returningFromSubmenu) {
        var menuType = getMenuType(locator, player);
        try {
            if (player.containerMenu.getType() == menuType) {
                return false;
            }
        } catch (UnsupportedOperationException ignored) {
        }
        return MenuOpener.open(menuType, player, locator, returningFromSubmenu);
    }

    public boolean tryOpen(Player player, ItemMenuHostLocator locator, boolean returningFromSubmenu) {
        if (checkPreconditions(locator.locateItem(player)))
            return open(player, locator, returningFromSubmenu);
        return false;
    }

    public abstract MenuType<?> getMenuType(ItemMenuHostLocator locator, Player player);

    protected boolean checkPreconditions(ItemStack item) {
        return !item.isEmpty()
                && (item.getItem() == this || AE2wtlibAPI.isUniversalTerminal(item.getItem()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        ItemStack is = player.getItemInHand(hand);
        if (checkPreconditions(is)) {
            if (!level.isClientSide())
                open(player, MenuLocators.forHand(player, hand), false);
            return new InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide()), is);
        }
        return new InteractionResultHolder<>(InteractionResult.FAIL, is);
    }

    @Override
    public WirelessTerminalMenuHost<?> getMenuHost(Player player, ItemMenuHostLocator locator,
                                                   @Nullable BlockHitResult hitResult) {
        return WTDefinition.of(locator.locateItem(player)).wTMenuHostFactory().create(this, player, locator,
                (p, subMenu) -> tryOpen(player, locator, true));
    }

    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    /**
     * Return the config manager for the wireless terminal.
     *
     * @return config manager of wireless terminal
     */
    public IConfigManager getConfigManager(Supplier<ItemStack> target) {
        return AE2wtlibConfigManager.builder(target)
                .registerSetting(Settings.SORT_BY, SortOrder.NAME)
                .registerSetting(Settings.VIEW_MODE, ViewItems.ALL)
                .registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING)
                .build();
    }
}
