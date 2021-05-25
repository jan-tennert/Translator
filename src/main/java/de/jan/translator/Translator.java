package de.jan.translator;

import de.jan.translator.screens.TranslatorScreen;
import de.jan.translator.translators.GoogleTranslator;
import de.jan.translator.translators.ITranslator;
import net.labymod.api.LabyModAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.TickEvent;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.settings.elements.KeyElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Keyboard;
import net.minecraft.client.Minecraft;

import java.util.List;

public class Translator extends LabyModAddon {

    private int openTranslator = 48;
    private static Translators translator = Translators.GOOGLE_TRANSLATE;

    @Override
    public void onEnable() {
        getApi().getEventService().registerListener(this);
    }

    @Override
    public void loadConfig() {
        openTranslator = getConfig().has("openTranslator") ? getConfig().get("openTranslator").getAsInt() : openTranslator;
        translator = getConfig().has("translator") ? Translators.valueOf(getConfig().get("translator").getAsString()) : translator;
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new KeyElement("Open Translator", this, null, "openTranslator", openTranslator));

        DropDownMenu<Translators> translators = new DropDownMenu<Translators>(null, 0, 0, 0, 0)
                .fill(Translators.values());
        translators.setSelected(Translators.GOOGLE_TRANSLATE);
        DropDownElement<Translators> translatorsElement = new DropDownElement<>("Translator", translators);
        translatorsElement.setChangeListener(i -> {
            getConfig().addProperty("translator", i.name());
            translator = i;
        });
        list.add(translatorsElement);
    }

    @Subscribe
    public void onTick(TickEvent e) {
        if (LabyMod.getInstance().isInGame() && Minecraft.getInstance().currentScreen == null && Keyboard.isKeyDown(openTranslator)) {
            System.out.println("Gui opened");
            Minecraft.getInstance().displayGuiScreen(new TranslatorScreen());
        }
    }

    public static ITranslator getTranslator() {
        switch (translator) {
            case GOOGLE_TRANSLATE:
                return new GoogleTranslator();
            default:
                return new GoogleTranslator();
        }
    }
}
