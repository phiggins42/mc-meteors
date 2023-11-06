# MeteorPlugin

An experiment in animated block movement, rain a series of random meteors (`Material.MAGMA_BLOCK`) down from the sky,
exploding on impact. 

Blocks turn to Obsidian when intersecting water, and start fires when intersecting burnables along the path.

Makes a huge mess.

Mostly just an experiment in animation, vector travel, and using Runnables.

## Usage

Copy the `jar` to your `plugins/` folder, and reload/restart.

Provides one command, `shower` and a `duration`. The duration is milliseconds.  

```
/shower 50000
```

All online players will be "bombarded" with falling Magma Blocks at various vectors, exploding on impact with the first non-air block the path travels through.

## TODO

* make `shower` an OP-only command, so users can't just destruct-o the world
* switch animation to time-based progress for a more natural feel
* Find floating blocks and make them fall?
* leave a few smoldering magma blocks on the ground at point of impact
* configuration files, allowing tweaking of frequency of meteor generation, impact area size, randomness from player position, etc.
* Lore?
    * Custom items that nullify meteor impact? 
    * Items that trigger single meteor strikes? 
    * Cursed Items that make a single player the target?
* Better long-lived apocalyptic progression, "The End is Nigh", where the end of the timer is a full out barrage of destruction


