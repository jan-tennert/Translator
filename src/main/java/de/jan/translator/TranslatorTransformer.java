package de.jan.translator;

import net.labymod.addon.AddonTransformer;
import net.labymod.api.TransformerType;

public class TranslatorTransformer extends AddonTransformer {

    @Override
    public void registerTransformers() {
        this.registerTransformer(TransformerType.VANILLA, "translator.mixin.json");
    }
}
