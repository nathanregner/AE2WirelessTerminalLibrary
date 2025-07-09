package de.mari_023.ae2wtlib.hotkeys;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

import de.mari_023.ae2wtlib.api.AE2wtlibAPI;

public class CycleHotkeyAction {
    public static final KeyMapping NEXT = new KeyMapping("key.ae2.ae2wtlib_cycle_next",
            KeyConflictContext.GUI, KeyModifier.NONE, InputConstants.Type.KEYSYM, InputConstants.KEY_GRAVE,
            "key.ae2.category");

    public static final KeyMapping PREV = new KeyMapping("key.ae2.ae2wtlib_cycle_prev",
            KeyConflictContext.GUI, KeyModifier.SHIFT, InputConstants.Type.KEYSYM, InputConstants.KEY_GRAVE,
            "key.ae2.category");

    public static void register(RegisterKeyMappingsEvent e) {
        e.register(NEXT);
        e.register(PREV);
    }

    public static boolean cycle(int keyCode, int scanCode) {
        if (NEXT.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            AE2wtlibAPI.cycleTerminal(false);
            return true;
        }
        if (PREV.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            AE2wtlibAPI.cycleTerminal(true);
            return true;
        }
        return false;
    }
}
