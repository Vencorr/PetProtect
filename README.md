# PetProtect
PetProtect is a small plugin that attempts to negate the issue of malicious players killing pets. It's purpose is to protect the pets from being stolen, accessed, and killed by players other than the owner. Supporting any tameable mob, players will be unable to hurt, leash, ride, access, or shoot any pet they do not own.

## Compiling and Building
This project uses maven, so building is as easy as any other maven project. Java 8 is required.
https://github.com/Vencorr/PetProtect
```
git clone https://github.com/Vencorr/PetProtect.git
cd PetProtect
mvn install
```

The compiled jar is available in the target folder.

## Permissions
PetProtect uses 3 permissions to control certain actions.


`petprotect.hurt` controls hitting another pet. Setting to true will allow the user to hurt any pet.

`petprotect.ride` controls riding pets such as horses or donkeys. Setting to true will allow the user to ride any pet.

`petprotect.access` controls accessing pets. Setting to true will allow the user to take or give anything from any pets inventory and leash them.

## Config.yml
A configurable file is available in the PetProtect directory in the plugins folder.


`enabled` - On/Off switch for the plugin.

`actionbar` - Whether to use Actionbar text over chat messages.

### Protection

`hurt` - Protect against hurt attempts.
`ride` - Protect against ride attempts.
`access` - Protect against access attempts.

### Messages
Tags are {pet} and {owner}. Color codes are supported.

`message` - Message to send on any interaction.

`altmessage` - Message to send on any interaction if the owner is not found. Using '{owner}' could cause a NullPointerException.

`customname` - Whether to allow pet custom names in message or use the entity name.
