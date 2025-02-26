package com.teampotato.comparties;

public class CompatHandler {
    private static CompatHandler instance;
    public final boolean ironMagicLoaded;
    public static CompatHandler getInstance() {
        if (instance == null) instance = new CompatHandler();
        return instance;
    }
    public CompatHandler() {
        ironMagicLoaded = getViaCfgAndClass();
    }
    private boolean getViaCfgAndClass() {
        try {
            Class.forName("io.redspace.ironsspellbooks.IronsSpellbooks", false, this.getClass().getClassLoader());
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }
}
