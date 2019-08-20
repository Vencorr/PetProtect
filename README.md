# PetProtect
PetProtect is a small plugin that attempts to negate the issue of malicious players killing pets. It's purpose is to protect the pets from being stolen, accessed, and killed by players other than the owner. Supporting any tameable mob, players will be unable to hurt, leash, ride, access, or shoot any pet they do not own.

## Compiling and Building
This project uses maven, so building is as easy as any other maven project. Java 8 is required.

```
git clone https://github.com/Vencorr/PetProtect.git
cd PetProtect
mvn install
```

The compiled jar is available in the target or builds folder.

## Permissions
PetProtect uses 3 permissions to control certain actions.

`petprotect.hurt` controls hitting another pet. Setting to true will allow the user to hurt any pet.

`petprotect.ride` controls riding pets such as horses or donkeys. Setting to true will allow the user to ride any pet.

`petprotect.access` controls accessing pets. Setting to true will allow the user to take or give anything from any pets inventory and leash them.

## Config.yml
A configurable file is available in the PetProtect directory in the plugins folder.


`enabled` - This completely disables the plugin on startup if set to false

### Protection

`pet-invulnerable` - Make pets invulnerable to the environment (blocks)

`owner-protect` - Prevent owners from hitting their pets

`hurt` - Protect against hurt

`ride` - Protect against riding

`access` - Protect against leads and inventory access

`exclude` - Not yet implemented (0.3.x will do nothing with it)

### Messages
Tags are {pet} and {owner}. Color codes are supported.

`messagetype` - Type of message (0 = none, 1 = chat, 2 = actionbar)

`message` - Message shown to player if interacting with pet that is not theirs

`altmessage` - Message shown to player if interacting with pet that has no owner or if the player is the owner

`customname` - Whether to use custom names from pets for messages
