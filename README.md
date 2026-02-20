# AsteorBarModPublisher

Publish mod files to Modrinth and CurseForge for the AsteorBar Minecraft mod.

Just a garbage project, but at least it works.

Although I know this could be done with a simple shell script, I do it for fun, because Kotlin is beautiful :)

## Usage

1. create a `publish.properties` file under `src/main/resources`, and copy the content from `example.publish.properties` to it.
2. fill in the required fields in `publish.properties`.
3. edit `Main.kt` to set mod loaders and game versions.
4. run the `main` function in `Main.kt`.

### Update CurseForge version data (optional)

Located at `src/main/resources/curseforge_versions_data.json`, may not up to date.
To retrieve the latest version data, go to the author console of CurseForge, select a project, 
press F12 to open the developer tools and switch to "Network" tab, filter by "create-project-file-form-data",
then go to "Files" tab, click "Add File".

You will see a request with the URL "https://authors.curseforge.com/_api/project-files/{project-id}/create-project-file-form-data?gameId=432&classId=6", 
which contains a JSON response with keys "versionsData", "acceptedFileTypes" and "additionalFileInfoOptions", 
only the "versionsData" is needed, which is a nested JSON array, just copy the whole array and replace the content of `curseforge_versions_data.json` with it.

These steps may no longer work in the future if CurseForge changes their website, 
but it's believed that this version info is retrieved when creating a new file that requires the user to select versions and mod loaders.
