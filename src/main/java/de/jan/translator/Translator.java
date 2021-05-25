package de.jan.translator;

import de.jan.translator.screens.TranslatorScreen;
import de.jan.translator.translators.GoogleTranslator;
import de.jan.translator.translators.ITranslator;
import de.jan.translator.translators.Translated;
import net.labymod.api.LabyModAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.TickEvent;
import net.labymod.api.event.events.client.chat.MessageReceiveEvent;
import net.labymod.api.event.events.client.chat.MessageSendEvent;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Arrays;
import java.util.List;

public class Translator extends LabyModAddon {

    private int openTranslator = 48;
    private static Translators translator = Translators.GOOGLE_TRANSLATE;
    private Language chatLanguage = Language.GERMAN;
    private boolean enabled = true;
    private boolean chatTranslatorEnabled = false;
    private boolean openGUI = false;

    @Override
    public void onEnable() {
        getApi().getEventService().registerListener(this);
    }

    @Override
    public void loadConfig() {
        openTranslator = getConfig().has("openTranslator") ? getConfig().get("openTranslator").getAsInt() : openTranslator;
        translator = getConfig().has("translator") ? Translators.valueOf(getConfig().get("translator").getAsString()) : translator;
        chatLanguage = getConfig().has("chatLanguage") ? Language.valueOf(getConfig().get("chatLanguage").getAsString()) : chatLanguage;
        enabled = getConfig().has("enabled") ? getConfig().get("enabled").getAsBoolean() : true;
        chatTranslatorEnabled = getConfig().has("chatTranslatorEnabled") ? getConfig().get("chatTranslatorEnabled").getAsBoolean() : false;
        openGUI = getConfig().has("openGUI") ? getConfig().get("openGUI").getAsBoolean() : false;
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement("General"));
        list.add(new BooleanElement("Enabled", this, null, "enabled", enabled));
        list.add(new HeaderElement("Translator GUI"));
        list.add(new KeyElement("Open Translator", this, null, "openTranslator", openTranslator));
        DropDownMenu<Translators> translators = new DropDownMenu<Translators>("Translator", 0, 0, 0, 0)
                .fill(Translators.values());
        translators.setSelected(Translators.GOOGLE_TRANSLATE);
        DropDownElement<Translators> translatorsElement = new DropDownElement<>(null, translators);
        translatorsElement.setChangeListener(i -> {
            getConfig().addProperty("translator", i.name());
            translator = i;
        });
        list.add(translatorsElement);
        list.add(new HeaderElement("Chat Translator"));
        DropDownMenu<Language> languages = new DropDownMenu<Language>("Translate to", 0, 0, 0, 0)
                .fill(Arrays.stream(Language.values()).filter(l -> l != Language.AUTO).toArray(Language[]::new));
        languages.setSelected(Language.GERMAN);
        DropDownElement<Language> languageElement = new DropDownElement<>(null, languages);
        languageElement.setChangeListener(l -> {
            getConfig().addProperty("chatLanguage", l.name());
            chatLanguage = l;
        });
        languageElement.setDescriptionText("The language will be used to auto translate in this language, if the translate button in the chat is pressed");
        list.add(languageElement);
        list.add(new BooleanElement("Open GUI instead of sending a message", this, null, "openGUI", false));
        list.add(new BooleanElement("Enabled", this, null, "chatTranslatorEnabled", chatTranslatorEnabled));
    }

    @Subscribe
    public void onMessageSend(MessageSendEvent e) {
        if (e.getMessage().startsWith("/translate") && enabled) {
            e.setCancelled(true);
            String text = e.getMessage().replace("/translate ", "");
            if (openGUI) {
                Minecraft.getInstance().displayGuiScreen(new TranslatorScreen(removeName(text)));
            } else {
                Translated translated = getTranslator().translate(removeName(text), chatLanguage.getKey(), "auto");
                StringTextComponent t1 = new StringTextComponent("Translated: ");
                t1.setStyle(t1.getStyle().setColor(Color.fromHex("#59ff00")));
                StringTextComponent msg = new StringTextComponent(translated.getTranslatedText());
                msg.setStyle(msg.getStyle()
                        .setColor(Color.fromHex("#ffffff"))
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to copy")))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, translated.getTranslatedText())));
                Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(t1.append(msg));
            }
        }
    }

    private String removeName(String c) {
        String author = null;
        if (Minecraft.getInstance().isIntegratedServerRunning()) {
            for (String player : Minecraft.getInstance().getIntegratedServer().getOnlinePlayerNames()) {
                if (c.contains(player)) {
                    author = player;
                    break;
                }
            }
        } else if (Minecraft.getInstance().getConnection() != null) {
            for (NetworkPlayerInfo player : Minecraft.getInstance().getConnection().getPlayerInfoMap()) {
                if (player.getDisplayName() != null && c.contains(player.getDisplayName().getString())) {
                    author = player.getDisplayName().getString();
                    break;
                }
            }
        }

        if (c.contains(author)) {
            if (c.contains("<" + author + ">")) {
                author = c.replace("<" + author + ">", "");
            } else if (c.contains(author + ":")) {
                author = c.replace(author + ":", "");
            } else {
                author = c.replace(author, "");
            }
        }
        return author;
    }

    @Subscribe
    public void onMessageReceive(MessageReceiveEvent e) {
        if (enabled && chatTranslatorEnabled && !e.getComponent().getString().contains("Translated:")) {
            String message = e.getComponent().getString();
            StringTextComponent translateButton1 = new StringTextComponent(" [");
            translateButton1.setStyle(translateButton1.getStyle().setColor(Color.fromHex("#ffffff")));
            StringTextComponent translateButton2 = new StringTextComponent("]");
            translateButton2.setStyle(translateButton2.getStyle().setColor(Color.fromHex("#ffffff")));
            StringTextComponent translateButtonText = new StringTextComponent("Translate");
            Style style = translateButtonText.getStyle();
            style = style.setColor(Color.fromHex("#04ff00"));
            style = style.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/translate " + message));
            translateButtonText.setStyle(style);
            e.getComponent().getSiblings().addAll(Arrays.asList(translateButton1, translateButtonText, translateButton2));
        }
    }

    @Subscribe
    public void onTick(TickEvent e) {
        if (LabyMod.getInstance().isInGame() && Minecraft.getInstance().currentScreen == null && Keyboard.isKeyDown(openTranslator) && enabled) {
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
