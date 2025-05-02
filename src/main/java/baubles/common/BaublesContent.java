package baubles.common;

import baubles.api.BaubleType;
import baubles.api.BaublesRegister;
import baubles.common.config.Config;
import baubles.common.config.json.JsonHelper;

import java.io.FileNotFoundException;
import java.io.IOException;

import static baubles.common.Baubles.config;

public class BaublesContent extends BaublesRegister {
    JsonHelper jsonHelper = new JsonHelper();
    public BaublesContent() {
        if (Config.jsonFunction) {
            readJson();
        }
        else {
            this.init();
        }
    }
    @Override
    public void init() {
        super.init();
        for (BaubleType type : BaubleType.values()) {
            try {
                int value = config.getAmount(type.name());
                baubles.get(type.getTypeName()).setAmount(value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                if (e instanceof NoSuchFieldException) {
                    Baubles.log.warn("Bauble type " + type.name() + " loading failed");
                }
            }
        }
        super.loadValidSlots();
    }

    public void writeJson() {
        try {
            jsonHelper.typesToJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readJson() {
        try {
            jsonHelper.jsonToType();
        } catch (FileNotFoundException e) {
            this.init();
            writeJson();
        }
        try {
            jsonHelper.jsonToType();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}