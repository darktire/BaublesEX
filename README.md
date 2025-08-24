# [BaublesEX](https://www.curseforge.com/minecraft/mc-mods/baublesex)

[![Version](https://cf.way2muchnoise.eu/versions/For%20MC_baublesex_all.svg)](https://www.curseforge.com/minecraft/mc-mods/baublesex)
[![Downloads](https://cf.way2muchnoise.eu/full_baublesex_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/baublesex)

---

# What Does This Mod Do

## This mod maintains most functions of Baubles:
- Miner's Ring
- bauble slots
- bauble api

## Add ~~and wait to add~~ some new functions:
- [x] customizable bauble slots
  - [x] customize default types and quantities in baubles container
  - [x] item can be defined as bauble
  - [x] modify baubles container of each entity independently
- [x] more apis
  - [x] provide the method to add bauble slots for specific  player
  - [x] provide register for bauble type
  - [x] provide register for existed item
  - [x] provide baubles ability for more entity (may have problems)
  - [x] provide event when exchange baubles
  - [x] provide multiple types for one bauble
- [x] more configure
  - [x] json support, use json to:
    - edit type of baubles
    - register your type for baubles
    - see Json Helper below for help
- [x] more command
- [x] add bauble button in creative tab
- [x] elytra can be a bauble in the config
- [x] new style of bauble slots (redraw again)
- [x] right click to equip baubles
- [x] enchantment support for baubles
- [ ] json function
- [ ] visibility of each bauble
- [ ] support for CraftTweaker
- [ ] ...
- [ ] waiting for more suggestions

---

## More Details:

- ### customizable bauble slots in the config:
  - customize bauble types and quantities ![](https://i.imgur.com/JhJC0yM.png)

- ### new style of bauble slots:
  - now the bauble button can open new slots for baubles beside survival inventory. ![](https://i.imgur.com/3ri5oKX.png)

---

## Json Helper:
  - locate in `your_minecraft_floder/config/baubles/*`
  - `items_data.json` and `types_data.json` will be auto created, or you can also create it yourself
  - use command `/baubles debug dump` to get all existed items and types into `items_data.json` and `types_data.json`. remember to backup your json if you have edited, though the dumping will not clear your editions.

### items_data.json:

To set bauble's type/types:
```json
{
  "minecraft:elytra": {
    "types": [
      "baubles:body",
      "baubles:elytra"
    ]
  }
}
```

To make an item not a bauble:
```json
{
  "minecraft:elytra": {
    "addition": "remove"
  }
}
```

### types_data.json:

To set a new type:
```json
{
  "baubles:elytra": {
    "typeName": "elytra",
    "amount": 1,
    "priority": 5
  }
}
```
You can use the resource pack to add textures and i18n names.

---

#  [Original Repo](https://github.com/Azanor/Baubles) / [Download original version from CF](https://www.curseforge.com/minecraft/mc-mods/baubles)