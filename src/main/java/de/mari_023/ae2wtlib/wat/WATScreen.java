package de.mari_023.ae2wtlib.wat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ToolboxPanel;

import de.mari_023.ae2wtlib.api.gui.ScrollingUpgradesPanel;
import de.mari_023.ae2wtlib.api.terminal.IUniversalTerminalCapable;
import de.mari_023.ae2wtlib.api.terminal.WTMenuHost;
import de.mari_023.ae2wtlib.hotkeys.CycleHotkeyAction;

public class WATScreen extends PatternAccessTermScreen<WATMenu> implements IUniversalTerminalCapable {
    private final ScrollingUpgradesPanel upgradesPanel;

    public WATScreen(WATMenu container, Inventory playerInventory, Component title, ScreenStyle style) {
        super(container, playerInventory, title, style);
        if (getMenu().isWUT())
            addToLeftToolbar(cycleTerminalButton());

        upgradesPanel = addUpgradePanel(widgets, getMenu());
        if (getMenu().getToolbox().isPresent())
            widgets.add("toolbox", new ToolboxPanel(style, getMenu().getToolbox().getName()));
    }

    @Override
    public void init() {
        super.init();
        upgradesPanel.setMaxRows(Math.max(2, getVisibleRows()));
    }

    @Override
    public WTMenuHost getHost() {
        return (WTMenuHost) getMenu().getHost();
    }

    @Override
    public void storeState() {}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int keyPressed) {
        if (CycleHotkeyAction.cycle(keyCode, scanCode))
            return true;

        boolean value = super.keyPressed(keyCode, scanCode, keyPressed);
        if (!value)
            return checkForTerminalKeys(keyCode, scanCode);
        return true;
    }
}
