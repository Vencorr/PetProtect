# PetProtect
PP is a really simple plugin which helps protect user pets from being killed, stolen, or items taken away by evil doers. Denying access to hurting, riding, or accessing chests from pets that aren't from the owners unless with the proper permissions, PetProtect is a simple solution to a chaotic wrongdoing.

## Permissions
PetProtect uses 3 permissions to control certain actions.
`petprotect.hurt` controls hitting another pet. Setting to true will allow the user to hurt any pet.
`petprotect.ride` controls riding pets such as horses or donkys. Setting to true will allow the user to ride any pet.
`petprotect.access` controls accessing chests and inventory of pets. Setting to true will allow the user to take or give anything from any pets inventory.

## Config.yml
`enabled` - On/Off switch for the plugin.
The tag '{player}' is used to display the playername.
The tag '{pet}' is used to display the pet type.
Example: `'No, that is {player}'s! Not your {pet}!'` turns into 'No, that is Notch's! Not your dog!'
`hurt` - Hurt message to display to player if no permissions.
`ride` - Ride message to display to player if no permissions.
`access` - Access message to display to player if no permissions.
The rest are mob names. If you wish to name some mobs differently, the option is available.
