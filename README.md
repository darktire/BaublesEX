# [BaublesEX](https://www.curseforge.com/minecraft/mc-mods/baublesex)

[![Version](https://cf.way2muchnoise.eu/versions/For%20MC_baubles-ex_all.svg)](https://www.curseforge.com/minecraft/mc-mods/baublesex)
[![Downloads](https://cf.way2muchnoise.eu/full_baubles-ex_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/baublesex)

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
  - [x] provide easy system for rendering bauble model (support enchanted glint)
- [x] more configure
  - [x] json support, use json to:
    - edit type of baubles
    - register your type for baubles
    - see Json Helper below for help
- [x] control the visibility of baubles. The following are the mods that can be controlled:
  - [x] RLArtifacts
  - [x] Bountiful Baubles
  - [x] Trinkets and Baubles
  - [x] Enigmatic Legacy
  - [x] I&F_RL
  - [x] Thaumcraft
  - [x] Thaumic Periphery
  - [ ] Aether
  - [ ] Botania
  - [ ] ...
- [x] extra supports for mods
  - [x] all models support Mo' Bends
  - [x] aether accessory
  - [x] CraftTweaker
- [x] more commands
- [x] add bauble button in creative tab
- [x] elytra can be a bauble in the config
- [x] new style of bauble slots (redraw again)
- [x] right click to equip baubles
- [x] enchantment support for baubles
- [ ] ...
- [ ] waiting for more suggestions

---

## More Details:

- ### customizable bauble slots in the config:
  - customize bauble types and quantities ![](https://i.imgur.com/JhJC0yM.png)

- ### new style of bauble slots:
  - now the bauble button can open new slots for baubles beside survival inventory. ![](https://i.imgur.com/3ri5oKX.png)

- ### control the visibility of baubles:
  - add switchers for each slot. ![](https://i.imgur.com/9PWpoec.png)

---

## Json Helper:
  - locate in `your_minecraft_floder/config/baubles/*`
  - `items_data.json` and `types_data.json` will be auto created, or you can also create it yourself
  - use command `/baubles dump` to get all existed items and types into `items_dump.json` and `types_dump.json`.

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