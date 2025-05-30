package baubles.common.extra;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesRegister;
import baubles.common.Baubles;
import baubles.common.config.Config;

import java.io.FileNotFoundException;
import java.io.IOException;

import static baubles.common.Baubles.config;

public class BaublesContent extends BaublesRegister {
    public BaublesContent() {
        if (Config.jsonFunction) {
            readJson();
        }
        else {
            this.registerBaubles();
            this.loadValidSlots();
        }
    }

    @Override
    public void registerBaubles() {
        super.registerBaubles();
        int amount = 0;
        int value;
        for (BaubleType type : BaubleType.values()) {
            try {
                value = config.getAmount(type.name());
                if (type.equals(BaubleType.TRINKET) & !Config.trinketLimit) value = 0;
                baubles.get(type.getTypeName()).setAmount(value);
                amount += value;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                if (e instanceof NoSuchFieldException) {
                    Baubles.log.warn("Bauble type " + type.name() + " loading failed");
                }
            }
        }
        if (!Config.trinketLimit & Config.TRINKET > amount) {
            baubles.get(BaubleType.TRINKET.getTypeName()).setAmount(Config.TRINKET - amount);
            amount = Config.TRINKET;
        }
        sum = amount;
    }

    @Override
    public void loadValidSlots() {
        super.loadValidSlots();
        if (!Config.trinketLimit) {
            BaubleTypeEx trinket = baubles.get("trinket");
            int[] trinketSlots = new int[sum];
            for (int i = 0; i < sum; i++) {
                trinketSlots[i] = i;
            }
            trinket.setValidSlots(trinketSlots);
        }
    }

    public void writeJson() {
        try {
            Baubles.jsonHelper.typesToJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readJson() {
        try {
            Baubles.jsonHelper.jsonToType();
        } catch (FileNotFoundException e) {
            this.registerBaubles();
            this.loadValidSlots();
            writeJson();
        }
    }
}
//todo capability to modify slots