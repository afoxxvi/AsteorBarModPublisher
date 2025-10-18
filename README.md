# AsteorBarModPublisher

Publish mod files to Modrinth and CurseForge for the AsteorBar Minecraft mod.

Just a garbage project, but at least it works.

Although I know this could be done with a simple shell script, I do it for fun, because Kotlin is beautiful :)

## Usage

1. create a `publish.properties` file under `src/main/resources`, and copy the content from `example.publish.properties` to it.
2. fill in the required fields in `publish.properties`.
3. edit `Main.kt` to set mod loaders and game versions.
4. run the `main` function in `Main.kt`.
