package de.jan.translator.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.jan.translator.Language;
import de.jan.translator.Translator;
import de.jan.translator.translators.ITranslator;
import de.jan.translator.translators.Translated;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

public class TranslatorScreen extends Screen {

    private ModTextField inputText;
    private String loadValue;
    private ModTextField result;
    private String text = null;
    private Language language = Language.ENGLISH;
    private DropDownElement<Language> languageToMenu;
    private DropDownElement<Language> languageFromMenu;
    private Language languageFrom = Language.AUTO;

    public TranslatorScreen() {
        super(new StringTextComponent("Translator"));
    }

    public TranslatorScreen(String loadValue) {
        super(new StringTextComponent("Translator"));
        this.loadValue = loadValue;
    }

    @Override
    protected void init() {
        this.result = new ModTextField(0, LabyMod.getInstance().getDrawUtils().getFontRenderer(), this.width / 2 - 75, this.height / 2 + 10, 170, 20);
        result.setMaxStringLength(500);
        this.inputText = new ModTextField(0, LabyMod.getInstance().getDrawUtils().getFontRenderer(), this.width / 2 - 75, this.height / 2 - 40, 170, 20);
        inputText.setMaxStringLength(500);
        if (loadValue != null) inputText.setText(loadValue);
        DropDownMenu<Language> languageToMenu = new DropDownMenu<Language>("Translate to", 0, 0, 0, 0)
                .fill(Arrays.stream(Language.values()).filter(l -> l != Language.AUTO).toArray(Language[]::new));
        this.languageToMenu = new DropDownElement<Language>(null, languageToMenu);
        languageToMenu.setSelected(Language.ENGLISH);
        this.languageToMenu.setChangeListener(l -> language = l);
        DropDownMenu<Language> languageFromMenu = new DropDownMenu<Language>("Translate from", 0, 0, 0, 0)
                .fill(Language.values());
        this.languageFromMenu = new DropDownElement<Language>(null, languageFromMenu);
        languageFromMenu.setSelected(Language.AUTO);
        this.languageFromMenu.setChangeListener(l -> languageFrom = l);
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        renderBackground(matrixStack);
        this.inputText.drawTextBox(matrixStack);
        this.result.drawTextBox(matrixStack);
        this.languageToMenu.draw(matrixStack, this.width / 2 + 100, this.height / 2 - 20, this.width / 2 + 180, this.height / 2 + 10, mouseX, mouseY);
        this.languageFromMenu.draw(matrixStack, this.width / 2 - 160, this.height / 2 - 20, this.width / 2 - 80, this.height / 2 + 10, mouseX, mouseY);
        this.addButton(new Button(this.width / 2, this.height / 2 - 15, 60, 20, new StringTextComponent("Translate"), a -> {
            ITranslator translator = Translator.getTranslator();
            String text = this.text;
            if (text == null) {
                text = this.inputText.getText();
                if (text == null) {
                    text = "Error while translating";
                }
            }
            Translated translated = translator.translate(text, language.getKey(), languageFrom.getKey());
            result.setText(translated.getTranslatedText());
        }));
        this.addButton(new Button(this.width / 2 - 40, this.height / 2 + 40, 100, 20, new StringTextComponent("Copy to Clipboard"), a -> {
            if (result.getText() != null && !result.getText().isEmpty()) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(result.getText()), null);
            }
        }));
        draw.drawImageUrl(matrixStack, "https://i.imgur.com/zo7lZZl.png", (int) (this.width / 2) - 30, (int) (this.height / 2) - 20, 256, 256, 30, 30);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.inputText.keyPressed(keyCode, scanCode, modifiers);
        this.result.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (inputText.textboxKeyTyped(codePoint, modifiers)) {
            text = inputText.getText();
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.inputText.mouseClicked((int) mouseX, (int) mouseY, button);
        this.languageToMenu.onClickDropDown((int) mouseX, (int) mouseY, button);
        this.languageFromMenu.onClickDropDown((int) mouseX, (int) mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.languageToMenu.mouseRelease((int) mouseX, (int) mouseY, button);
        this.languageFromMenu.mouseRelease((int) mouseX, (int) mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.languageToMenu.onScrollDropDown(delta);
        this.languageFromMenu.onScrollDropDown(delta);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}
